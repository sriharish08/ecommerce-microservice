package com.sri.notification_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // =========================
    // Email Notification
    // =========================

    public static final String EMAIL_QUEUE = "email.notification.queue";
    public static final String EMAIL_EXCHANGE = "notification.exchange";
    public static final String EMAIL_ROUTING_KEY = "notification.email";

    // =========================
    // Order Placed
    // =========================

    public static final String ORDER_EXCHANGE = "order.exchange";
    public static final String ORDER_PLACED_QUEUE = "order.placed.queue";
    public static final String ORDER_PLACED_ROUTING_KEY = "order.placed";

    // =========================
    // Daily Order Report
    // =========================

    public static final String DAILY_ORDER_REPORT_QUEUE = "daily.order.report.queue";
    public static final String DAILY_ORDER_REPORT_ROUTING_KEY = "daily.order.report";

    // =========================
    // Email Queue
    // =========================

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    @Bean
    public TopicExchange emailExchange() {
        return new TopicExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    // =========================
    // Order Placed Queue
    // =========================

    @Bean
    public Queue orderPlacedQueue() {
        return new Queue(ORDER_PLACED_QUEUE, true);
    }

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Binding orderPlacedBinding() {
        return BindingBuilder.bind(orderPlacedQueue())
                .to(orderExchange())
                .with(ORDER_PLACED_ROUTING_KEY);
    }

    // =========================
    // Daily Order Report Queue
    // =========================

    @Bean
    public Queue dailyOrderReportQueue() {
        return new Queue(DAILY_ORDER_REPORT_QUEUE, true);
    }

    @Bean
    public Binding dailyOrderReportBinding() {
        return BindingBuilder.bind(dailyOrderReportQueue())
                .to(orderExchange())
                .with(DAILY_ORDER_REPORT_ROUTING_KEY);
    }

    // =========================
    // Message Converter
    // =========================

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}