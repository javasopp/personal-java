package com.mds.business.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.mds.business.common.message.Message;
import com.mds.business.service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/11/9 10:44
 */
@Slf4j
@Service
public class TestServiceImpl implements TestService {
    @Override
    public Message requestLineResource(String data) {
        JSONObject json = new JSONObject();
        json.put("test1", 1);
        json.put("test2", 1);
        json.put("test3", 1);
        json.put("test4", 1);
        return Message.success(json);
    }
}
