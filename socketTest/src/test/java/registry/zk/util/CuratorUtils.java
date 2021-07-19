package registry.zk.util;
import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.CuratorFrameworkState;
import org.apache.curator.retry.ExponentialBackoffRetry;
@Slf4j
public class CuratorUtils {
    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    private static CuratorFramework zkClient;
    public static final String ZK_REGISTER_ROOT_PATH = "/DemoRpc";
    private static final String DEFAULT_ZOOKEEPER_ADDRESS = "127.0.0.1:2181";
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
    public static void createPersistentNode(CuratorFramework zkClient,String path){
        try{

        }catch(Exception e){
            log.error();
        }
    }
    public static List<String> getChildrenNodes(CuratorFramework zkClient,String rpcServiceName){
        List<String> res  = null;
        return res;
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
