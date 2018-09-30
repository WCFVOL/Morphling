package com.wcfvol.morphling.cache;

import com.wcfvol.morphling.zkclient.ZooKeeperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wangchunfei
 */
@Component
public class ServiceCache {
    @Autowired
    ZooKeeperClient zooKeeperClient;

    //TODO
}
