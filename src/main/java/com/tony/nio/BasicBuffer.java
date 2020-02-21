package com.tony.nio;

import java.nio.IntBuffer;

/**
 * Created on 2020/2/20 14:46.
 *
 * @author Tony
 * @description:
 * 一个Channel对应一个Buffer，一个Selector管理多个Channel，一个线程对应一个Selector
 * 程序切换到哪个Channel由事件决定，Event
 * Buffer就是一个内存块，底层是数组。Client通过Buffer进行数据的读写，NIO中的Buffer是双向的，BIO中的输入流、输出流不是双向的。
 * Channel也是双向的
 */
public class BasicBuffer {
    /**
     * mark ： 标记
     * position ： 位置，下一次要读写的元素的位置。
     * limit ： 缓冲区的终点，不能超过缓冲区的最大位置，可以修改
     * capacity ：容量，缓冲区创建时指定
     */
    public static void main(String[] args) {
        IntBuffer intBuffer=IntBuffer.allocate(5);
        for (int i = 0; i < intBuffer.capacity(); i++) {
            intBuffer.put(i*2);
        }
        //必须反转，才能读取
        intBuffer.flip();
        while(intBuffer.hasRemaining()){
            System.out.println(intBuffer.get());
        }
    }
}
