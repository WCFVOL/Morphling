package com.wcfvol.rpcserver.server;

import com.wcfvol.rpcserver.bean.RPCRequest;
import com.wcfvol.rpcserver.bean.RPCResponse;
import com.wcfvol.rpcserver.codec.RPCDecoder;
import com.wcfvol.rpcserver.codec.RPCEncoder;
import com.wcfvol.rpcserver.registry.ServiceRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import java.net.InetSocketAddress;
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
        EventLoopGroup group = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(group)
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
        bootstrap.bind(ip,port).sync();

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
