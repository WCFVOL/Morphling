package com.wcfvol.rpcclient.codec;

import com.wcfvol.rpcserver.common.SerializationUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class RPCEncoder extends MessageToByteEncoder {

    public Class<?> genericClass;
    public RPCEncoder(Class<?> genericClass) {
        this.genericClass = genericClass;
    }

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, Object o, ByteBuf out) throws Exception {
        //todo
        if (genericClass.isInstance(o)) {
            byte[] data = SerializationUtil.serialize(o);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
