package com.dada.shen.springboot.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.netty.HelloClientInitializer
 * @Project:
 * @author: dada.shen
 * @date: 2017-12-31 18:59
 * @Description:
 */
public class HelloClientInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        /*
         * 这个地方的 必须和服务端对应上。否则无法正常解码和编码
         *
         * 解码和编码 我将会在下一张为大家详细的讲解。再次暂时不做详细的描述
         *
         * */
        pipeline.addLast("framer", new DelimiterBasedFrameDecoder(16384, Delimiters.lineDelimiter()));
//        pipeline.addLast("decoder", new StringDecoder());
        pipeline.addLast("decoder", new MessageToMessageDecoder<ByteBuf>() {
            @Override
            protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf msg, List<Object> out) throws Exception {
                out.add(msg.toString(Charset.defaultCharset()));
            }
        });
        pipeline.addLast("encoder", new StringEncoder());


        // 客户端的逻辑
        pipeline.addLast("handler", new HelloClientHandler());
    }
}
