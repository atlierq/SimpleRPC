package Netty.provider.Imlp;

import Netty.provider.ServiceProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ZkServiceProviderImpl implements ServiceProvider {
    private final Map<String,Object> serviceMap;
    public ZkServiceProviderImpl(){
        serviceMap = new ConcurrentHashMap<>();

    }
    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if(service==null){
            throw new RuntimeException("service can not be found");
        }
        return service;
    }
}
