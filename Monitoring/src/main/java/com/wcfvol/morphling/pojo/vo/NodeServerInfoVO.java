package com.wcfvol.morphling.pojo.vo;

import lombok.Data;

@Data
public class NodeServerInfoVO {
    private String fullName;
    private String version;
    private String serverName;
    private Integer allCall;
    private Integer successCall;
}
