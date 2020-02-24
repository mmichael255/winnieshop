package com.winnie.user.controller;

import com.winnie.common.exception.pojo.WNException;
import com.winnie.user.entity.User;
import com.winnie.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.xml.ws.Response;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserNameOrPhone(@PathVariable("data")String data,
                                                        @PathVariable("type")Integer type){
        Boolean isTrue = userService.checkUserNameOrPhone(data,type);
        return ResponseEntity.ok(isTrue);
    }

    @PostMapping("/code")
    public ResponseEntity<Void> sendVcodeSms(@RequestParam("phone")String phone){
        userService.sendVcodeSms(phone);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid User user, BindingResult result, @RequestParam("code")String code){
        if (result.hasErrors()){
            String collect = result.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining("|"));
            throw new WNException(501,collect);
        }
        userService.register(user,code);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/query")
    public ResponseEntity<User> query(@RequestParam("username")String username,
                                      @RequestParam("password")String password){
        User user = userService.queryUserWithNameAndPasword(username,password);
        return ResponseEntity.ok(user);
    }
}
