package com.wcfvol.rpc.rpcserver.bean;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

@Data
public class RPCResponse {
    private String requestId;
    private Exception exception;
    private Object result;
    public boolean hasException() {
        return null != exception;
    }

    @Override
    public String toString() {
        return "requestId["+this.requestId+"] "
                +"exception["+JSONObject.toJSONString(this.exception)+"] "
                +"result["+JSONObject.toJSONString(this.result)+"].";
    }
}
