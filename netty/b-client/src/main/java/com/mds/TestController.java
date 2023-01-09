package com.mds;

import com.alibaba.fastjson.JSONObject;
import com.mds.config.netty.NettyClient;
import com.mds.util.Aes256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/9 16:13
 */
@Slf4j
@RestController
public class TestController {

    @Value("${client.type}")
    private int type;

    @Value("${client.name}")
    private String name;

    @Value("${client.status}")
    private int status;

    @Value("${client.knowledge}")
    private int knowledge;

    @Autowired
    private NettyClient nettyClient;

    @Autowired
    private Aes256Util aes256Util;

    @GetMapping("/test")
    public void sendInfo() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("key", 3);
        jsonObject.put("clientType", type);
        jsonObject.put("clientName", name);
        jsonObject.put("status", status);
        jsonObject.put("knowledgeType", knowledge);
        JSONObject data = new JSONObject();
        data.put("test1", "test1");
        data.put("test2", "test2");
        data.put("test3", "test3");
        data.put("test4", "test4");
        data.put("test5", "test5");
        jsonObject.put("data", data);
        nettyClient.sendInfoToServer(aes256Util.encode(jsonObject.toJSONString()));
    }
}
