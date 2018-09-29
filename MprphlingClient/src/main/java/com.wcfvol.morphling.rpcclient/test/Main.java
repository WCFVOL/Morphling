package com.wcfvol.morphling.rpcclient.test;

import com.wcfvol.morphling.rpcclient.client.RPCProxy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @ClassName Main
 * @Author Wang Chunfei
 * @Date 2018/9/8 下午4:15
 **/
public class Main {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("client.xml");
        RPCProxy proxy = context.getBean(RPCProxy.class);
        Util util = proxy.create(Util.class,"com.wcfvol.rpcserver");
        System.out.println(util.getTime());
    }
}
