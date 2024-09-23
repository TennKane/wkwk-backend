package com.gtkang.feignapi.clients;

import com.wkwk.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 交互服务客户端接口
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@FeignClient("wkwk-service-interact")
public interface InteractClient {

    /**
     * 获取关注总数
     * @param userId 用户id
     * @return 关注总数
     */
    @GetMapping("/wkwk/interact/follow/num")
    ResponseResult<Integer> getFollowNum(@RequestParam("userId") Long userId);

    /**
     * 获取粉丝总数
     * @param userId 用户id
     * @return 粉丝总数
     */
    @GetMapping("/wkwk/interact/fansNum")
    ResponseResult<Integer> getFansNum(@RequestParam("userId") Long userId);

    /**
     * 判断是否关注
     * @param firstUser 第一个用户
     * @param secondUser 第二个用户
     * @return 是否关注
     */
    @GetMapping("/wkwk/interact/follow/ifFollow")
    ResponseResult<Boolean> ifFollow(@RequestParam("firstUser") Long firstUser,
                                     @RequestParam("secondUser") Long secondUser);
}
