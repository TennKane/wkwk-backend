package com.wkwk.user.dto;

import lombok.Data;


@Data
public class UserLoginDto {
    /**
     * 手机号
     */
    public String phone;
    /**
     * 密码
     */
    public String password;
}
