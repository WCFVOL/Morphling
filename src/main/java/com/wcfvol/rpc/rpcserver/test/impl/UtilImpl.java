package com.wcfvol.rpc.rpcserver.test.impl;

import com.wcfvol.rpc.rpcserver.server.RPCService;
import com.wcfvol.rpc.rpcserver.test.Util;

import java.util.Date;

@RPCService(value=Util.class,version = "com.wcfvol.rpcserver")
public class UtilImpl implements Util {
    public Date getTime() {
        return new Date();
    }
}
