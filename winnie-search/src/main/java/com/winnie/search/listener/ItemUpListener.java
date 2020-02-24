package com.winnie.search.listener;

import com.winnie.common.constants.MQConstant;
import com.winnie.search.service.GoodsService;
import org.elasticsearch.search.SearchService;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ItemUpListener {
    @Autowired
    private GoodsService goodsService;

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstant.Queue.SEARCH_ITEM_UP,durable = "true"),
            exchange = @Exchange(value = MQConstant.Exchange.ITEM_EXCHANGE_NAME,type = ExchangeTypes.TOPIC),
            key = MQConstant.RoutingKey.ITEM_UP_KEY
    ))
    public void addSearchIndex(Long id){
        goodsService.addIndex(id);
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MQConstant.Queue.SEARCH_ITEM_DOWN,durable = "true"),
            exchange = @Exchange(value = MQConstant.Exchange.ITEM_EXCHANGE_NAME, type = ExchangeTypes.TOPIC),
            key = MQConstant.RoutingKey.ITEM_DOWN_KEY
    ))
    public void deleSearchIndex(Long id){
        goodsService.deleIndex(id);
    }
}
