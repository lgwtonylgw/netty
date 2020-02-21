package com.tony.netty.simple;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

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

    }
}
