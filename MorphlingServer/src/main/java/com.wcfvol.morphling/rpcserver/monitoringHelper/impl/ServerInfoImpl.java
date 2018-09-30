package com.wcfvol.morphling.rpcserver.monitoringHelper.impl;

import com.wcfvol.morphling.rpcserver.monitoringHelper.ServerInfo;
import com.wcfvol.morphling.rpcserver.server.RPCService;
import org.apache.log4j.Logger;
import org.hyperic.sigar.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Properties;

/**
 * @ClassName ServerInfoImpl
 * @Author Wang Chunfei
 * @Date 2018/9/29 下午5:11
 **/
@RPCService(value = ServerInfo.class,version = "con.wcfvol.morphling.monitoring")
public class ServerInfoImpl implements ServerInfo {
    @Override
    public Map getCPUInfo() {
        return null;
    }
}


class RuntimeTest {
    public static void main(String[] args) {
        new RuntimeTest();
    }
    public static final Logger logger = Logger.getLogger(RuntimeTest.class);

    static {
        try {
            // System信息，从jvm获取
            property();
            logger.info("----------------------------------");
            // cpu信息
            cpu();
            logger.info("----------------------------------");
            // 内存信息
            memory();
            logger.info("----------------------------------");
            // 操作系统信息
            os();
            logger.info("----------------------------------");
            // 用户信息
            who();
            logger.info("----------------------------------");
            // 文件系统信息
            try {
                file();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }

            logger.info("----------------------------------");
            // 网络信息
            net();
            logger.info("----------------------------------");
            // 以太网信息
            ethernet();
            logger.info("----------------------------------");
        } catch (Exception e1) {
            logger.info("获取系统配置异常:"+e1.getMessage());
        }
    }

    private static void property() throws UnknownHostException {
        Runtime r = Runtime.getRuntime();
        Properties props = System.getProperties();
        InetAddress addr;
        addr = InetAddress.getLocalHost();
        String ip = addr.getHostAddress();
        Map<String, String> map = System.getenv();
        String userName = map.get("USERNAME");// 获取用户名
        String computerName = map.get("COMPUTERNAME");// 获取计算机名
        String userDomain = map.get("USERDOMAIN");// 获取计算机域名
        logger.info("用户名:" + userName);
        logger.info("计算机名:" + computerName);
        logger.info("计算机域名:" + userDomain);
        logger.info("本地ip地址:" + ip);
        logger.info("本地主机名:" + addr.getHostName());
        logger.info("JVM可以使用的总内存:" + r.totalMemory());
        logger.info("JVM可以使用的剩余内存:" + r.freeMemory());
        logger.info("JVM可以使用的处理器个数:" + r.availableProcessors());
        logger.info("Java的运行环境版本：" + props.getProperty("java.version"));
        logger.info("Java的运行环境供应商：" + props.getProperty("java.vendor"));
        logger.info("Java供应商的URL：" + props.getProperty("java.vendor.url"));
        logger.info("Java的安装路径：" + props.getProperty("java.home"));
        logger.info("Java的虚拟机规范版本：" + props.getProperty("java.vm.specification.version"));
        logger.info("Java的虚拟机规范供应商：" + props.getProperty("java.vm.specification.vendor"));
        logger.info("Java的虚拟机规范名称：" + props.getProperty("java.vm.specification.name"));
        logger.info("Java的虚拟机实现版本：" + props.getProperty("java.vm.version"));
        logger.info("Java的虚拟机实现供应商：" + props.getProperty("java.vm.vendor"));
        logger.info("Java的虚拟机实现名称：" + props.getProperty("java.vm.name"));
        logger.info("Java运行时环境规范版本：" + props.getProperty("java.specification.version"));
        logger.info("Java运行时环境规范供应商：" + props.getProperty("java.specification.vender"));
        logger.info("Java运行时环境规范名称：" + props.getProperty("java.specification.name"));
        logger.info("Java的类格式版本号：" + props.getProperty("java.class.version"));
        logger.info("Java的类路径：" + props.getProperty("java.class.path"));
        logger.info("加载库时搜索的路径列表：" + props.getProperty("java.library.path"));
        logger.info("默认的临时文件路径：" + props.getProperty("java.io.tmpdir"));
        logger.info("一个或多个扩展目录的路径：" + props.getProperty("java.ext.dirs"));
        logger.info("操作系统的名称：" + props.getProperty("os.name"));
        logger.info("操作系统的构架：" + props.getProperty("os.arch"));
        logger.info("操作系统的版本：" + props.getProperty("os.version"));
        logger.info("文件分隔符：" + props.getProperty("file.separator"));
        logger.info("路径分隔符：" + props.getProperty("path.separator"));
        logger.info("行分隔符：" + props.getProperty("line.separator"));
        logger.info("用户的账户名称：" + props.getProperty("user.name"));
        logger.info("用户的主目录：" + props.getProperty("user.home"));
        logger.info("用户的当前工作目录：" + props.getProperty("user.dir"));
    }

