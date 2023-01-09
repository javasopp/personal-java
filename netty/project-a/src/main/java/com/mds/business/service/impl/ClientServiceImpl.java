package com.mds.business.service.impl;

import com.mds.business.common.message.Message;
import com.mds.business.config.netty.TcpConnector;
import com.mds.business.service.ClientService;
import com.mds.business.vo.ClientStatusVo;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 客户端实现类。
 * @author sopp
 * @version 1.0
 * @date 2021/11/22 09:49
 */
@Slf4j
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private TcpConnector tcpConnector;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Message getAllOnlineClient() {
        Map<String, Channel> map = tcpConnector.getMap();
        List<ClientStatusVo> list = new ArrayList<>();
        for(HashMap.Entry<String,Channel> entry : map.entrySet()) {
            // redis 的key的格式：客户端类型：A/B + 知识库类型 + 客户端名称 + 当前是否临时
            String key = entry.getKey();
            String[] redisKeys = key.split("-");
            int clientType = Integer.parseInt(redisKeys[0]);
            int type = Integer.parseInt(redisKeys[1]);
            String name = redisKeys[2];
            // 当前是否临时连接：0-否；1-是；
            int status = Integer.parseInt(redisKeys[3]);


            ClientStatusVo clientStatusVo = new ClientStatusVo();
            clientStatusVo.setName(name);
            clientStatusVo.setType(type);
            clientStatusVo.setRedisKey(key);
            list.add(clientStatusVo);
        }
        return list.isEmpty()?Message.noData():Message.success(list);
    }
}
