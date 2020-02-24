package com.winnie.user.service;

import com.netflix.discovery.converters.Auto;
import com.winnie.common.constants.MQConstant;
import com.winnie.common.constants.WNConstant;
import com.winnie.common.exception.pojo.ExceptionEnum;
import com.winnie.common.exception.pojo.WNException;
import com.winnie.user.entity.User;
import com.winnie.user.mapper.UserMapper;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Service
@Transactional
public class UserService {
    @Autowired(required = false)
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public Boolean checkUserNameOrPhone(String data, Integer type) {
        User user = new User();
        switch (type){
            case 1:
                user.setUsername(data);
                break;
            case 2:
                user.setPhone(data);
                break;
            default:
                throw new WNException(ExceptionEnum.INVALID_PARAM_ERROR);

        }
        int i = userMapper.selectCount(user);
        return i==0;
    }

    public void sendVcodeSms(String phone) {
        String key = WNConstant.CHECK_CODE_PRE+phone;
        String code = RandomStringUtils.randomNumeric(4);

        redisTemplate.opsForValue().set(key,code,24, TimeUnit.HOURS);

        HashMap<String, String> map = new HashMap<>();
        map.put("phone",phone);
        map.put("code",code);

        amqpTemplate.convertAndSend(MQConstant.Exchange.SMS_EXCHANGE_NAME,
                MQConstant.RoutingKey.VERIFY_CODE_KEY,
                map);
    }

    public void register(User user, String code) {
        String redisCode = (String) redisTemplate.opsForValue().get(WNConstant.CHECK_CODE_PRE + user.getPhone());
        if (!StringUtils.equals(redisCode,code)){
            throw new WNException(ExceptionEnum.INVALID_VERIFY_CODE);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        int i = userMapper.insertSelective(user);
        if (i == 0){
            throw new WNException(ExceptionEnum.INSERT_OPERATION_FAIL);
        }
    }

    public User queryUserWithNameAndPasword(String username, String password) {
        User record = new User();
        record.setUsername(username);
        User user = userMapper.selectOne(record);
        if (user == null){
            throw new WNException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        if (!passwordEncoder.matches(password,user.getPassword())){
            throw new WNException(ExceptionEnum.INVALID_USERNAME_PASSWORD);
        }
        return user;
    }
}
