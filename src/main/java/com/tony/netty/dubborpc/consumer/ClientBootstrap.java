package com.tony.netty.dubborpc.consumer;

import com.tony.netty.dubborpc.netty.NettyClient;
import com.tony.netty.dubborpc.publicinterface.HelloService;

/**
 * Created on 2020/2/26 9:56.
 *
 * @author Tony
 * @description:
 */
public class ClientBootstrap {
    //这里定义协议头
    public static final String providerName="HelloService#hello#";
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        HelloService helloService= (HelloService)nettyClient.getBean(HelloService.class, providerName);
        //通过代理对象调用服务提供者的方法
        for(;;){
            try {
                String resp = helloService.hello("你好,dubbo");
                Thread.sleep(10000);
                System.out.println("调用的结果res——="+resp);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
