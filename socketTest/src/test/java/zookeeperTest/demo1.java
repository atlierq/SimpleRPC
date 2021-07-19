package zookeeperTest;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;

public class demo1 {
    public static void main(String[] args) throws Exception {

        final int BASE_SLEEP_TIME = 1000;
        final int MAX_RETRIES = 3;
//        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME,MAX_RETRIES);
//        CuratorFramework zkClient = CuratorFrameworkFactory.builder()
//                .connectString("127.0.0.1:2181")
//                .retryPolicy(retryPolicy)
//                .build();
//        zkClient.start();

        RetryPolicy retryPolicy = new ExponentialBackoffRetry(BASE_SLEEP_TIME, MAX_RETRIES);
        CuratorFramework build = CuratorFrameworkFactory.builder()
                // the server to connect to (can be a server list)
                .connectString("127.0.0.1:2181")
                .retryPolicy(retryPolicy)
                .build();


        build.start();
        build.create().forPath("/node1");
    }
}
