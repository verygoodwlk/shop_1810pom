package com.qf.shop_service_goods;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    public static final String FANOUT_NAME = "goods_fanoutexchange";

    /**
     * 声称两个队列
     * @return
     */
    @Bean
    public Queue getQueue1(){
        return new Queue("goods_queue1");
    }

    @Bean
    public Queue getQueue2(){
        return new Queue("goods_queue2");
    }

    /**
     * 声明交换机
     * @return
     */
    @Bean
    public FanoutExchange getFanoutExchange(){
        return new FanoutExchange(FANOUT_NAME);
    }

    /**
     * 将队列绑定到交换机
     */
    @Bean
    public Binding getBindingBuilder1(Queue getQueue1, FanoutExchange getFanoutExchange){
        return BindingBuilder.bind(getQueue1).to(getFanoutExchange);
    }

    @Bean
    public Binding getBindingBuilder2(Queue getQueue2, FanoutExchange getFanoutExchange){
        return BindingBuilder.bind(getQueue2).to(getFanoutExchange);
    }
}
