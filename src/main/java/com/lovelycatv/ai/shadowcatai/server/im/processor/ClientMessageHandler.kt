package com.lovelycatv.ai.shadowcatai.server.im.processor

import com.alibaba.fastjson2.JSON
import com.lovelycatv.ai.shadowcatai.server.ShadowCatAiServerApplication
import com.lovelycatv.ai.shadowcatai.server.entity.MessageEntity
import com.lovelycatv.ai.shadowcatai.server.service.MessageService
import com.lovelycatv.ai.shadowcatai.server.service.SessionService
import com.lovelycatv.ai.shadowcatai.share.im.message.MessageChain
import com.lovelycatv.ai.shadowcatai.share.im.message.TextMessage
import com.lovelycatv.ai.shadowcatai.share.im.message.func.CallBackMessage
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import org.springframework.ai.chat.messages.AssistantMessage
import org.springframework.ai.chat.messages.Message
import org.springframework.ai.chat.messages.UserMessage
import org.springframework.ai.chat.prompt.ChatOptionsBuilder
import org.springframework.ai.chat.prompt.Prompt
import org.springframework.ai.ollama.OllamaChatModel
import org.springframework.ai.ollama.api.OllamaApi
import org.springframework.ai.ollama.api.OllamaOptions
import java.util.*

/**
 * @author lovelycat
 * @since 2024-08-09 15:18
 * @version 1.0
 */
class ClientMessageHandler(
    val channelMapUserId: (channelId: String) -> Long,
    private val sessionService: SessionService,
    private val messageService: MessageService
) : SimpleChannelInboundHandler<MessageChain>() {
    override fun channelRead0(ctx: ChannelHandlerContext, msg: MessageChain) {
        val channelId = ctx.channel().id().asLongText()
        val userId = channelMapUserId(channelId)
        val session = sessionService.getDetailedBySessionId(msg.sessionId)

        println("From Client (%s : %s) ==> %s%n".format(session.id, session.modelId, JSON.toJSONString(msg)))

        if (ShadowCatAiServerApplication.isModelGenerating) {
            ctx.writeAndFlush(CallBackMessage.build(CallBackMessage.CODE_SESSION_BUSY, "An other request is being processing"))
        } else {
            ShadowCatAiServerApplication.isModelGenerating = true
            try {
                // Get history chat messages
                val messages = messageService.getMessagesBySessionId(msg.sessionId, userId, true, true)
                val prompts = mutableListOf<Message>()

                messages.forEach {
                    if (it.assistant) {
                        prompts.add(AssistantMessage(it.message))
                    } else {
                        prompts.add(UserMessage(it.message))
                    }
                }

                val inputMessage = arrayOf("")
                msg.messages.forEach {
                    if (it is TextMessage) {
                        inputMessage[0] = inputMessage[0] + it.message + "\n"
                    } else {
                        println("Unsupported message type: ${JSON.toJSONString(it)}")
                    }
                }
                prompts.add(UserMessage(inputMessage[0]))

                val messageIdInDatabase = messageService.addMessage(MessageEntity(
                    null,
                    userId,
                    session.id,
                    false,
                    0,
                    inputMessage[0],
                    System.currentTimeMillis(),
                    0L,
                    false
                ))
                ctx.writeAndFlush(CallBackMessage.build(CallBackMessage.CODE_SESSION_MESSAGE_RECEIVED, messageIdInDatabase.toString()))

                val prompt = Prompt(prompts, ChatOptionsBuilder.builder().build())
                val messageUUID = UUID.randomUUID().toString().replace("-", "")
                val ollamaChatModel = OllamaChatModel(
                    OllamaApi(),
                    OllamaOptions.create()
                        .withModel(session.modelEntity!!.qualifiedName)
                        .withTemperature(0.4f)
                )

                val streamMessage = arrayOf("")
                ollamaChatModel.stream(prompt).doOnComplete {
                    ShadowCatAiServerApplication.isModelGenerating = false

                    val assistantMessage = MessageEntity(
                        null,
                        userId,
                        session.id,
                        true,
                        0,
                        streamMessage[0],
                        System.currentTimeMillis(),
                        messageIdInDatabase,
                        false
                    )

                    messageService.addMessage(assistantMessage)

                    val assistantMessageIdInDatabase = assistantMessage.id

                    ctx.writeAndFlush(CallBackMessage.build(
                        CallBackMessage.CODE_SESSION_STREAM_MESSAGE_COMPLETED,
                        "$messageUUID:${assistantMessageIdInDatabase}"
                    ))
                }.subscribe{ chatResponse ->
                    val generated = chatResponse.result.output.content
                    streamMessage[0] += generated
                    val messageChain = MessageChain(TextMessage(generated))
                    messageChain.sessionId = session.id
                    messageChain.streamId = messageUUID
                    ctx.writeAndFlush(messageChain)
                }
            } catch (e: Exception) {
                ShadowCatAiServerApplication.isModelGenerating = false
                e.printStackTrace()
            }
        }
    }
}