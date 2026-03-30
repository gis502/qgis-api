package com.ruoyi.system.core.config;

import com.ruoyi.common.constant.BaseConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @ClassName RabbitConfig
 * @Description RabbitMq 配置
 * @Author Huang Yx
 * @Date 2026/2/12 16:52
 */
@Slf4j
@Configuration
public class RabbitConfig {


    // 专题图队列
    @Bean
    public Queue mapsQueue() {
        return new Queue(BaseConstants.MAPS_QUEUE, true);
    }

    // 文档队列
    @Bean
    public Queue documents() {
        return new Queue(BaseConstants.DOCUMENTS_QUEUE, true);
    }

    // 死信队列
    @Bean
    public Queue dlqQueue() {
        return new Queue(BaseConstants.DLQ_QUEUE, true);
    }

    // 定义主题交换机
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(BaseConstants.ASSESS_EXCHANGE);
    }

    // 定义死信交换机
    @Bean
    public DirectExchange  dlxExchange() {
        return new DirectExchange (BaseConstants.DLX_EXCHANGE);
    }
    // 将 mapsQueue 队列和 assess 交换机绑定,而且绑定的键值为 maps
    // 这样只要是消息携带的路由键是 maps,才会分发到该队列
    @Bean
    public Binding bindingExchangeMessageOfMaps() {
        return BindingBuilder.bind(mapsQueue()).to(exchange()).with(BaseConstants.MAPS_QUEUE);
    }

    @Bean
    public Binding bindingExchangeMessageOfDocuments() {
        return BindingBuilder.bind(documents()).to(exchange()).with(BaseConstants.DOCUMENTS_QUEUE);
    }

    @Bean
    public Binding bindingExchangeMessageOfFiles() {
        return BindingBuilder.bind(dlqQueue()).to(exchange()).with(BaseConstants.DLQ_QUEUE);
    }

    // 设置消息回调函数 自动确认消息 ack
    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        // 设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//                log.info("消息回调信息：correlationData-{}，确认状态-{}，cause-{}", correlationData, ack, cause);
            }
        });

        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//                log.info("回调参数：message-{}，replyCode-{}，replyText-{}，exchange-{}，routingKey-{}" + message, replyCode, replyText, exchange, routingKey);
            }
        });

        return rabbitTemplate;
    }
}
