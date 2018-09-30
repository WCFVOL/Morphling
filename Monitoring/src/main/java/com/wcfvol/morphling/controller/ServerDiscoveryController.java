package com.wcfvol.morphling.controller;

import com.wcfvol.morphling.pojo.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangchunfei
 */
@RequestMapping(value = "service_discovery")
@RestController
public class ServerDiscoveryController {

    @RequestMapping(value = "list")
    public RestResult listServer() {
        return null;
    }
}
