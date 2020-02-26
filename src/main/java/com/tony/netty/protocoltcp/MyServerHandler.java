package com.tony.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Created on 2020/2/23 20:31.
 *
 * @author Tony
 * @description:
 */
public class MyServerHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        int len = messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();
        System.out.println("长度="+len);
        System.out.println("内容是："+new String(content, Charset.forName("utf-8")));
        System.out.println("服务器接收到消息包数量="+(++this.count));
        //回复客户端信息
        byte[] responseConteent = UUID.randomUUID().toString().getBytes("utf-8");
        int length=responseConteent.length;
        MessageProtocol messageProtocol1 = new MessageProtocol();
        messageProtocol.setLen(length);
        messageProtocol.setContent(responseConteent);
        channelHandlerContext.writeAndFlush(messageProtocol);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
