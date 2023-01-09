package com.sopp.all.util;

import com.sopp.all.common.constats.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author: sopp
 * @Description: 公共的工具类
 * @Date: Created in 09:38 2020/5/7
 * @Modified By:
 */
@Slf4j
@Component
public class MyUtils {
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
     *  * ip地址转成long型数字
     *  * 将IP地址转化成整数的方法如下：
     *  * 1、通过String的split方法按.分隔得到4个长度的数组
     *  * 2、通过左移位操作（<<）给每一段的数字加权，第一段的权为2的24次方，第二段的权为2的16次方，第三段的权为2的8次方，最后一段的权为1
     *  * @param strIp
     *  * @return
     *  
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
}
