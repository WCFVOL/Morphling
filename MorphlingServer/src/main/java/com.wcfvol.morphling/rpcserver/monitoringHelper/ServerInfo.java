package com.wcfvol.morphling.rpcserver.monitoringHelper;

import java.io.IOException;
import java.util.Map;

public interface ServerInfo {
    Map getAllInfo();
    Double getMemInfo();
    Double getCPUInfo();
    Double getIOInfo();
    Double getNetInfo();
}
