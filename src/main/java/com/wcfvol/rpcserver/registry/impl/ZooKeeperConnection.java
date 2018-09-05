package com.wcfvol.rpcserver.registry.impl;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperConnection {
    private static String ZOOKEEPER_HOSTS="127.0.0.1:2181"; // ','分割可以有多個比如127.0.0.1:3000,127.0.0.1:3001...
    private static int ZK_SESSION_TIMEOUT = 5000;
    private static int ZK_CONNECTION_TIMEOUT = 1000;
    private static String ZK_ROOT_REGISTRY_ZNODE_PATH = "/simpleRPC"; //服务在zk下的路径
    private static CountDownLatch latch = new CountDownLatch(1);

    public static  ZooKeeper connectServer(String address){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(address, ZK_SESSION_TIMEOUT, new Watcher() {
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