    private static void memory() throws SigarException {
        Sigar sigar = new Sigar();
        Mem mem = sigar.getMem();
        // 内存总量
        logger.info("内存总量:" + mem.getTotal() / 1024L + "K av");
        // 当前内存使用量
        logger.info("当前内存使用量:" + mem.getUsed() / 1024L + "K used");
        // 当前内存剩余量
        logger.info("当前内存剩余量:" + mem.getFree() / 1024L + "K free");
        Swap swap = sigar.getSwap();
        // 交换区总量
        logger.info("交换区总量:" + swap.getTotal() / 1024L + "K av");
        // 当前交换区使用量
        logger.info("当前交换区使用量:" + swap.getUsed() / 1024L + "K used");
        // 当前交换区剩余量
        logger.info("当前交换区剩余量:" + swap.getFree() / 1024L + "K free");
    }

    private static void cpu() throws SigarException {
        Sigar sigar = new Sigar();
        CpuInfo infos[] = sigar.getCpuInfoList();
        CpuPerc cpuList[] = null;
        cpuList = sigar.getCpuPercList();
        for (int i = 0; i < infos.length; i++) {// 不管是单块CPU还是多CPU都适用
            CpuInfo info = infos[i];
            logger.info("第" + (i + 1) + "块CPU信息");
            logger.info("CPU的总量MHz:" + info.getMhz());// CPU的总量MHz
            logger.info("CPU生产商:" + info.getVendor());// 获得CPU的卖主，如：Intel
            logger.info("CPU类别:" + info.getModel());// 获得CPU的类别，如：Celeron
            logger.info("CPU缓存数量:" + info.getCacheSize());// 缓冲存储器数量
            printCpuPerc(cpuList[i]);
        }
    }

    private static void printCpuPerc(CpuPerc cpu) {
        logger.info("CPU用户使用率:" + CpuPerc.format(cpu.getUser()));// 用户使用率
        logger.info("CPU系统使用率:" + CpuPerc.format(cpu.getSys()));// 系统使用率
        logger.info("CPU当前等待率:" + CpuPerc.format(cpu.getWait()));// 当前等待率
        logger.info("CPU当前错误率:" + CpuPerc.format(cpu.getNice()));//
        logger.info("CPU当前空闲率:" + CpuPerc.format(cpu.getIdle()));// 当前空闲率
        logger.info("CPU总的使用率:" + CpuPerc.format(cpu.getCombined()));// 总的使用率
    }

    private static void os() {
        OperatingSystem OS = OperatingSystem.getInstance();
        // 操作系统内核类型如： 386、486、586等x86
        logger.info("操作系统:" + OS.getArch());
        logger.info("操作系统CpuEndian():" + OS.getCpuEndian());//
        logger.info("操作系统DataModel():" + OS.getDataModel());//
        // 系统描述
        logger.info("操作系统的描述:" + OS.getDescription());
        // 操作系统类型
        // logger.info("OS.getName():"+ OS.getName());
        // logger.info("OS.getPatchLevel():"+ OS.getPatchLevel());//
        // 操作系统的卖主
        logger.info("操作系统的卖主:" + OS.getVendor());
        // 卖主名称
        logger.info("操作系统的卖主名:" + OS.getVendorCodeName());
        // 操作系统名称
        logger.info("操作系统名称:" + OS.getVendorName());
        // 操作系统卖主类型
        logger.info("操作系统卖主类型:" + OS.getVendorVersion());
        // 操作系统的版本号
        logger.info("操作系统的版本号:" + OS.getVersion());
    }

    private static void who() throws SigarException {
        Sigar sigar = new Sigar();
        Who who[] = sigar.getWhoList();
        if (who != null && who.length > 0) {
            for (int i = 0; i < who.length; i++) {
                // logger.info("当前系统进程表中的用户名"+ String.valueOf(i));
                Who _who = who[i];
                logger.info("用户控制台:" + _who.getDevice());
                logger.info("用户host:" + _who.getHost());
                // logger.info("getTime():"+ _who.getTime());
                // 当前系统进程表中的用户名
                logger.info("当前系统进程表中的用户名:" + _who.getUser());
            }
        }
    }

