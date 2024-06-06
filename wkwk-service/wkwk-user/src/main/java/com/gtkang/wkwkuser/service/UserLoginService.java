package com.gtkang.wkwkuser.service;



import com.wkwk.response.ResponseResult;
import com.wkwk.user.dto.RegisterDto;
import com.wkwk.user.dto.UserLoginDto;
import org.springframework.stereotype.Service;

/**
 * 用户登录服务接口
 */
@Service
public interface UserLoginService {
    /**
     * 用户注册
     * @param registerDto 注册信息
     * @return 注册结果
     */
    ResponseResult register(RegisterDto registerDto);

    /**
     * 用户登录
     * @param userLoginDto 登录信息
     * @return 登录结果
     */
    ResponseResult userLogin(UserLoginDto userLoginDto);
}
