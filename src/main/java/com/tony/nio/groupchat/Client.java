package com.tony.nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Created on 2020/2/21 15:14.
 *
 * @author Tony
 * @description:
 */
public class Client {
    private final  String HOST="127.0.0.1";
    private final   int PORT=6667;
    private SocketChannel socketChannel;
    private Selector selector;
    private String username;
    public Client() throws IOException {
        selector=Selector.open();
        socketChannel = SocketChannel.open(new InetSocketAddress(HOST,PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        username=socketChannel.getLocalAddress().toString().substring(1);
        System.out.println(username+"is OK...");
    }
    public void sendInfo(String info){
        info=username+"说："+info;
        try {
            socketChannel.write(ByteBuffer.wrap(info.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void readInfo(){
        try {
            int i = selector.select();
            if(i>0){
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while ((iterator.hasNext())){
                    SelectionKey key = iterator.next();
                    if(key.isReadable()){
                        SocketChannel channel =(SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        channel.read(buffer);
                        String msg = new String(buffer.array());
                        System.out.println(msg.trim());
                    }
                }
                iterator.remove();
            }else{
//                System.out.println("没有可用的通道");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws Exception{
        Client client = new Client();
        new Thread(){
            public void run(){
                while(true){
                    client.readInfo();
                    try {
                        Thread.currentThread().sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
        Scanner scanner = new Scanner(System.in);
        while(scanner.hasNextLine()){
            String s=scanner.nextLine();
            client.sendInfo(s);
        }
    }
}
