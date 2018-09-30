package com.wcfvol.morphling.service.impl;

import com.wcfvol.morphling.service.ServerStatService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServerStatServiceImplTest {

    @Autowired
    ServerStatService serverStatService;

    @Test
    public void listServer() {
        System.out.println("list"+serverStatService.getMonitors());
    }
}