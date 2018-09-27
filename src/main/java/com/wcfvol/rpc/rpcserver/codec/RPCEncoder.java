package com.wcfvol.rpc.rpcserver.codec;

import com.wcfvol.rpc.rpcserver.common.SerializationUtil;
import com.wcfvol.rpc.rpcserver.common.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

public class RPCEncoder extends MessageToByteEncoder {

    public Class<?> genericClass;
    public RPCEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
        if (genericClass.isInstance(o)) {
            byte[] data = SerializationUtil.serialize(o);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
