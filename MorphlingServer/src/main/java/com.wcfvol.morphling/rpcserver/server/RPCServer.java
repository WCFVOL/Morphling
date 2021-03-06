package com.wcfvol.morphling.rpcserver.server;

import com.wcfvol.morphling.rpcserver.registry.ServiceRegistry;
import com.wcfvol.morphling.rpcserver.bean.RPCRequest;
import com.wcfvol.morphling.rpcserver.bean.RPCResponse;
import com.wcfvol.morphling.rpcserver.codec.RPCDecoder;
import com.wcfvol.morphling.rpcserver.codec.RPCEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RPCServer implements ApplicationContextAware, InitializingBean {
    private final static Logger LOG = Logger.getLogger(RPCServer.class);

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

    @Override
    public void afterPropertiesSet() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group,work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new RPCDecoder(RPCRequest.class));
                        pipeline.addLast(new RPCEncoder(RPCResponse.class));
                        pipeline.addLast(new RPCServerHandler(beans));
                    }
                });
        String[] address = serverAddress.split(":");
        String ip = address[0];
        int port = Integer.parseInt(address[1]);
        ChannelFuture f= bootstrap.bind(ip,port).sync();
        // 注册服务到ZK
        if (null != serviceRegistry) {
            for (String className : beans.keySet()) {
                serviceRegistry.registry(className,serverAddress);
            }
        }
        f.channel().closeFuture().sync();
        group.shutdownGracefully().sync();
        work.shutdownGracefully().sync();
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String,Object> beanMap = ctx.getBeansWithAnnotation(RPCService.class);
        if (!CollectionUtils.isEmpty(beanMap)) {
            for (Object bean:beanMap.values()) {
                RPCService rpcService = bean.getClass().getAnnotation(RPCService.class);
                String serviceName = rpcService.value().getSimpleName();
                String version = rpcService.version();
                if (StringUtils.isNotBlank(version)){
                    serviceName+="-"+version;
                }
                LOG.info("get service bean: [ "+serviceName+" ]");
                beans.put(serviceName,bean);
            }
        }

    }
}
