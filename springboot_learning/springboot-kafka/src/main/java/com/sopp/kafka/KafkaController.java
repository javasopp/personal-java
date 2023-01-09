package com.sopp.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sopp
 * @version 1.0
 * @date 2021/6/17 10:36
 */
@RestController
public class KafkaController {
    @Autowired
    private UserLogProducer userLogProducer;

    @GetMapping("/send/{msg}")
    public void sendMsg(@PathVariable("msg") String msg) {
        userLogProducer.send(msg);
    }
}
