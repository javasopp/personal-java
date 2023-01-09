package com.mds.business.service;

import com.mds.business.common.message.Message;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/22 09:45
 */
public interface ClientService {

    /**
     * 获取当前所有在线的A客户端信息
     * @return
     */
    Message getAllOnlineClient();
}
