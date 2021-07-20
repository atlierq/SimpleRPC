package Netty.registry.zk;

import org.apache.curator.framework.CuratorFramework;
import Netty.registry.ServiceRegistry;
import Netty.registry.zk.util.CuratorUtils;

import java.net.InetSocketAddress;

public class ServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        String servicePath= CuratorUtils.ZK_REGISTER_ROOT_PATH+"/"+rpcServiceName+inetSocketAddress.toString();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        CuratorUtils.createPersistentNode(zkClient,servicePath);
        

    }
}
