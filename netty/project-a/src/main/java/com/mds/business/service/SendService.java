package com.mds.business.service;

import com.mds.business.DTO.SendInfoDto;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/3 14:27
 */
public interface SendService {

    /**
     * 主动发送信息到其他的客户端或者B
     * @param sendInfoDto
     * @return
     */
    int sendToOtherDevice(SendInfoDto sendInfoDto);

    /**
     * 发送到其他的客户端，通过redis key，也就是唯一标识
     * @param sendInfoDto
     */
    void sendToOtherDeviceByRedisKey(SendInfoDto sendInfoDto);
}
