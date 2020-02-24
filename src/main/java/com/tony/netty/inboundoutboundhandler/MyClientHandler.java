package com.tony.netty.inboundoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created on 2020/2/23 20:53.
 *
 * @author Tony
 * @description:
 */
public class MyClientHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("服务器ip:"+channelHandlerContext.channel().remoteAddress());
        System.out.println("服务端消息："+aLong);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("MyclientHandler被调用");
        ctx.writeAndFlush(1234567L);
    }
}
