package com.sopp.all.config.schedule;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TestSchedule
 * @Description:
 * @Author: Sopp
 * @Date: 2019/11/18 14:57
 **/
@Component
@Slf4j
public class TestSchedule {

    @Autowired
    private RabbitTemplate template;

    @Scheduled(fixedDelay = 1000 * 20)
    public void sendHeartToManager() {
//        template.convertAndSend(RabbitmqConfig.TEST, json);
    }
}