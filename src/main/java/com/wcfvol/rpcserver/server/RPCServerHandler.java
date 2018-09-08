package com.wcfvol.rpcserver.server;

import com.wcfvol.rpcserver.bean.RPCRequest;
import com.wcfvol.rpcserver.bean.RPCResponse;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    protected void messageReceived(ChannelHandlerContext context, RPCRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        RPCResponse response = new RPCResponse();
        response.setRequestId(request.getRequestId());
        response.setResult(this.handle(request));
        context.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private Object handle(RPCRequest request) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String className = request.getInterfaceName();
        String version = request.getServiceVersion();
        if (StringUtils.isNotBlank(version)) {
            className += "-" + version;
        }
        Object serverBean = beans.get(className);
        if (null == serverBean) {
            throw new RuntimeException("can't find service bean: "+className);
        }
        Class<?> serviceClass = serverBean.getClass();
        String methodName = request.getMethodName();
        Class<?>[] parameterTypes = request.getParameterTypes();
        // 反射执行方法
        Object[] parameters = request.getParameters();
        Method method = serviceClass.getMethod(methodName,parameterTypes);
        method.setAccessible(true);
        Object ans = null;
        ans = method.invoke(serverBean,parameters);
        return ans;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
    }
}
