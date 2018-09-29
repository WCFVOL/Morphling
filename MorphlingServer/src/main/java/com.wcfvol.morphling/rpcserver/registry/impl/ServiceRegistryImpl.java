package com.wcfvol.morphling.rpcserver.registry.impl;

import com.wcfvol.morphling.rpcserver.common.ZooKeeperConnection;
import com.wcfvol.morphling.rpcserver.common.constants.ZookeeperConstants;
import com.wcfvol.morphling.rpcserver.registry.ServiceRegistry;
import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


/**
 * @author wangchunfei
 */
public class ServiceRegistryImpl implements ServiceRegistry {
    private static Logger LOG = Logger.getLogger(ServiceRegistryImpl.class);
    private final ZooKeeper zk;
    public ServiceRegistryImpl(String zkAddress) {
        this.zk = ZooKeeperConnection.connectServer(zkAddress);
    }
    @Override
    public void registry(String serviceName, String serviceAddress) {
        String root = ZookeeperConstants.ZK_ROOT_REGISTRY_ZNODE_PATH;
        try {
            Stat statRootPath = zk.exists(root,false);
            if (null == statRootPath) {
                LOG.info("create zk root path ["+this.createPersistentNode(root)+"]");
            }
            String servicePath = root+"/"+serviceName;
            Stat serviceStat = zk.exists(servicePath,false);
            if (null == serviceStat) {
                LOG.info("create service name path["+createPersistentNode(servicePath)+"] end.");
            }
            String serviceAddressPath = servicePath + "/address["+ serviceAddress + "]";
            Stat statServiceAddress = zk.exists(serviceAddressPath, false);
            if (null == statServiceAddress) {
                String node = createNode(serviceAddressPath, serviceAddress);
                LOG.info("create service address node[" + node+"] end.");
            }
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private String createPersistentNode(String root) throws KeeperException, InterruptedException {
        Stat s = zk.exists(root,false);
        if (s == null) {
            return zk.create(root,new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
        return " createPersistentNode error.";
    }

    private String createNode(String path,String data) throws KeeperException, InterruptedException {
        return zk.create(path,data.getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
    }
}
