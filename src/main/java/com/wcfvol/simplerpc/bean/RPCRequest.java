package com.wcfvol.simplerpc.bean;

import lombok.Data;

import java.util.Arrays;

@Data
public class RPCRequest {
    private String requestID;
    private String className;
    private String methodName;
    private Class<?>[] parameterTypes;
    private Object[] parameters;
    private String serviceVersion;

    @Override
    public String toString() {
        return "requestId["+this.requestID+"] "
                +"interfaceName["+this.className+"] "
                +"serviceVersion["+this.serviceVersion+"] "
                +"methodName["+this.methodName+"] "
                +"parameterTypes["+ Arrays.toString(this.parameterTypes)+"] "
                +"parameters["+Arrays.toString(this.parameters)+"].";
    }

    private RPCRequest(String requestID, String className,
                       String serviceVersion, String methodName,
                       Class<?>[] parameterTypes, Object[] parameters) {
        this.requestID = requestID;
        this.className = className;
        this.serviceVersion = serviceVersion;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.parameters = parameters;
    }

    public RPCRequest build(String requestID, String className,
                 String serviceVersion, String methodName,
                 Class<?>[] parameterTypes, Object[] parameters){
        return new RPCRequest(requestID,className,serviceVersion,methodName,parameterTypes,parameters);
    }
}
