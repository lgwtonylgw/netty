package com.tony.netty.dubborpc.provider;

import com.tony.netty.dubborpc.netty.NettyServer;

/**
 * Created on 2020/2/26 8:49.
 *
 * @author Tony
 * @description:
 */
public class ServerBootstrp {
    public static void main(String[] args) {
        NettyServer.startServer("127.0.0.1",8889);
    }
}
