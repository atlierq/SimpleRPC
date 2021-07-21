package Netty.server.handler;

import Netty.Message.RPCRequest;
import Netty.Tools.Factory.SingletonFactory;
import Netty.provider.Imlp.ZkServiceProviderImpl;
import Netty.provider.ServiceProvider;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
@Slf4j
public class RpcRequestHandler {
    private final ServiceProvider serviceProvider;
    public RpcRequestHandler(){
        serviceProvider = SingletonFactory.getInstance(ZkServiceProviderImpl.class);
    }
    public Object handle(RPCRequest rpcRequest){
        Object service = serviceProvider.getService(rpcRequest.getRpcServiceName());
        return invokeTargetMethod(rpcRequest,service);
    }
    public Object invokeTargetMethod(RPCRequest rpcRequest,Object service){
        Object res = null;
        try {
            Method method = service.getClass().getMethod(rpcRequest.getMethodName(),rpcRequest.getParamTypes());
            System.out.println(method);
            res = method.invoke(service,rpcRequest.getParameters());
            log.info("service:[{}]successful invoke method:[{}]",rpcRequest.getInterfaceName(),rpcRequest.getMethodName());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return res;


    }
}
