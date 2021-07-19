package registry.zk;

import Netty.Message.RPCRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import registry.ServiceDiscovery;
import registry.zk.util.CuratorUtils;
import java.util.*;
import java.net.InetSocketAddress;
@Slf4j
public class ServiceDiscoveryImpl implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookupService(RPCRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getRpcServiceName();
        CuratorFramework zkClient = CuratorUtils.getZkClient();
        List<String> serviceUrlList = CuratorUtils.getChildrenNodes(zkClient,rpcServiceName);
        if(serviceUrlList == null||serviceUrlList.size() ==0){
            throw new RuntimeException("service not found");

        }
        String targetServiceURL = serviceUrlList.get(0);
        log.info("successfully found the service address:[{}]",targetServiceURL);
        String[] tmp = targetServiceURL.split(":");
        String host = tmp[0];
        int port = Integer.parseInt(tmp[1]);
        return new InetSocketAddress(host,port);

    }
}
