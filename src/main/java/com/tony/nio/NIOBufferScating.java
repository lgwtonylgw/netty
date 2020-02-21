package com.tony.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * Created on 2020/2/20 22:02.
 *
 * @author Tony
 * @description:
 *  Scatting：将数据写入到buffer时，可以使用buffer数组，依次写入
 *          * Gathering：从buffer读取数据时，可以采用buffer数组，依次读取
 */
public class NIOBufferScating {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        InetSocketAddress inetSocketAddress = new InetSocketAddress(8888);
        serverSocketChannel.socket().bind(inetSocketAddress);
        ByteBuffer[] buffers = new ByteBuffer[2];
        buffers[0]=ByteBuffer.allocate(5);
        buffers[1]=ByteBuffer.allocate(3);
        int messageLength = 8;
        // 等待连接
        SocketChannel socketChannel = serverSocketChannel.accept();
        // 循环读取
        while (true) {
            int byteRead = 0;
            while (byteRead < messageLength) {
                long l = socketChannel.read(buffers);
                byteRead += l;
                System.out.println("byteRead = " + byteRead);
                Arrays.asList(buffers).stream().map(buffer -> "position = " + buffer.position() + ",limit = " + buffer.limit()).forEach(System.out::println);
            }

            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.flip());
            int byteWrite = 0;
            while (byteWrite < messageLength) {
                long l = socketChannel.write(buffers);
                byteWrite += l;
            }
            Arrays.asList(buffers).forEach(byteBuffer -> byteBuffer.clear());
            System.out.println("byteRead = " + byteRead + ",byteWrite = " + byteWrite + ",messageLength = " + messageLength);
        }
    }
}
