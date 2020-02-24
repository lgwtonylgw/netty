package com.tony.netty.inboundoutboundhandler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created on 2020/2/23 20:48.
 *
 * @author Tony
 * @description:
 */
public class MyLongToByteEncoderHandler extends MessageToByteEncoder<Long> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Long aLong, ByteBuf byteBuf) throws Exception {
        System.out.println("MyLongToBytehandler被调用");
        System.out.println("msg="+ aLong);
        byteBuf.writeLong(aLong);

    }
}
