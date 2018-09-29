package com.wcfvol.morphling.rpcclient.client;

import com.wcfvol.morphling.rpcclient.bean.RPCRequest;
import com.wcfvol.morphling.rpcclient.bean.RPCResponse;
import com.wcfvol.morphling.rpcclient.discovery.ServiceDiscovery;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @ClassName RPCProxy
 * @Author Wang Chunfei
 * @Date 2018/9/8 下午8:21
 **/
public class RPCProxy {
    private final static Logger LOG = Logger.getLogger(RPCClient.class);
    private String serviceAddress;
    private ServiceDiscovery serviceDiscovery;
    public RPCProxy(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public <T> T create(final Class<?> interfaceName) {
        return create(interfaceName,"");
    }

    @SuppressWarnings("unchecked")
    public <T> T create(final Class<?> interfaceName,final String serviceVersion) {
        return (T)Proxy.newProxyInstance(
                interfaceName.getClassLoader(),
                new Class<?>[]{interfaceName},
                (proxy, method, args) -> {
                    RPCRequest request = new RPCRequest(
                            UUID.randomUUID().toString(),
                            method.getDeclaringClass().getSimpleName(),
                            serviceVersion,
                            method.getName(),
                            method.getParameterTypes(),
                            args
                    );
                    System.out.println("request:"+request);
                    if (null != serviceDiscovery) {
                        String serviceName = interfaceName.getSimpleName();
                        if (StringUtils.isNotBlank(serviceVersion)) {
                            serviceName+="-"+serviceVersion;
                        }
                        serviceAddress = serviceDiscovery.discover(serviceName);
                    }
                    if (StringUtils.isEmpty(serviceAddress)){
                        throw new RuntimeException("server address is empty");
                    }
                    String[] addressArray = serviceAddress.split(":");
                    String host = addressArray[0];
                    int port = Integer.parseInt(addressArray[1]);
                    RPCClient rpcClient = new RPCClient(host,port);
                    long time = System.currentTimeMillis();
                    RPCResponse response = rpcClient.send(request);
                    if (null == response) {
                        throw new RuntimeException("response is null");
                    }
                    LOG.info("Request:"+request.toString()+";Response:"+response.toString()+";execu time[" + (System.currentTimeMillis()-time) +"] ms.");
                    if (response.hasException()) {
                        throw response.getException();
                    }
                    return response.getResult();
                }
        );
    }
}
