package de.hfu.cnc.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
    /*
     * Two predefined values in the application.yaml:
     * routingKey -> weigoldl-kubecoin
     * queueName -> cnc.weigoldl-kubecoin
     */
    @Value("${frontend.routingKey}")
    private String routingKey;

    @Value("${frontend.queue}")
    private String queueName;


    @Bean
    public DirectExchange direct() {
        return new DirectExchange("cnc.direct");
    }

    @Bean
    public Queue queue() {
        Queue queue = new Queue(queueName);
        queue.addArgument("x-queue-type", "quorum");
        return queue;
    }

    @Bean
    public Binding binding1(DirectExchange direct, Queue queue) {
        return BindingBuilder.bind(queue).to(direct).with(routingKey);
    }

}
