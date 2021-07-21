package Netty.provider.Imlp;

import Netty.config.RpcServiceConfig;
import Netty.provider.ServiceProvider;
import Netty.registry.ServiceRegistry;
import Netty.registry.zk.ServiceRegistryImpl;
import Netty.server.NettyServer;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ZkServiceProviderImpl implements ServiceProvider {
    private final Map<String,Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;
    public ZkServiceProviderImpl(){
        serviceMap = new ConcurrentHashMap<>();
        serviceRegistry = new ServiceRegistryImpl();
        registeredService  = ConcurrentHashMap.newKeySet();

    }

    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServerName = rpcServiceConfig.getRpcServiceName();
        if(registeredService.contains(rpcServerName)){
            return;
        }
        registeredService.add(rpcServerName);
        serviceMap.put(rpcServerName,rpcServiceConfig.getService());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if(service==null){
            throw new RuntimeException("service can not be found");
        }
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getServiceName(),new InetSocketAddress(host, NettyServer.port));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}
