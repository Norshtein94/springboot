package com.dada.shen.springboot.netty;

import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.handler.codec.string.StringDecoder;

import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.netty.HelloServerInitializer
 * @Project:
 * @author: dada.shen
 * @date: 2017-12-31 18:55
 * @Description:
 */
public class HelloServerInitializer extends ChannelInitializer<SocketChannel>{
    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
         // 以("\n")为结尾分割的 解码器
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        // 字符串解码 和 编码
        pipeline.addLast("decoder", new StringDecoder());
//        pipeline.addLast("encoder", new StringEncoder());
        pipeline.addLast("encoder", new MessageToMessageEncoder<CharSequence>() {
            @Override
            protected void encode(ChannelHandlerContext ctx, CharSequence msg, List<Object> out) throws Exception {
                if(msg.length() != 0) {
                    out.add(ByteBufUtil.encodeString(ctx.alloc(), CharBuffer.wrap(msg), Charset.defaultCharset()));
                }
            }
        });
        // 自己的逻辑Handler
        pipeline.addLast("handler", new HelloServerHandler());
    }
}
