package Netty.Test;

import Netty.client.NettyClient;
import Netty.proxy.RpcClientProxy;
import Netty.server.NettyServer;

public class RpcClient {
    public static void main(String[] args) {
        NettyClient nettyClient = new NettyClient();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyClient);


    }
}
