package com.dada.shen.springboot.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.netty.HelloServerHandler
 * @Project:
 * @author: dada.shen
 * @date: 2017-12-31 18:58
 * @Description:
 */
public class HelloServerHandler extends SimpleChannelInboundHandler<String> {

    public static final Map<String, Object> clientChannels = new HashMap<>();
    public static StringBuffer cacheMsg = new StringBuffer();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 收到消息直接打印输出
        cacheMsg.append(ctx.channel().remoteAddress()).append(" Say : ").append(msg).append("\n");
        System.out.println(ctx.channel().remoteAddress() + " Say : " + msg);
        for (Object clientChannel : clientChannels.values()) {
            ((SocketChannel)clientChannel).writeAndFlush(ctx.channel().remoteAddress() + " Say : " + msg + "\n");
        }
    }

    /*
     *
     * 覆盖 channelActive 方法 在channel被启用的时候触发 (在建立连接的时候)
     *
     * channelActive 和 channelInActive 在后面的内容中讲述，这里先不做详细的描述
     * */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        clientChannels.put(ctx.channel().remoteAddress().toString(), ctx.channel());
        System.out.println("RemoteAddress : " + ctx.channel().remoteAddress() + " active !");

        ctx.writeAndFlush( "Welcome to " + InetAddress.getLocalHost().getHostName() + " service!\n" + cacheMsg);


        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        clientChannels.remove(ctx.channel().remoteAddress().toString());
        System.out.println("RemoteAddress : " + ctx.channel().remoteAddress() + " inactive !");
        super.channelInactive(ctx);
    }
}
