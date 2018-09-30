package com.wcfvol.morphling.controller;

import com.wcfvol.morphling.pojo.RestResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wangchunfei
 */
@RestController
@RequestMapping(value = "server_stat")
public class ServerStatController {

    @RequestMapping(value = "all")
    public RestResult getAllInfo() {
        return null;
    }
}
