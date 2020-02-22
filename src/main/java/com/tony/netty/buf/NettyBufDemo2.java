package com.tony.netty.buf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

public class NettyBufDemo2 {
    public static void main(String[] args) {
        ByteBuf buf = Unpooled.copiedBuffer("hello,world!", Charset.forName("utf-8"));
        if(buf.hasArray()){
            byte[] array = buf.array();
            System.out.println(new String(array, Charset.forName("utf-8")));
        }
    }
}
