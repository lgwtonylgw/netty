package com.tony.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import javax.sound.midi.Soundbank;

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
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup,workerGroup)//设置两个线程组
                .channel(NioServerSocketChannel.class)//是用NioSocketChannel作为服务器通通道实现
                .option(ChannelOption.SO_BACKLOG,128)//设置线程队列得到的连接个数
                .childOption(ChannelOption.SO_KEEPALIVE,true)//设置保持活动连接状态
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(null);
                    }
                });//给workergroup的Eventloop设置处理器
        System.out.println("======服务器准备好了=========");
        //绑定一个端口并且同步，生成一个ChannelFuture对象
        //、启动服务器
        ChannelFuture future = bootstrap.bind(6668).sync();
        //对关闭端口进行监听
        future.channel().closeFuture().sync();

    }
}
