package com.winnie.page.listener;

import com.winnie.common.constants.MQConstant;
import com.winnie.page.Service.PageService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class RabbitMQListener {
    @Autowired
    private PageService pageService;

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = MQConstant.Queue.PAGE_ITEM_UP,durable = "true"),
            exchange = @Exchange(value = MQConstant.Exchange.ITEM_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = MQConstant.RoutingKey.ITEM_UP_KEY))
    public void addStaticPage(Long id){
        pageService.createItemStaticPage(id);
    }

    @RabbitListener(
            bindings = @QueueBinding(value = @Queue(value = MQConstant.Queue.PAGE_ITEM_DOWN,durable = "true"),
            exchange = @Exchange(value = MQConstant.Exchange.ITEM_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = MQConstant.RoutingKey.ITEM_DOWN_KEY
    ))
    public void deleStaticPage(Long id){
        pageService.deleStaticPage(id);
    }
}
