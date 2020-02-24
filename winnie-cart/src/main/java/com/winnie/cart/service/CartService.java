package com.winnie.cart.service;

import com.winnie.cart.pojo.Cart;
import com.winnie.common.auth.pojo.UserHolder;
import com.winnie.common.constants.WNConstant;
import com.winnie.common.utils.BeanHelper;
import com.winnie.common.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    @Autowired
    private StringRedisTemplate redisTemplate;


    public void addCart(Cart cart) {
        Long userId = UserHolder.getUserId();
        String key = WNConstant.CART_PRE + userId;
        BoundHashOperations<String, String, String> redisHash = redisTemplate.boundHashOps(key);
        String hashKey = cart.getSkuId().toString();
        if (redisHash.hasKey(hashKey)){
            Cart redisCart = JsonUtils.toBean(redisHash.get(hashKey), Cart.class);
            cart.setNum(redisCart.getNum()+cart.getNum());
        }
        redisHash.put(hashKey, JsonUtils.toString(cart));
    }

    public List<Cart> queryCarts() {
        Long userId = UserHolder.getUserId();
        String key = WNConstant.CART_PRE + userId;
        BoundHashOperations<String, String, String> redisHash = redisTemplate.boundHashOps(key);
        if (redisHash != null){
            Map<String, String> entries = redisHash.entries();
            List<Cart> collect = entries.values().stream()
                    .map(cartStr -> JsonUtils.toBean(cartStr, Cart.class))
                    .collect(Collectors.toList());
            return collect;
        }
        return null;
    }

    public void addCarts(List<Cart> carts) {
        for (Cart cart : carts) {
            addCart(cart);
        }
    }
}
