package com.wcfvol.rpcserver.server;

import com.wcfvol.rpcserver.bean.RPCRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName RPCServerHandler
 * @Author Wang Chunfei
 * @Date 2018/9/7 下午4:30
 **/
public class RPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {
    private final Map<String,Object> beans ;

    public RPCServerHandler(Map beans) {
        this.beans = beans;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {

    }
}
