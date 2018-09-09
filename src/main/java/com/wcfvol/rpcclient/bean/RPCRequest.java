package com.wcfvol.rpcclient.bean;

import lombok.Data;

import java.util.Arrays;

@Data
public class RPCRequest {
    private String requestId;
    private String interfaceName;
    private String serviceVersion;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;

    @Override
    public String toString() {
        return "requestId["+this.requestId+"] "
                +"interfaceName["+this.interfaceName+"] "
                +"serviceVersion["+this.serviceVersion+"] "
                +"methodName["+this.methodName+"] "
                +"parameterTypes["+ Arrays.toString(this.parameterTypes)+"] "
                +"parameters["+Arrays.toString(this.parameters)+"].";
    }

    public RPCRequest(String requestID, String className,
                      String serviceVersion, String methodName,
                      Class<?>[] parameterTypes, Object[] parameters) {
        this.requestId = requestID;
        this.interfaceName = className;
        this.serviceVersion = serviceVersion;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public RPCRequest create(String requestID, String className,
                             String serviceVersion, String methodName,
                             Class<?>[] parameterTypes, Object[] parameters){
        return new RPCRequest(requestID,className,serviceVersion,methodName,parameterTypes,parameters);
    }

}
