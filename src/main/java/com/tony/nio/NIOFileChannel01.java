package com.tony.nio;


import java.io.*;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created on 2020/2/20 15:36.
 *
 * @author Tony
 * @description:
 * Channel可以同时读写，可以异步读写数据。
 *
 * FileChannel：文件读写
 * FileOutPutStream和FileInputStream中包含了FileChannel属性，可以通过这两个类的实例获得Channel。
 * 实例：
 * 		FileInputStream inputStream = new FileInputStream("D://hello.txt");
 *         // FileInputStream获取FileChannel，实际类型是FileChannelImpl
 *         FileChannel channel = inputStream.getChannel();
 *         ByteBuffer buffer = ByteBuffer.allocate(1024);
 *         int read = channel.read(buffer);
 *         System.out.println(read);
 *         String s = new String(buffer.array(), 0, read,"utf-8");
 *         System.out.println(s);
 *         inputStream.close();
 *         channel.close();
 */
public class NIOFileChannel01 {
    public static void write(String[] args) throws IOException {
        FileOutputStream fileOutputStream=new FileOutputStream("D:\\file01.txt");
        // 从FileOutPutStream获取FileChannelImpl
        FileChannel fileChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String str="hello,刘国文";
        //将byte数组放入缓冲区，buffer的position等于数组长度
        byteBuffer.put(str.getBytes());
        // 读写翻转，limit=position，而position置0
        byteBuffer.flip();
        fileChannel.write(byteBuffer);
        fileOutputStream.close();
        fileChannel.close();
    }

    public static void read(String[] args) throws IOException {
        File file = new File("D:\\file01.txt");
        FileInputStream fileInputStream = new FileInputStream(file);
        // FileInputStream获取FileChannel，实际类型是FileChannelImpl
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());
        int read = channel.read(byteBuffer);
        System.out.println(new String(byteBuffer.array()));
        fileInputStream.close();
        channel.close();
    }

    public static void readwrite(String[] args) throws IOException {
        FileInputStream fileInputStream = new FileInputStream("file01.txt");
        FileOutputStream fileOutputStream = new FileOutputStream("fil02e.txt");
        FileChannel fileInputStreamChannel = fileInputStream.getChannel();
        FileChannel fileOutputStreamChannel = fileOutputStream.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        while(true){
            // clear不重置的话，position=limit，则read一直等于0
            // 标志位重置，position重置为0，limit设为capacity
            byteBuffer.clear();
            int read = fileInputStreamChannel.read(byteBuffer);
            if(read==-1){
                break;
            }
            byteBuffer.flip();
            fileOutputStreamChannel.write(byteBuffer);
        }
        fileInputStream.close();
        fileOutputStream.close();
    }

    public static void main(String[] args) throws IOException {
        // 使用transfrom拷贝文件
        FileInputStream inputStream = new FileInputStream("D://a.jpg");
        FileOutputStream outputStream = new FileOutputStream("D://a2.jpg");
        FileChannel inChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();
        // transferFrom拷贝
        outputChannel.transferFrom(inChannel,0,inChannel.size());
        inputStream.close();
        outputStream.close();
    }
    /**
     * ByteBuffer，put什么类型，取得时候就要相应的类型去get。
     *
     * Buffer可以设置为只读
     * // 只读Buffer，不可写，否则会报ReadOnlyBufferException
     * ByteBuffer readOnlyBuffer = buffer.asReadOnlyBuffer();
     *
     * MappedByteBuffer可以直接在内存（堆外内存）中修改，操作系统不需要拷贝一次
     * 		// 直接在内存中修改，不用操作系统再拷贝一次
     *         RandomAccessFile accessFile = new RandomAccessFile("D://a.txt", "rw");
     *         FileChannel channel = accessFile.getChannel();
     *
     *          * 参数说明；
     *          * 1.FileChannel.MapMode.READ_WRITE 使用读写模式
     *          * 2.直接修改的起始位置
     *          * 3.从起始位置映射到内存的大小（不是索引），超过字节大小将不能修改
     *
     *          MappedByteBuffer mappedByteBuffer = channel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
     *          mappedByteBuffer.put(0,(byte)'a');
     *          mappedByteBuffer.put(3,(byte)'9');
     *          accessFile.close();
     */
}
