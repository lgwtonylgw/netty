package com.tony.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created on 2020/2/20 23:20.
 *
 * @author Tony
 * @description:
 * Channel注册到Selector，Selector能够检测到Channel是否有事件发生。如果有事件发生，则进行相应的处理。这样可以实现一个线程管理多个Channel（即多个连接和请求）
 * 只有通道真正有读写事件发生时，才会进行读写。减少了创建的线程数，降低了系统开销
 * 减少了上下文的切换，用户态和系统态的切换
 * 以ServerSocketChannel为例说明：
 *
 * 当有客户端连接时，ServerSocketChannel会返回一个SocketChannel
 * SocketChannel注册到Selector。（register方法）
 * register方法会返回一个SelectionKey，SelectionKey与Channel关联
 * Selector监听select方法，返回有事件的个数
 * 进一步得到SelectionKey
 * 通过SelectionKey获取SocketChannel（SelectionKey中的channel方法）
 * 通过获取的channel，执行业务处理
 */
public class NIOServer {
    public static void main(String[] args) throws IOException {
        // 创建ServerSocketChannel
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 创建Selector
        Selector selector = Selector.open();
        // 绑定ip
        serverSocketChannel.socket().bind(new InetSocketAddress(9000));
        // 设置为不阻塞
        serverSocketChannel.configureBlocking(false);
        // 将ServerSocketChannel注册到selector。指定关心的事件为OP_ACCEPT，
        // 当有关心的事件发生时，会返回这个SelectionKey，通过SelectionKey可以拿到Channel
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

        while (true) {
            // Selector监听，等于0说明此时没有事件发生。
            if (selector.select(1000) == 0) {
                System.out.println("Selector监听了一秒");
                continue;
            }
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isAcceptable()) {
                    // 获得SocketChannel，此处的accept不会阻塞
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    // 此处socketChannel也要设置为非阻塞模式
                    socketChannel.configureBlocking(false);
                    // 注册Selector。第三个参数是连接的对象，通过SelectionKey可以连接到这个对象
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(1024));
                }
                if (key.isReadable()) {
                    SocketChannel channel = (SocketChannel) key.channel();
                    ByteBuffer buffer = (ByteBuffer) key.attachment();
                    int read = channel.read(buffer);
                    System.out.println("客户端 : " + new String(buffer.array(), 0, read));
                }
                // 手动删除避免重复
                keyIterator.remove();
            }
        }
    }
}
