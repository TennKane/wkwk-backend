package com.wkwk.user.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用户登录VO
 */
@Data
@AllArgsConstructor
public class UserLoginVo {
    /**
     * token
     */
    private String token;
    /**
     * 用户id
     */
    private String userId;
}
