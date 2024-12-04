package com.example.ws;

import com.example.ws.services.NotificationService;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WsApplication {

    static final String priorityQueue = "wsPriorityQueue";
    static final String defaultQueue = "wsDefaultQueue";
    @Autowired
    private NotificationService notificationService;
    @Bean
    public Queue priorityQueue() {
        return new Queue(priorityQueue, true);
    }

    @Bean
    public Queue defaultQueue() {
        return new Queue(defaultQueue, true);
    }

    @RabbitListener(queues = priorityQueue)
    public void listenPriority(String message) {
        System.out.println("Message read from priorityQueue : " + message);
        notificationService.sendNotification("message from priority queue " + message);
    }

    @RabbitListener(queues = defaultQueue)
    public void listenDefault(String message) {
        System.out.println("Message read from defaultQueue : " + message);
        notificationService.sendNotification("message from default " + message);
    }
    public static void main(String[] args) {
        SpringApplication.run(WsApplication.class, args);
    }
}
