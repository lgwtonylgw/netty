package com.tony.netty.protocoltcp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.nio.charset.Charset;

/**
 * Created on 2020/2/23 20:53.
 *
 * @author Tony
 * @description:
 */
public class MyClientHandler extends SimpleChannelInboundHandler<MessageProtocol> {
    private int count;
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        for (int i = 0; i < 10; i++) {
            String msg="今天天气冷，吃火锅";
            byte[] content = msg.getBytes(Charset.forName("utf-8"));
            int len=msg.getBytes(Charset.forName("utf-8")).length;
            MessageProtocol messageProtocol = new MessageProtocol();
            messageProtocol.setLen(len);
            messageProtocol.setContent(content);
            ctx.writeAndFlush(messageProtocol);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol) throws Exception {
        int len=messageProtocol.getLen();
        byte[] content = messageProtocol.getContent();
        System.out.println("接收到长度="+len);
        System.out.println("接收到内容："+new String(content,Charset.forName("utf-8")));
        System.out.println("接收到的次数："+(++this.count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常消息："+cause.getMessage());
        ctx.close();
    }
}
