package com.tony.netty.simple2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * Created on 2020/2/21 21:45.
 *
 * @author Tony
 * @description:
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    //当通道就绪就会触发此方法
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client+"+ctx);
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,server=======", CharsetUtil.UTF_8));
    }
//当通道有读取事件是会触发
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf msg1 = (ByteBuf) msg;
        System.out.println("服务器发来的消息："+msg1.toString(CharsetUtil.UTF_8));
        System.out.println("服务器地址:"+ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
