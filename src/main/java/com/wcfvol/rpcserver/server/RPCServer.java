package com.wcfvol.rpcserver.server;

import com.wcfvol.rpcserver.registry.ServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RPCServer implements ApplicationContextAware, InitializingBean {

    private String serverAddress;
    private ServiceRegistry serviceRegistry;

    private final static Map<String,Object> beans = new ConcurrentHashMap<String,Object>();

    public RPCServer(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public RPCServer(String serverAddress,ServiceRegistry serviceRegistry) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = serviceRegistry;
    }

    public void afterPropertiesSet() throws Exception {
    }

    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String,Object> beanMap = ctx.getBeansWithAnnotation(RPCService.class);
        if (!CollectionUtils.isEmpty(beanMap)) {
            for (Object bean:beanMap.values()) {
                RPCService rpcService = bean.getClass().getAnnotation(RPCService.class);
                String serviceName = rpcService.value().getName();
                String version = rpcService.version();
                if (StringUtils.isNotBlank(version)) {
                    serviceName+="-"+version;
                }
                beans.put(serviceName,bean);
            }
        }

    }
}
