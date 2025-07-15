package com.example.User.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    public void sendMessage(EmailRequest message) {
        rabbitTemplate.convertAndSend(RabbitConfig.QUEUE, message);
        System.out.println("Sending message: " + message.getTo());
    }
}
