package com.zhengyuan.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class MessageService {

    @Autowired
    private KafkaTemplate kafkaTemplate;

    //发送消息方法
    public void send(String msg) {

        //System.out.println("produceMsg"+msg);
       // kafkaTemplate.send("p1",msg);
    }

}
