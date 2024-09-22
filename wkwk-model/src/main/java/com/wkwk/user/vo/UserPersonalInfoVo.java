package com.wkwk.user.vo;

import lombok.Data;

/**
 * 用户个人信息vo类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class UserPersonalInfoVo {

    /**
     * 用户id
     */
    private String id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 头像
     */
    private String image;

    /**
     * 签名
     */
    private String signature;
}
