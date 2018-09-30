package com.wcfvol.morphling.zkclient;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperConnection {
    // ','分割可以有多個比如127.0.0.1:3000,127.0.0.1:3001...
    // public static String ZOOKEEPER_HOSTS="106.15.183.211:2181";

    public static int ZK_SESSION_TIMEOUT = 5000;
    public static int ZK_CONNECTION_TIMEOUT = 1000;
    //服务在zk下的路径
    public static String ZK_ROOT_REGISTRY_ZNODE_PATH = "/Morphling";
    private static CountDownLatch latch = new CountDownLatch(1);

    public static  ZooKeeper connectServer(String address){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(address, ZK_SESSION_TIMEOUT, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        latch.countDown();
                    }
                }
            });
            latch.await();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return zk;
    }
}
