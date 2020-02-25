package com.winnie.order.controller;

import com.winnie.order.dto.OrderDTO;
import com.winnie.order.dto.OrderVO;
import com.winnie.order.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity<Long> saveOrder(@RequestBody OrderDTO orderDTO){
        Long orderId = orderService.saveOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(orderId);
    }
    /**
     * 查询订单信息
     */
    @GetMapping("/order/{id}")
    public ResponseEntity<OrderVO> findOrderVoById(@PathVariable("id") Long id){
        OrderVO orderVO = orderService.findOrderVoById(id);
        return ResponseEntity.ok(orderVO);
    }

    @GetMapping("/order/url/{id}")
    public ResponseEntity<String> getPayUrl(@PathVariable("id") Long id){
        String payUrl = orderService.getPayUrl(id);
        return ResponseEntity.ok(payUrl);
    }
    /**
     * 查询支付状态
     */
    @GetMapping("/order/state/{id}")
    public ResponseEntity<Integer> getPayCode(@PathVariable("id") Long id){
        Integer status = orderService.getPayCode(id);
        return ResponseEntity.ok(status);
    }

    @PostMapping(value = "/pay/wx/notify",produces = "application/xml")
    public Map<String, String> notifyWx(@RequestBody Map<String, String> paramMap){
        log.info("【微信来通知我们了！】处理器开始了！！！！！！！！！！！！！");
        orderService.checkWxNotify(paramMap);
        Map<String, String> result = new HashMap<>();
        result.put("return_code", "SUCCESS");
        result.put("return_msg", "OK");
        log.info("【微信来通知我们了！】处理器结束了！！！！！！！！！！！！！");
        return result;
    }

}
