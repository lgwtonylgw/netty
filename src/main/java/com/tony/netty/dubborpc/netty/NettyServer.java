package com.tony.netty.dubborpc.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Created on 2020/2/26 8:50.
 *
 * @author Tony
 * @description:
 */
public class NettyServer {
    public static void startServer(String hostName,int port){
        startServer0(hostName,port);
    }
    private static void startServer0(String hostName,int port){
        NioEventLoopGroup booGroup = new NioEventLoopGroup(1);
        NioEventLoopGroup workerGroup= new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrp = new ServerBootstrap();
            serverBootstrp.group(booGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(new NettyServerHandler());
                        }
                    });
            ChannelFuture future = serverBootstrp.bind(hostName,port).sync();
            System.out.println("服务器端开始提供服务！");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            booGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
