package Netty.registry.zk.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static CuratorFramework zkClient;
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    public static final String ZK_REGISTER_ROOT_PATH = "/DemoRpc";
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    public static void createPersistentNode(CuratorFramework zkClient,String path){
        try{
            if(REGISTERED_PATH_SET.contains(path)||zkClient.checkExists().forPath(path)!=null){
                log.info("The node already exists.The node is:[{}]",path);
            }else {
                zkClient.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path);
                log.info("create node[{}]successful!!",path);
            }
            REGISTERED_PATH_SET.add(path);

        }catch(Exception e){
            log.error("create persistent node for path[{}]failed",path);
        }
    }
    public static List<String> getChildrenNodes(CuratorFramework zkClient,String rpcServiceName){

        if(SERVICE_ADDRESS_MAP.containsKey(rpcServiceName)){
            return SERVICE_ADDRESS_MAP.get(rpcServiceName);
        }
        List<String> list  = null;
        String servicePath = ZK_REGISTER_ROOT_PATH+"/"+rpcServiceName;
        try{
            list = zkClient.getChildren().forPath(servicePath);
            SERVICE_ADDRESS_MAP.put(rpcServiceName,list);
            registerWatcher(rpcServiceName,zkClient);
        }catch (Exception e){
            log.error("get children nodes for path[{}] fail",servicePath);
        }

        return list;
    }
    public static void clearRegistry(CuratorFramework zkClient, InetSocketAddress inetSocketAddress){

    }

    public static CuratorFramework getZkClient(){
        if(zkClient!=null&&zkClient.getState()== CuratorFrameworkState.STARTED){
            return zkClient;
        }
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME,MAX_RETRIES);
        String zooKeeperAddress = DEFAULT_ZOOKEEPER_ADDRESS;
        zkClient = CuratorFrameworkFactory.builder()
                .connectString(zooKeeperAddress)
                .retryPolicy(retryPolicy)
                .build();
        zkClient.start();
        return zkClient;
    }

    private static void registerWatcher(String rpcServiceName,CuratorFramework zkClient){

    }

}
