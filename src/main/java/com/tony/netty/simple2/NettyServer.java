package com.tony.netty.simple2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * Created on 2020/2/21 20:15.
 *
 * @author Tony
 * @description:
 */
public class NettyServer
{
    public static void main(String[] args) throws InterruptedException {
        //创建两个线程组
        //两个都是无限循环
        //boosgroup只处理连接请求，workergroup处理客户端业务处理
        //bossGroup和workerGroup还有的子线程个数默认和cpu核数*2
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        //除了再handler中加入业务线程池，也可以在context中加入
        EventExecutorGroup group = new DefaultEventExecutorGroup(2);

        ServerBootstrap bootstrap;
        try {
            bootstrap  = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup)//设置两个线程组
                    .channel(NioServerSocketChannel.class)//是用NioSocketChannel作为服务器通通道实现（反射）
                    .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到的连接个数
                    .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new NettyServerHandler());
                            //在context中加入线程池，则handler优先使用这个线程池，如果不添加，则使用IO线程池
//                            .addLast(group,new NettyServerHandler());
                        }
                    });//给workergroup的Eventloop设置处理器
            System.out.println("======服务器准备好了=========");
            //绑定一个端口并且同步，生成一个ChannelFuture对象
            //、启动服务器
            ChannelFuture future = bootstrap.bind(6668).sync();
            //给ChannelFuture注册监听器
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if(future.isSuccess()){
                        System.out.println("监听端口成功");
                    }else{
                        System.out.println("监听端口失败");
                    }
                }
            });
            //对关闭端口进行监听
            future.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }
}