    private static void file() throws Exception {
        Sigar sigar = new Sigar();
        FileSystem fslist[] = sigar.getFileSystemList();
        for (int i = 0; i < fslist.length; i++) {
            logger.info("分区的盘符名称" + i);
            FileSystem fs = fslist[i];
            // 分区的盘符名称
            logger.info("盘符名称:" + fs.getDevName());
            // 分区的盘符名称
            logger.info("盘符路径:" + fs.getDirName());
            logger.info("盘符标志:" + fs.getFlags());//
            // 文件系统类型，比如 FAT32、NTFS
            logger.info("盘符类型:" + fs.getSysTypeName());
            // 文件系统类型名，比如本地硬盘、光驱、网络文件系统等
            logger.info("盘符类型名:" + fs.getTypeName());
            // 文件系统类型
            logger.info("盘符文件系统类型:" + fs.getType());
            FileSystemUsage usage = null;

            usage = sigar.getFileSystemUsage(fs.getDirName());
            switch (fs.getType()) {
                case 0: // TYPE_UNKNOWN ：未知
                    break;
                case 1: // TYPE_NONE
                    break;
                case 2: // TYPE_LOCAL_DISK : 本地硬盘
                    // 文件系统总大小
                    logger.info(fs.getDevName() + "总大小:" + usage.getTotal() + "KB");
                    // 文件系统剩余大小
                    logger.info(fs.getDevName() + "剩余大小:" + usage.getFree() + "KB");
                    // 文件系统可用大小
                    logger.info(fs.getDevName() + "可用大小:" + usage.getAvail() + "KB");
                    // 文件系统已经使用量
                    logger.info(fs.getDevName() + "已经使用量:" + usage.getUsed() + "KB");
                    double usePercent = usage.getUsePercent() * 100D;
                    // 文件系统资源的利用率
                    logger.info(fs.getDevName() + "资源的利用率:" + usePercent + "%");
                    break;
                case 3:// TYPE_NETWORK ：网络
                    break;
                case 4:// TYPE_RAM_DISK ：闪存
                    break;
                case 5:// TYPE_CDROM ：光驱
                    break;
                case 6:// TYPE_SWAP ：页面交换
                    break;
            }
            logger.info(fs.getDevName() + "读出：" + usage.getDiskReads());
            logger.info(fs.getDevName() + "写入：" + usage.getDiskWrites());
        }
        return;
    }

    private static void net() throws Exception {
        Sigar sigar = new Sigar();
        String ifNames[] = sigar.getNetInterfaceList();
        for (int i = 0; i < ifNames.length; i++) {
            String name = ifNames[i];
            NetInterfaceConfig ifconfig = sigar.getNetInterfaceConfig(name);
            logger.info("网络设备名:" + name);// 网络设备名
            logger.info("IP地址:" + ifconfig.getAddress());// IP地址
            logger.info("子网掩码:" + ifconfig.getNetmask());// 子网掩码
            if ((ifconfig.getFlags() & 1L) <= 0L) {
                logger.info("!IFF_UP...skipping getNetInterfaceStat");
                continue;
            }
            NetInterfaceStat ifstat = sigar.getNetInterfaceStat(name);
            logger.info(name + "接收的总包裹数:" + ifstat.getRxPackets());// 接收的总包裹数
            logger.info(name + "发送的总包裹数:" + ifstat.getTxPackets());// 发送的总包裹数
            logger.info(name + "接收到的总字节数:" + ifstat.getRxBytes());// 接收到的总字节数
            logger.info(name + "发送的总字节数:" + ifstat.getTxBytes());// 发送的总字节数
            logger.info(name + "接收到的错误包数:" + ifstat.getRxErrors());// 接收到的错误包数
            logger.info(name + "发送数据包时的错误数:" + ifstat.getTxErrors());// 发送数据包时的错误数
            logger.info(name + "接收时丢弃的包数:" + ifstat.getRxDropped());// 接收时丢弃的包数
            logger.info(name + "发送时丢弃的包数:" + ifstat.getTxDropped());// 发送时丢弃的包数
        }
    }

    private static void ethernet() throws SigarException {
        Sigar sigar = null;
        sigar = new Sigar();
        String[] ifaces = sigar.getNetInterfaceList();
        for (int i = 0; i < ifaces.length; i++) {
            NetInterfaceConfig cfg = sigar.getNetInterfaceConfig(ifaces[i]);
            if (NetFlags.LOOPBACK_ADDRESS.equals(cfg.getAddress()) || (cfg.getFlags() & NetFlags.IFF_LOOPBACK) != 0
                    || NetFlags.NULL_HWADDR.equals(cfg.getHwaddr())) {
                continue;
            }
            logger.info(cfg.getName() + "IP地址:" + cfg.getAddress());// IP地址
            logger.info(cfg.getName() + "网关广播地址:" + cfg.getBroadcast());// 网关广播地址
            logger.info(cfg.getName() + "网卡MAC地址:" + cfg.getHwaddr());// 网卡MAC地址
            logger.info(cfg.getName() + "子网掩码:" + cfg.getNetmask());// 子网掩码
            logger.info(cfg.getName() + "网卡描述信息:" + cfg.getDescription());// 网卡描述信息
            logger.info(cfg.getName() + "网卡类型" + cfg.getType());//
        }
    }
}
