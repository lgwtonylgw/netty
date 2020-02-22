package com.tony.netty.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class MyServer {
    private int port=8888;
    private void run(){
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //readeridleTime:  多长时间没有读就会发送一个心跳检测包检测连接
                            //readeridleTime:  多长时间没有写就会发送一个心跳检测包检测连接
                            //readeridleTime:  多长时间没有读写就会发送一个心跳检测包检测连接
                            socketChannel.pipeline()
                                    .addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS))
                            //加一个对空闲handler的处理handler
                            .addLast(new MyServerhandler());

                        }
                    });
            try {
                ChannelFuture future = serverBootstrap.bind(port).sync();
                future.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) {
        new MyServer().run();
    }
}
