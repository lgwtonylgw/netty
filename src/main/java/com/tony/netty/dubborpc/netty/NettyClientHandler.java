package com.tony.netty.dubborpc.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.Callable;

/**
 * Created on 2020/2/26 9:07.
 *
 * @author Tony
 * @description:
 */
public class NettyClientHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String pram;
    @Override
    //与服务端连接后调用这个方法
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("jdsjfhiosdf");
        this.context=ctx;  //因为在其它方法会使用到ctx；

    }

    //收到服务端数据就会调用此方法
    @Override
    public synchronized void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result=msg.toString();
        notify();//唤醒等待线程
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
    //被代理对象调用，发送数据给服务端，等待被唤醒，返回结果
    @Override
    public synchronized Object call() throws Exception {
        System.out.println("para="+pram);
        context.writeAndFlush(pram);
        wait();//等待channelRead获取结果后唤醒
        return result;//服务方返回的结果
    }
    void setPram(String arg){
        this.pram=arg;
    }
}
