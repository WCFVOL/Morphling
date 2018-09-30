package com.wcfvol.morphling.pojo;

import java.io.Serializable;

/**
 * @ClassName RestResult
 * @Author Wang Chunfei
 * @Date 2018/7/2 上午8:32
 **/
public class RestResult<T> implements Serializable {
    private static final long serialVersionUID = 9107885251530031692L;

    private int code;
    private String msg;
    private T data;
    public Boolean isSuccess() {
        if (code == 1) {
            return true;
        }
        return false;
    }
    public static <T> RestResult<T> ok() {
        RestResult<T> restResult = new RestResult<>();
        return restResult.setCode(1);
    }

    public static <T> RestResult<T> ok(String message) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setMsg(message);
        return restResult.setCode(1);
    }

    public static <T> RestResult<T> fail(int code, String message) {
        RestResult<T> restResult = new RestResult<>();
        return restResult.setCode(code).setMsg(message);
    }

    public RestResult<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public RestResult<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public RestResult<T> setData(T data) {
        this.data = data;
        return this;
    }


    public int getCode() {
        return code;
    }
    public String getMsg() {
        return msg;
    }
    public T getData() {
        return data;
    }
}
