package com.tony.netty.protocoltcp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * Created on 2020/2/23 20:48.
 *
 * @author Tony
 * @description:
 */
public class MyMessageEncoderHandler extends MessageToByteEncoder<MessageProtocol> {



    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, MessageProtocol messageProtocol, ByteBuf byteBuf) throws Exception {
        System.out.println("MyMessageEncoderHandler被调用");
        byteBuf.writeInt(messageProtocol.getLen());
        byteBuf.writeBytes(messageProtocol.getContent());
    }
}
