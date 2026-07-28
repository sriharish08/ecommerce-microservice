package com.sri.order_service.common.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String ORDER_EXCHANGE = "order.exchange";

    public static final String ORDER_PLACED_ROUTING_KEY = "order.placed";

    public static final String DAILY_ORDER_REPORT_QUEUE = "daily.order.report.queue";
    public static final String DAILY_ORDER_REPORT_ROUTING_KEY = "daily.order.report";

    @Bean
    public TopicExchange orderExchange() {
        return new TopicExchange(ORDER_EXCHANGE);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue dailyOrderReportQueue() {
        return new Queue(DAILY_ORDER_REPORT_QUEUE);
    }

    @Bean
    public Binding dailyOrderReportBinding(Queue dailyOrderReportQueue,
                                           TopicExchange orderExchange) {

        return BindingBuilder.bind(dailyOrderReportQueue)
                .to(orderExchange)
                .with(DAILY_ORDER_REPORT_ROUTING_KEY);
    }
}
