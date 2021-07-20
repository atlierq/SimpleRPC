package Netty.proxy;

import Netty.Message.RPCRequest;
import Netty.Message.RPCResponse;
import Netty.client.NettyClient;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class RpcClientProxy implements InvocationHandler {
    private final NettyClient nettyClient;

    public RpcClientProxy(NettyClient nettyClient){
        this.nettyClient = nettyClient;
    }
    public <T>T getProxy(Class<T> clazz){
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class<?>[]{clazz},this);
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("invoked method:[{}]",method.getName());
        RPCRequest rpcRequest = RPCRequest.builder()
                .methodName(method.getName())
                .interfaceName(method.getDeclaringClass().getName())
                .paramTypes(method.getParameterTypes())
                .requestID(UUID.randomUUID().toString())
                .build();

        RPCResponse<Object> rpcResponse = null;
        CompletableFuture<RPCResponse<Object>> completableFuture=(CompletableFuture<RPCResponse<Object>>)nettyClient.sendRPCRequest(rpcRequest);
        rpcResponse = completableFuture.get();
        return rpcResponse.getData();
    }
}
