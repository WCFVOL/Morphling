package com.wcfvol.rpcclient.Discovery.impl;

import com.wcfvol.rpcclient.Discovery.ServiceDiscovery;
import com.wcfvol.rpcclient.common.constants.ZookeeperConstants;
import com.wcfvol.rpcserver.registry.impl.ZooKeeperConnection;
import io.netty.util.internal.ThreadLocalRandom;
import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName ServiceDiscoveryImpl
 * @Author Wang Chunfei
 * @Date 2018/9/8 下午9:09
 **/
public class ServiceDiscoveryImpl extends ZooKeeperConnection implements ServiceDiscovery{
    private static final Logger LOG = Logger.getLogger(ServiceDiscoveryImpl.class);
    private final ZooKeeper zk;
    private static Map<String,List<String>> servers = new ConcurrentHashMap<String, List<String>>();

    public ServiceDiscoveryImpl(String zkAddress) throws KeeperException, InterruptedException {
        this.zk = connectServer(zkAddress);
        if (zk != null) {
            watchNode(zk);
        }
    }

    private void watchNode(final ZooKeeper zk) throws KeeperException, InterruptedException {
        Map<String,List<String>> servers = new ConcurrentHashMap<String, List<String>>();
        String rootPath = ZookeeperConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
        List<String> serviceNameList = zk.getChildren(rootPath, new Watcher() {
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeChildrenChanged) {
                    try {
                        watchNode(zk);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        for (String serviceName : serviceNameList) {
            String serviceNamePath = rootPath+"/"+serviceName;
            List<String> servicePath = zk.getChildren(serviceNamePath, new Watcher() {
                public void process(WatchedEvent watchedEvent) {
                    if (watchedEvent.getType() == Event.EventType.NodeChildrenChanged) {
                        try {
                            watchNode(zk);
                        } catch (KeeperException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            if (servicePath.isEmpty()) {
                LOG.error("service path "+serviceNamePath+"is empty.");
                continue;
            }
            List<String> tmpAddress = new ArrayList<String>();
            for (String address : servicePath) {
                String addressPath = serviceNamePath+"/"+address;
                byte[] data = zk.getData(addressPath,false,null);
                tmpAddress.add(new String(data, Charset.defaultCharset()));
            }
            servers.put(serviceNamePath,tmpAddress);
        }
        ServiceDiscoveryImpl.servers=servers;
        LOG.info("servers:"+servers.toString());
    }

    @Override
    public String discover(String serviceName) {
        String servicePath = ZookeeperConstants.ZK_ROOT_REGISTRY_ZNODE_PATH+"/"+serviceName;
        List<String> address = servers.get(servicePath);
        int size = address.size();
        if (size == 1) {
            return address.get(0);
        } else {
            return address.get(ThreadLocalRandom.current().nextInt(size));
        }
    }
}
