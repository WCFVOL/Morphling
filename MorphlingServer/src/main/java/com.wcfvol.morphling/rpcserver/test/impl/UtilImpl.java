package com.wcfvol.morphling.rpcserver.test.impl;

import com.wcfvol.morphling.rpcserver.server.RPCService;
import com.wcfvol.morphling.rpcserver.test.Util;

import java.util.Date;

@RPCService(value=Util.class,version = "com.wcfvol.rpcserver")
public class UtilImpl implements Util {
    @Override
    public Date getTime() {
        return new Date();
    }
}
