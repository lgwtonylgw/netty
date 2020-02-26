package com.tony.netty.dubborpc.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.Proxy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2020/2/26 9:29.
 *
 * @author Tony
 * @description:
 */
public class NettyClient {
    private static ExecutorService service= Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static NettyClientHandler handler;
    //编写方法使用代理模式，获取一个代理对象
    public Object getBean(final  Class<?> serviceClass,final String providerName){
        return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] {serviceClass},(proxy,method,args) -> {
            //客户端每调用一次hello，就会进入这个方法
            if(handler==null){
                initClient();
            }
            //设置发送给服务端的消息
                    //providerName协议头arg[0] 就是客户端调用api hello(???) 参数
            handler.setPram(providerName+args[0]);
            return service.submit(handler).get();
                });
    }
    private static void initClient(){
        handler=new NettyClientHandler();
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new StringDecoder())
                                    .addLast(new StringEncoder())
                                    .addLast(handler);
                        }
                    });
            try {
                bootstrap.connect("127.0.0.1",8889).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } finally {
            eventLoopGroup.shutdownGracefully();

        }
    }
}
