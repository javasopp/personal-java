package com.sopp.all.config.mq.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName: RabbitmqConfig
 * @Description: rabbit mq configuration class
 * @Author: Sopp
 * @Date: 2019/7/5 10:18
 **/
@Configuration
@Slf4j
public class RabbitmqConfig {

    /*------------------定义第4个队列------------------*/
    /**
     * 鉴权请求队列
     */
    public final static String QUEUE_TEST = "test";

    @Bean("testQueue")
    public Queue testQueue() {
        return new Queue(QUEUE_TEST, false);
    }

    /**
     * mq配置json格式转换
     * @return
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}