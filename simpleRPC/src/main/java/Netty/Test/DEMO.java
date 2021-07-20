package Netty.Test;

import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import Netty.client.NettyClient;
import Netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class DEMO {



    public void serverStart(){
        new NettyServer(8080).start();
    }

    public void clientStart(){
        System.out.println("prepare RPCRequest");
        RPCRequest rpcRequest = RPCRequest.builder()
                .interfaceName("interface")
                .methodName("hello")
                .build();
        System.out.println(rpcRequest.toString());
        NettyClient nettyClient = new NettyClient();
        RPCResponse request = (RPCResponse) nettyClient.sendRPCRequest(rpcRequest);
        log.info(String.valueOf(request));
    }
}
