package com.wcfvol.morphling.zkclient;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.wcfvol.morphling.zkclient.ZooKeeperConnection.ZK_ROOT_REGISTRY_ZNODE_PATH;
import static com.wcfvol.morphling.zkclient.ZooKeeperConnection.connectServer;


/**
 * @author wangchunfei
 */
@Component
public class ZooKeeperClient {
    public static final Logger log = Logger.getLogger(ZooKeeperClient.class);

    public Map<String, List<String>> getServers() {
        return servers;
    }

    private Map<String, List<String>> servers = new ConcurrentHashMap<>();

    public Map<String, List<String>> getMonitors() {
        return monitors;
    }

    private Map<String, List<String>> monitors = new ConcurrentHashMap<>();
    private final ZooKeeper zk;

    public ZooKeeperClient(@Value("${zookeeper.host}") String host) throws KeeperException, InterruptedException {
        zk = connectServer(host);
        log.info("zookeeper connect success!");
        if (zk != null) {
            watchNode(zk);
        }
    }

    private void watchNode(final ZooKeeper zk) throws KeeperException, InterruptedException {
        Map<String,List<String>> servers = new ConcurrentHashMap<>();
        String rootPath = ZK_ROOT_REGISTRY_ZNODE_PATH;
        List<String> serviceNameList = zk.getChildren(rootPath, event -> {
            if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                try {
                    watchNode(zk);
                } catch (KeeperException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        for (String serviceName : serviceNameList) {
            String serviceNamePath = rootPath+"/"+serviceName;
            List<String> servicePath = zk.getChildren(serviceNamePath, watchedEvent -> {
                if (watchedEvent.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                    try {
                        watchNode(zk);
                    } catch (KeeperException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
            if (servicePath.isEmpty()) {
                log.error("service path "+serviceNamePath+"is empty.");
                continue;
            }
            List<String> tmpAddress = new ArrayList<String>();
            for (String address : servicePath) {
                String addressPath = serviceNamePath+"/"+address;
                byte[] data = zk.getData(addressPath,false,null);
                tmpAddress.add(new String(data, Charset.defaultCharset()));
            }
            String[] fullName = serviceName.split("-");
            if ("monitoring".equals(fullName[1])) {
                monitors.put(serviceName,tmpAddress);
            } else {
                servers.put(serviceName,tmpAddress);
            }
        }
        System.out.println(servers);
        this.servers=servers;
        log.info("servers:"+servers.toString());
    }

}
