package com.wcfvol.rpcserver.registry.impl;

import com.wcfvol.rpcserver.registry.ServiceRegistry;
import org.apache.zookeeper.ZooKeeper;

public class ServiceRegistryImpl implements ServiceRegistry {
    private final ZooKeeper zk;
    public ServiceRegistryImpl(String zkAddress) {
        this.zk = ZooKeeperConnection.connectServer(zkAddress);
    }
    public void registry(String serviceName, String serviceAddress) {

    }
}
