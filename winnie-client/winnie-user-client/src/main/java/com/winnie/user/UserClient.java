package com.winnie.user;

import com.winnie.order.dto.AddressDTO;
import com.winnie.user.entity.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
@FeignClient(name = "user-service")
public interface UserClient {

    @PostMapping("/query")
    public User query(@RequestParam("username")String username,
                      @RequestParam("password")String password);

    @GetMapping("/address")
    public AddressDTO queryAddressById(@RequestParam("userId") Long userId, @RequestParam("id") Long id);
}
