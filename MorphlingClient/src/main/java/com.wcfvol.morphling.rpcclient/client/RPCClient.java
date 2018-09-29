package com.wcfvol.morphling.rpcclient.client;

import com.wcfvol.morphling.rpcclient.bean.RPCRequest;
import com.wcfvol.morphling.rpcclient.bean.RPCResponse;
import com.wcfvol.morphling.rpcclient.codec.RPCDecoder;
import com.wcfvol.morphling.rpcclient.codec.RPCEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

/**
 * @ClassName RPCClient
 * @Author Wang Chunfei
 * @Date 2018/9/8 下午8:21
 **/
public class RPCClient extends SimpleChannelInboundHandler<RPCResponse> {
    private final static Logger LOG = Logger.getLogger(RPCClient.class);
    private String host;
    private int port;
    private RPCResponse response;
    public RPCClient(String host,int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse) throws Exception {
        this.response = rpcResponse;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        LOG.error("api exceptionCaught ", cause);
        ctx.close();
    }

    public RPCResponse send(RPCRequest request) throws InterruptedException {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class).handler(
                new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel channel) throws Exception {
                        ChannelPipeline pipeline = channel.pipeline();
                        pipeline.addLast(new RPCEncoder(RPCRequest.class));
                        pipeline.addLast(new RPCDecoder(RPCResponse.class));
                        pipeline.addLast(RPCClient.this);
                    }
                }
        );
        ChannelFuture f = bootstrap.connect(host,port).sync();
        Channel channel = f.channel();
        channel.writeAndFlush(request).sync();
        channel.closeFuture().sync();
        group.shutdownGracefully().sync();
        return response;
    }
}
