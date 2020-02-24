package com.tony.netty.inboundoutboundhandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Created on 2020/2/23 20:41.
 *
 * @author Tony
 * @description:
 */
public class MyClient {
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventExecutors)
                    .channel(NioSocketChannel.class)//设置客户端通道的类（反射）
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline()
                                    .addLast(new MyLongToByteEncoderHandler())
                                    .addLast(new MyByteToLonghandler())
                                    .addLast(new MyClientHandler());
                        }
                    });
            System.out.println("客户端准备好了");
            //ChannelFuture涉及到netty的异步模型
            ChannelFuture future = bootstrap.connect("127.0.0.1", 8888).sync();
            //给关闭通道进行监听
        }finally {
            eventExecutors.shutdownGracefully();
        }
    }
}
