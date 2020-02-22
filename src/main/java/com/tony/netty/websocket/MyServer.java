package com.tony.netty.websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class MyServer  {
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
                            socketChannel.pipeline().addLast(new HttpServerCodec())
                                    .addLast(new ChunkedWriteHandler())
                                    //http传输过程是分段的，Aggregator可以聚合这些分段
                                    .addLast(new HttpObjectAggregator(8192))
                                    //对于websocket它是以数据帧的形式传递的,可以看到他有6个子类，浏览器请求时 ws://localhost:8888/xxx
                                    //websockerServerProtocolhandler核心功能是将http协议升级为ws协议，保持长连接
                                    .addLast(new WebSocketServerProtocolHandler("/hello"))
                                    .addLast(new WenSockerHandler());

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
        new com.tony.netty.websocket.MyServer().run();
    }
}
