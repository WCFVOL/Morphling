package com.wcfvol.morphling.service;

import com.wcfvol.morphling.pojo.vo.ServerVO;

import java.util.List;

/**
 * @author wangchunfei
 */
public interface ServerStatService {
    List<ServerVO> listServers();
    List<ServerVO> getMonitors();

}
