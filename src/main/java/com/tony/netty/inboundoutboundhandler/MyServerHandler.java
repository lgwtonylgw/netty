package com.tony.netty.inboundoutboundhandler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Created on 2020/2/23 20:31.
 *
 * @author Tony
 * @description:
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Long> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Long aLong) throws Exception {
        System.out.println("从客户端"+channelHandlerContext.channel().remoteAddress()+"读取到long"+aLong);
        //给客户端回复yige long
        channelHandlerContext.writeAndFlush(978675453L);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
