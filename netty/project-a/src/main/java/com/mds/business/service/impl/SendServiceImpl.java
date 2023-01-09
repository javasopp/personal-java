package com.mds.business.service.impl;

import com.mds.business.config.netty.TcpConnector;
import com.mds.business.service.SendService;
import com.mds.business.DTO.SendInfoDto;
import com.mds.business.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/3 14:31
 */
@Slf4j
@Service
public class SendServiceImpl implements SendService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private TcpConnector tcpConnector;

    /**
     * 推送信息到当前连接的各种客户端进行同步。
     *
     * @param sendInfoDto
     * @return
     */
    @Override
    public int sendToOtherDevice(SendInfoDto sendInfoDto) {
        // 获取info中的内容
        int type = sendInfoDto.getType();
        String info = sendInfoDto.getSendInfo();

        // 获取当前redis中，这种类型的客户端
        Set<String> clients = redisUtil.scan(type + "-*");

        // 遍历获取所有的客户端的key，然后去对应的map找到对应的发送连接，然后进行下发
        if (clients.size() < 1) {
            return 0;
        }
        List<String> listClient = new ArrayList<>(clients);
        tcpConnector.sendToOtherClient(listClient, info);
        return 1;
    }

    /**
     * 根据redis key的推送到。
     * @param sendInfoDto
     */
    @Override
    public void sendToOtherDeviceByRedisKey(SendInfoDto sendInfoDto) {
        tcpConnector.sendToOtherClientByRedisKey(sendInfoDto.getRedisKey(), sendInfoDto.getSendInfo());
    }
}
