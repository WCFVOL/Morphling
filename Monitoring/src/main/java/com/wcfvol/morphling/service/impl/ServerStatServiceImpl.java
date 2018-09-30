package com.wcfvol.morphling.service.impl;

import com.wcfvol.morphling.pojo.vo.ServerVO;
import com.wcfvol.morphling.service.ServerStatService;
import com.wcfvol.morphling.zkclient.ZooKeeperClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author wangchunfei
 */
@Service
public class ServerStatServiceImpl implements ServerStatService {
    @Autowired
    ZooKeeperClient zooKeeperClient;

    @Override
    public List<ServerVO> listServers() {
        List<ServerVO> result = new ArrayList<>();
        System.out.println(zooKeeperClient.getServers());
        Map<String,List<String>> servers = zooKeeperClient.getServers();
        for (String key:servers.keySet() ) {
            ServerVO serverVO = new ServerVO();
            serverVO.setFullName(key);
            serverVO.setIp(servers.get(key));
            String[] serverFullName = key.split("-");
            serverVO.setServerName(serverFullName[0]);
            serverVO.setVersion(serverFullName[1]);
            result.add(serverVO);
        }
        return result;
    }

    @Override
    public List<ServerVO> getMonitors() {
        List<ServerVO> result = new ArrayList<>();
        Map<String,List<String>> servers = zooKeeperClient.getMonitors();
        for (String key:servers.keySet() ) {
            ServerVO serverVO = new ServerVO();
            serverVO.setFullName(key);
            serverVO.setIp(servers.get(key));
            String[] serverFullName = key.split("-");
            serverVO.setServerName(serverFullName[0]);
            serverVO.setVersion(serverFullName[1]);
            result.add(serverVO);
        }
        return result;
    }
}
