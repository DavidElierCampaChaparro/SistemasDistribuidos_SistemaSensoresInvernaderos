package com.greenhouse.analytics_service_test.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE = "analytics.queue";

    @Bean
    public Queue analyticsQueue() {
        return new Queue(QUEUE, true);
    }

    @Bean
    public FanoutExchange sensorDataExchange() {
        return new FanoutExchange("sensor.data.exchange");
    }

    @Bean
    public Binding analyticsBinding(Queue analyticsQueue, FanoutExchange sensorDataExchange) {
        return BindingBuilder.bind(analyticsQueue).to(sensorDataExchange);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}