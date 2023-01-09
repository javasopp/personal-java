package com.mds.business.controller;

import com.mds.business.common.message.Message;
import com.mds.business.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * description : 客户端相关操作
 * @author sopp
 * @version 1.0
 * @date 2021/11/22 09:39
 */
@RequestMapping("/client")
@RestController
public class ClientController {

    @Autowired
    private ClientService clientService;

    /**
     * 获取所有在线的设备信息。
     * @return
     */
    @GetMapping("/online-client")
    public Message getAllOnlineClient() {
        return clientService.getAllOnlineClient();
    }

}
