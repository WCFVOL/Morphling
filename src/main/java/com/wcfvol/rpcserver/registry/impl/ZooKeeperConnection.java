package com.wcfvol.rpcserver.registry.impl;

import com.wcfvol.rpcserver.common.constants.ZookeeperConstants;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class ZooKeeperConnection {

    private static CountDownLatch latch = new CountDownLatch(1);

    public static  ZooKeeper connectServer(String address){
        ZooKeeper zk = null;
        try {
            zk = new ZooKeeper(address, ZookeeperConstants.ZK_SESSION_TIMEOUT, new Watcher() {
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
