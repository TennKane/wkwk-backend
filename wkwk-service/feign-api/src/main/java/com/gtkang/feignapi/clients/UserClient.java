package com.gtkang.feignapi.clients;

import com.wkwk.model.ResponseResult;
import com.wkwk.user.vo.UserPersonalInfoVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务客户端接口
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@FeignClient("azaz-service-user")
public interface UserClient {

    /**
     * 获取用户个人信息
     * @param userId 用户id
     * @return ResponseResult
     */
    @GetMapping("/azaz/user/personal")
    ResponseResult<UserPersonalInfoVo> getUserPersonalInfo(@RequestParam("userId") Long userId);
}
