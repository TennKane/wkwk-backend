package com.wkwk.user.dto;

import lombok.Data;

import java.util.Objects;


@Data
public class UserRegisterDto {
    /**
     * 手机号
     */
    public String phone;
    /**
     * 密码
     */
    public String password;
}
