package com.tony.netty.dubborpc.netty;

import com.tony.netty.dubborpc.consumer.ClientBootstrap;
import com.tony.netty.dubborpc.provider.HelloServiceImpl;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Created on 2020/2/26 8:59.
 *
 * @author Tony
 * @description:
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    private int a;
    public NettyServerHandler() {
        a=0;
        System.out.println("NettyServerhandler");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("mag:"+msg);
        if(msg.toString().startsWith(ClientBootstrap.providerName)){
            String message = new HelloServiceImpl().hello(msg.toString().substring(msg.toString().lastIndexOf("#") + 1));
            ctx.writeAndFlush(message);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
