package com.mds.auth.config.start;

import com.mds.business.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 开机清空redis
 * @author sopp
 * @version 1.0
 * @date 2021/9/16 10:03
 */
@Slf4j
@Component
public class BeforeStartApplicationHandler implements CommandLineRunner {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void run(String... args) {
        Set<String> keys = redisUtil.scan("*");
        List<String> list = new ArrayList<>(keys);
        redisUtil.del(list);
        log.info("redis清空成功");
    }
}
