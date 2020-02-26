package com.tony.netty.dubborpc.provider;

import com.tony.netty.dubborpc.publicinterface.HelloService;

/**
 * Created on 2020/2/26 8:46.
 *
 * @author Tony
 * @description:服务方
 */
public class HelloServiceImpl implements HelloService
{
    private int count=0;
    @Override
    public String hello(String msg) {
        System.out.println("收到客户短消息："+msg);
        if(msg!=null){
            return "你好客户端，已收到消息 ["+msg+"]第"+(++count)+"次";
        }else {
            return "你好客户端，已经收到消息";
        }
    }
}
