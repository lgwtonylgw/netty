package com.tony.netty.protobuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * Created on 2020/2/21 20:33.
 *
 * @author Tony
 * @description:
 * 必须继承
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    /**
     * 读取数据
     * ctx 上下文对象，含有pipeline  通道channel 地址
     * Object msg 客户端发送过来的数据
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("server ctx = "+ctx);
        //将msg转成一个ByteBuf
        //ByteBuf是Netty提供的，不是NIO的ByteBuffer
        ByteBuf buf=(ByteBuf)msg;
        System.out.println("客户端发的送的消息是："+buf.toString(CharsetUtil.UTF_8));
        //用户自定义的普通任务   放到tashQueue
        ctx.channel().eventLoop().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常+"+e.getMessage());
                }
            }
        });
        //用户自定义定时任务  放到scheduleTaskQueue
        ctx.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(10);
                    ctx.writeAndFlush(Unpooled.copiedBuffer("hello，客户端",CharsetUtil.UTF_8));
                } catch (InterruptedException e) {
                    System.out.println("发生异常+"+e.getMessage());
                }
            }
        },5,TimeUnit.SECONDS);
        System.out.println("go on.....");
        System.out.println("客户端地址："+ctx.channel().remoteAddress());
    }

    //数据读取完毕
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将数据读书到缓存，并刷新
        ctx.writeAndFlush(Unpooled.copiedBuffer("hello,客户端",CharsetUtil.UTF_8));

    }
    //处理异常

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
