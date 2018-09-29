package com.wcfvol.morphling.rpcserver.common.constants;

public class ZookeeperConstants {
    public static String ZOOKEEPER_HOSTS="106.15.183.211:2181"; // ','分割可以有多個比如127.0.0.1:3000,127.0.0.1:3001...
    public static int ZK_SESSION_TIMEOUT = 5000;
    public static int ZK_CONNECTION_TIMEOUT = 1000;
    public static String ZK_ROOT_REGISTRY_ZNODE_PATH = "/simpleRPC"; //服务在zk下的路径
}
