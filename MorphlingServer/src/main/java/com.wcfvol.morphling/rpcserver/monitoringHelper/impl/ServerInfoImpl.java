package com.wcfvol.morphling.rpcserver.monitoringHelper.impl;

import com.wcfvol.morphling.rpcserver.monitoringHelper.ServerInfo;
import com.wcfvol.morphling.rpcserver.server.RPCServer;
import com.wcfvol.morphling.rpcserver.server.RPCService;
import org.apache.log4j.Logger;
import org.hyperic.sigar.*;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName ServerInfoImpl
 * @Author Wang Chunfei
 * @Date 2018/9/29 下午5:11
 **/
@RPCService(value = ServerInfo.class, version = "monitoring")
public class ServerInfoImpl implements ServerInfo {
    private final static Logger log = Logger.getLogger(ServerInfoImpl.class);

    public static void main(String[] args) throws IOException {
        System.out.println(new ServerInfoImpl().getAllInfo());
    }

    @Override
    public Map getAllInfo() {
        Map result = new HashMap();
        result.put("cpu", getCPUInfo());
        result.put("mem",getMemInfo());
        result.put("io",getIOInfo());
        result.put("net",getNetInfo());
        return result;
    }

    @Override
    public Double getMemInfo() {
        log.info("开始收集memory使用率");
        Double memUsage = -1.0;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/meminfo";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count = 0;
            long totalMem = 0, freeMem = 0;
            while ((line = in.readLine()) != null) {
                log.info(line);
                String[] memInfo = line.split("\\s+");
                if (memInfo[0].startsWith("MemTotal")) {
                    totalMem = Long.parseLong(memInfo[1]);
                }
                if (memInfo[0].startsWith("MemFree")) {
                    freeMem = Long.parseLong(memInfo[1]);
                }
                memUsage = 1 - (double) freeMem / (double) totalMem;
                log.info("本节点内存使用率为: " + memUsage);
                if (++count == 2) {
                    break;
                }
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("MemUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        return memUsage;
    }


    @Override
    public Double getCPUInfo() {
        log.info("开始收集cpu使用率");
        Double cpuUsage = Double.valueOf(-1);
        Process pro1, pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/stat";
            //第一次采集CPU时间
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            //分别为系统启动后空闲的CPU时间和总的CPU时间
            long idleCpuTime1 = 0, totalCpuTime1 = 0;
            while ((line = in1.readLine()) != null) {
                if (line.startsWith("cpu")) {
                    line = line.trim();
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    idleCpuTime1 = Long.parseLong(temp[4]);
                    for (String s : temp) {
                        if (!"cpu".equals(s)) {
                            totalCpuTime1 += Long.parseLong(s);
                        }
                    }
                    log.info("IdleCpuTime: " + idleCpuTime1 + ", " + "TotalCpuTime" + totalCpuTime1);
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                log.error("CpuUsage休眠时发生InterruptedException. " + e.getMessage());
                log.error(sw.toString());
            }
            //第二次采集CPU时间
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long idleCpuTime2 = 0, totalCpuTime2 = 0;
            while ((line = in2.readLine()) != null) {
                if (line.startsWith("cpu")) {
                    line = line.trim();
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    idleCpuTime2 = Long.parseLong(temp[4]);
                    for (String s : temp) {
                        if (!s.equals("cpu")) {
                            totalCpuTime2 += Long.parseLong(s);
                        }
                    }
                    log.info("IdleCpuTime: " + idleCpuTime2 + ", " + "TotalCpuTime" + totalCpuTime2);
                    break;
                }
            }
            if (idleCpuTime1 != 0 && totalCpuTime1 != 0 && idleCpuTime2 != 0 && totalCpuTime2 != 0) {
                cpuUsage = 1 - (double) (idleCpuTime2 - idleCpuTime1) / (double) (totalCpuTime2 - totalCpuTime1);
                log.info("本节点CPU使用率为: " + cpuUsage);
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("CpuUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
            return Double.valueOf(-1);
        }
        return Double.valueOf(cpuUsage);
    }

    @Override
    public Double getIOInfo() {
        log.info("开始收集磁盘IO使用率");
        Double ioUsage = -1.0;
        Process pro = null;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "iostat -d -x";
            pro = r.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(pro.getInputStream()));
            String line = null;
            int count =  0;
            while((line=in.readLine()) != null){
                if(++count >= 4){
//                    log.info(line);
                    String[] temp = line.split("\\s+");
                    if(temp.length > 1){
                        float util =  Float.parseFloat(temp[temp.length-1]);
                        ioUsage = (ioUsage>util)?ioUsage:util;
                    }
                }
            }
            if(ioUsage > 0){
                log.info("本节点磁盘IO使用率为: " + ioUsage);
                ioUsage /= 100;
            }
            in.close();
            pro.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("IoUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
            return ioUsage;
        }
        return ioUsage;
    }

    @Override
    public Double getNetInfo() {
        log.info("开始收集网络带宽使用率");
        Double netUsage = -1.0;
        Double curRate = 0.0;
        Process pro1,pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/net/dev";
            //第一次采集流量数据
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line = null;
            long inSize1 = 0, outSize1 = 0;
            while((line=in1.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("eth0")||line.startsWith("wlp3s0")) {
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    inSize1 = Long.parseLong(temp[1]);    //Receive bytes,单位为Byte
                    outSize1 = Long.parseLong(temp[9]);                //Transmit bytes,单位为Byte
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                log.error("NetUsage休眠时发生InterruptedException. " + e.getMessage());
                log.error(sw.toString());
            }
            //第二次采集流量数据
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long inSize2 = 0 ,outSize2 = 0;
            while((line=in2.readLine()) != null){
                line = line.trim();
                if(line.startsWith("eth0")||line.startsWith("wlp3s0")){
                    log.info(line);
                    String[] temp = line.split("\\s+");
                    inSize2 = Long.parseLong(temp[1]);
                    outSize2 = Long.parseLong(temp[9]);
                    break;
                }
            }
            if(inSize1 != 0 && outSize1 !=0 && inSize2 != 0 && outSize2 !=0){
                double interval = (double)(endTime - startTime)/1000;
                //网口传输速度,单位为bps
                curRate = (double)(inSize2 - inSize1 + outSize2 - outSize1)*8/(1000000*interval);
                //netUsage = curRate/totalBandwidth;
                log.info("本节点网口速度为: " + curRate + "Mbps");
                //log.info("本节点网络带宽使用率为: " + netUsage);
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            log.error("NetUsage发生InstantiationException. " + e.getMessage());
            log.error(sw.toString());
        }
        return curRate;
    }
}

