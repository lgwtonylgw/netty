package com.tony.netty.inboundoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * Created on 2020/2/23 20:27.
 *
 * @author Tony
 * @description:
 */
public class MyByteToLonghandler extends ByteToMessageDecoder {
    /**
     *  decode会多次执行，知道没有新的元素添加到list中或者bytebuf没有更多数据
     *  如果list不为空，就会传给下一个handler，，下一个handler的方法也会被调用多次
     * @param channelHandlerContext 上下文
     * @param byteBuf   输入流
     * @param list   存储解码对象
     * @throws Exception
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        System.out.println("MyByteToLongHandler被调用");
        if(byteBuf.readableBytes()>8){
            list.add(byteBuf.readLong());
        }
    }
}
