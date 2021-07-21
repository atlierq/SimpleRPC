package github.L;

import Netty.config.RpcServiceConfig;
import Netty.server.NettyServer;
import github.L.serviceImpl.HelloServiceImpl1;

public class ServiceSimple {
    public static void main(String[] args) {
        HelloServiceImpl1 helloService = new HelloServiceImpl1();
        NettyServer nettyServer = new NettyServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(helloService);


    }
}
