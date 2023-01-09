package com.mds.business.util;

import cn.hutool.crypto.digest.MD5;
import com.mds.business.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.List;

/**
 * @Author: sopp
 * @Description: 公共的工具类
 * @Date: Created in 09:38 2020/5/7
 * @Modified By:
 */
@Slf4j
@Component
public class MyUtils {

    /**
     * Description: 获取byte实际大小
     *
     * @param bytes
     * @param length
     * @return byte[]
     * @throws
     * @auther Sopp
     * @date: 2019/12/10 16:28
     */
    public byte[] getCopyByte(byte[] bytes, int length) {
        if (null == bytes || 0 == bytes.length) {
            return new byte[1];
        }
        byte[] bb = new byte[length];
        System.arraycopy(bytes, 0, bb, 0, length);
        return bb;
    }


    /**
     * Description: 返回ip
     *
     * @param
     * @return int
     * @throws
     * @auther Sopp
     * @date: 2019/7/25 10:20
     */
    public int getPort() throws IOException {
        ServerSocket serverSocket = new ServerSocket();
        int port = serverSocket.getLocalPort();
        return port;
    }

    public String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || Constants.UNKNOW.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || Constants.UNKNOW.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }
            if (ipAddress == null || ipAddress.length() == 0 || Constants.UNKNOW.equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();
                if (ipAddress.equals(Constants.LOCALHOST) || ipAddress.equals(Constants.OTHER_IP)) {
                    // 根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    if (inet != null) {
                        ipAddress = inet.getHostAddress();
                    }
                }
            }
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            if (ipAddress != null && ipAddress.length() > Constants.IPUTIL_NUMBER) {
                if (ipAddress.indexOf(Constants.COMMA) > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }
        return ipAddress;
    }

    /**
     * MD5 加密
     *
     * @param salt        盐
     * @param msg         需要加密的明文
     * @param digestCount 加密次数
     * @return
     * @throws Exception
     */
    public String md5Hex(String salt, String msg, int digestCount) {
        MD5 md = new MD5();
        String hex = msg;
        for (int i = 0; i < digestCount; i++) {
            hex = md.digestHex(hex + ":" + salt);
        }
        return hex;
    }



    /**
     * ip地址转成long型数字
     * 将IP地址转化成整数的方法如下：
     * 1、通过String的split方法按.分隔得到4个长度的数组
     * 2、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
     * @paramstrIp
     * @return
     */
    public Long ipConvertLong(String strIp) {
        String[] ip = strIp.split("\\.");
        return (Long.parseLong(ip[0]) << 24) + (Long.parseLong(ip[1]) << 16) + (Long.parseLong(ip[2]) << 8) + Long.parseLong(ip[3]);
    }


    /**
     * long->ip
     * 1.采用StringBuffer方便字符串拼接。
     * 2.ip第一位：整数直接右移24位。
     * ip第二位：整数先高8位置0.再右移16位。
     * ip第三位：整数先高16位置0.再右移8位。
     * ip第四位：整数高24位置0.
     * 3.将他们用分隔符拼接即可。
     *
     * @param longIp
     * @return
     */
    public String longConvertIp(long longIp) {
        //采用SB方便追加分隔符 "."
        StringBuffer sb = new StringBuffer("");
        sb.append(String.valueOf(longIp >> 24)).append(".").
                append(String.valueOf((longIp & 0x00ffffff) >> 16)).append(".").
                append(String.valueOf((longIp & 0x0000ffff) >> 8)).append(".").
                append(String.valueOf(longIp & 0x000000ff));
        return sb.toString();
    }


    /**
     * 判断是否 ping 通
     *
     * @param ipAddress
     * @param pingTimes
     * @return
     */
    public boolean ping(String ipAddress, int pingTimes) {
        BufferedReader in = null;
        // 将要执行的ping命令,此命令是windows格式的命令
        Runtime r = Runtime.getRuntime();
        String pingCommand = "ping -c 5 " + ipAddress;
        try {
            // 执行命令并获取输出
            Process p = r.exec(pingCommand);
            if (p == null) {
                return false;
            }
            // 逐行检查输出,计算类似出现=23ms TTL=62字样的次数
            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            int connectedCount = 0;
            String line = null;
            while ((line = in.readLine()) != null) {
                connectedCount += getCheckResult(line);
            }
            // 如果出现 64 bytes 这样的字样，相加大于0，就表示能通
            return connectedCount > 0;
        } catch (Exception ex) {
            // 出现异常则返回假
            ex.printStackTrace();
            return false;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 若line含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
     *
     * @param line
     * @return
     */
    private int getCheckResult(String line) {
        if (line.startsWith("64 bytes")) {
            return 1;
        }
        return 0;
    }


    /**
     * 封装判断一个字符串是否包含在另一个集合中的方法
     * @param string
     * @param list
     * @return
     */
    public boolean contain(String string, List<String> list) {
        if (list.contains(string)) {
            return true;
        }
        for (String s : list) {
            String[] split = string.split(s);
            if (split.length == 1) {
                continue;
            }
            boolean result = true;
            for (String str : split) {
                boolean contain = contain(str, list);
                if (!contain) {
                    result = false;
                    break;
                }
            }
            if (result) {
                return result;
            }
        }
        return false;
    }
}
