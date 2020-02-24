package com.winnie.user.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import tk.mybatis.mapper.annotation.KeySql;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;


import java.util.Date;

@Data
@Table(name = "tb_user")
public class User {
    @Id
    @KeySql(useGeneratedKeys = true)
    private Long id;
    @Size(min = 4,max = 16,message = "用户名个数不对")
    private String username;
    @Length(min = 4,max = 30,message = "密码格式不对")
    private String password;
    private String phone;
    private Date createTime;
    private Date updateTime;
}
