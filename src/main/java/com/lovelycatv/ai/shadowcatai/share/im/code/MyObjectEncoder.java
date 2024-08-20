package com.lovelycatv.ai.shadowcatai.share.im.code;

import com.alibaba.fastjson2.JSONObject;
import com.lovelycatv.ai.shadowcatai.share.im.message.MessageChain;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;

public class MyObjectEncoder extends MessageToByteEncoder<MessageChain> {
    @Override
    protected void encode(ChannelHandlerContext ctx, MessageChain msg, ByteBuf out) {
        String jsonString = JSONObject.toJSONString(msg);
        byte[] jsonBytes = jsonString.getBytes(Charset.defaultCharset());

        out.writeInt(jsonBytes.length);
        out.writeBytes(jsonBytes);
    }
}
 
