package com.tony.nio.groupchat;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class Server {
    private Selector selector;
    private ServerSocketChannel serverSocketChannel;
    private static final int PORT=6667;

    public Server() {
        try {
            selector=Selector.open();
            serverSocketChannel=ServerSocketChannel.open();
            serverSocketChannel.socket().bind(new InetSocketAddress(PORT));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void listen(){
        while(true){
            try {
                int count = selector.select(2000);
                if(count>0){
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while(iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        if(key.isAcceptable()){
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            socketChannel.configureBlocking(false);
                            socketChannel.register(selector,SelectionKey.OP_READ);
                            System.out.println(socketChannel.getRemoteAddress()+"上线");
                        }
                        if(key.isReadable()){
                            readData(key);
                        }
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待中");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void readData(SelectionKey key){
        SocketChannel socketChannel=null;
        try {
            socketChannel= (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int count=socketChannel.read(buffer);
            if(count>0){
                String mag = new String(buffer.array());
                System.out.println("from 客户端: "+ mag);
                sendInfotoOtherClient(mag,socketChannel);
            }
        } catch (Exception e) {
            try {
                System.out.println(socketChannel.getRemoteAddress()+"离线了。。。");
                key.cancel();
                socketChannel.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }
    private void sendInfotoOtherClient(String msg,SocketChannel socketChannel){
        System.out.println("服务器转发消息中。。。。");
        for(SelectionKey key:selector.keys()){
            Channel channel = key.channel();
            if(channel instanceof  SocketChannel && channel!=socketChannel){
                SocketChannel dest = (SocketChannel) channel;
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                try {
                    dest.write(wrap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.listen();
    }
}
