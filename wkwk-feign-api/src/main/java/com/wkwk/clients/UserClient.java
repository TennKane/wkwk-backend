package com.wkwk.clients;


import com.wkwk.interceptor.MyFeignRequestInterceptor;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.vo.UserPersonalInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务客户端接口
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@FeignClient(value = "wkwk-service-user", configuration = MyFeignRequestInterceptor.class)
public interface UserClient {

    /**
     * 获取用户个人信息
     * @param userId 用户id
     * @return ResponseResult
     */
    @GetMapping("/wkwk/user/personal")
    ResponseResult<UserPersonalInfoVo> getUserPersonalInfo(@RequestParam("userId") Long userId);
}
