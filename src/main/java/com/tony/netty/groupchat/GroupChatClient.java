package com.tony.netty.groupchat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.util.Scanner;

public class GroupChatClient {
    private String host;
    private int port;

    public GroupChatClient() {
        host="127.0.0.1";
        port=8888;
    }
    private void run(){
        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder())
                            .addLast(new StringEncoder())
                            .addLast(new Clienthandler());
                        }
                    });
            try {
                ChannelFuture future = bootstrap.connect(host, port).sync();
                System.out.println("客户端准备好了");
                Scanner scanner = new Scanner(System.in);
                while (scanner.hasNextLine()){
                    String msg=scanner.nextLine();
                    future.channel().writeAndFlush(msg+"\r \n");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            eventExecutors.shutdownGracefully();
        }

    }

    public static void main(String[] args) {
        new GroupChatClient().run();
    }
}
