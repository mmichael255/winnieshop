package com.winnie.cart.controller;

import com.winnie.cart.pojo.Cart;
import com.winnie.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CartController {
    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Void> addCart(@RequestBody Cart cart){
        cartService.addCart(cart);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<Cart>> queryCarts(){
        List<Cart> carts = cartService.queryCarts();
        return ResponseEntity.ok(carts);
    }

    @PostMapping("/list")
    public ResponseEntity<Void> addCarts(@RequestBody List<Cart> carts){
        cartService.addCarts(carts);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
