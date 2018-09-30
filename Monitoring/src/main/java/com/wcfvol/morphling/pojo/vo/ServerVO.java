package com.wcfvol.morphling.pojo.vo;

import lombok.Data;

import java.util.List;

/**
 * @author wangchunfei
 */
@Data
public class ServerVO {
    private String fullName;
    private String serverName;
    private String version;
    private List<String> url;
    private List<Integer> allCall;
    private List<Integer> successCall;
}
