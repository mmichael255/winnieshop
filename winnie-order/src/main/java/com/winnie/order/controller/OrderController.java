package com.winnie.order.controller;

import com.winnie.order.dto.OrderDTO;
import com.winnie.order.dto.OrderVO;
import com.winnie.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
