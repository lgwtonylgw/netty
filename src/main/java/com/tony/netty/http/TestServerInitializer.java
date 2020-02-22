package com.tony.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * Created on 2020/2/21 23:48.
 *
 * @author Tony
 * @description:
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        //netty提供的编码解码器
        socketChannel.pipeline().addLast("MyHttpServerCodec",new HttpServerCodec())
                .addLast("Myhandler",new TestHttpServerHandler());
    }
}
