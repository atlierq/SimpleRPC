package Netty.Test;

import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import Netty.client.NettyClient;
import Netty.server.NettyServer;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

@Slf4j
public class DEMO {


    @Test
    public void serverStart(){
        new NettyServer(8080).run();
    }
    @Test
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
