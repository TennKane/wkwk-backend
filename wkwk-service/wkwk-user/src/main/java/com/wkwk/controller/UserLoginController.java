package com.wkwk.controller;


import com.wkwk.service.UserLoginService;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.dto.UserRegisterDto;
import com.wkwk.user.dto.UserLoginDto;
import com.wkwk.user.vo.UserLoginVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 用户登录控制器
 */
@RestController
@RequestMapping("/wkwk/user/login")
public class UserLoginController {

    @Resource
    private UserLoginService userLoginService;
    /**
     * 用户注册
     * @param userRegisterDto 注册信息
     * @return 登录结果
     */
    @PostMapping("/register")
    public ResponseResult register(UserRegisterDto userRegisterDto) {
        return userLoginService.register(userRegisterDto);
    }

    /**
     * 用户登录
     * @param userLoginDto 登录信息
     * @return 登录结果
     */
    @PostMapping
    public ResponseResult<UserLoginVo> userLogin(UserLoginDto userLoginDto) {
        return userLoginService.userLogin(userLoginDto);
    }
}
