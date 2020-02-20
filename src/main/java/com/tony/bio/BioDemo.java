package com.tony.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created on 2020/2/20 11:42.
 *
 * @author Tony
 * @description:
 */
public class BioDemo {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true){
            System.out.println("当前线程id:"+Thread.currentThread().getId()+"线程名："+Thread.currentThread().getName());
            System.out.println("等待连接。。。。。。");
            final Socket socket = serverSocket.accept();
            System.out.println("获得一个链接");
            executorService.execute(new Runnable() {
                public void run() {
                    System.out.println("当前线程id:"+Thread.currentThread().getId()+"线程名："+Thread.currentThread().getName());
                    handler(socket);
                }
            });
        }
    }

    private static void handler(Socket socket) {
        try {
            System.out.println("当前线程id:"+Thread.currentThread().getId()+"线程名："+Thread.currentThread().getName());
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[1024];
            while(true){
                int read=inputStream.read(bytes);
                if(read!=-1){
                    String s = new String(bytes, 0, read);
                    System.out.println(s);
                }else {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
