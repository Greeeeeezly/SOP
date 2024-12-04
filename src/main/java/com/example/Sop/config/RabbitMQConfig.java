package com.example.Sop.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue priorityQueue() {
        return new Queue("priorityQueue", true);
    }

    @Bean
    public Queue defaultQueue() {
        return new Queue("defaultQueue", true);
    }

    @Bean
    public Queue wsPriorityQueue(){ return new Queue("wsPriorityQueue", true);}

    @Bean
    public Queue wsDefaultQueue(){ return new Queue("wsDefaultQueue", true);}

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("notificationExchange");
    }

    @Bean
    public Binding bindingPriority(TopicExchange exchange, Queue priorityQueue) {
        return BindingBuilder.bind(priorityQueue).to(exchange).with("notification.priority");
    }

    @Bean
    public Binding bindingDefault(TopicExchange exchange, Queue defaultQueue) {
        return BindingBuilder.bind(defaultQueue).to(exchange).with("notification.default");
    }

    @Bean
    public Binding bindingPriorityWS(TopicExchange exchange, Queue wsPriorityQueue) {
        return BindingBuilder.bind(wsPriorityQueue).to(exchange).with("notification.wspriority");
    }

    @Bean
    public Binding bindingDefaultWS(TopicExchange exchange, Queue wsDefaultQueue) {
        return BindingBuilder.bind(wsDefaultQueue).to(exchange).with("notification.wsdefault");
    }
}


