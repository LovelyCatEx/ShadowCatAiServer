package com.lovelycatv.ai.shadowcatai.share.im.code;

import com.lovelycatv.ai.shadowcatai.share.im.message.MessageChain;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

public class MyObjectDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        byte[] bytes = new byte[in.readableBytes()];
        in.readBytes(bytes);
        String jsonString = new String(bytes, Charset.defaultCharset());
        System.out.println("Netty: " + jsonString);

        out.add(MessageChain.decodeJSON(jsonString));
    }
}