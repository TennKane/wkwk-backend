package com.gtkang.feignapi.clients;


import com.wkwk.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 视频客户端
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public interface VideoClient {
    /**
     * 检查两个用户是否互相关注
     * @param firstUserId 第一个用户id
     * @param secondUserId 第二个用户id
     * @return ResponseResult
     */
    @GetMapping("/azaz/video/follow/checkIfFollowEachOther")
    ResponseResult<Boolean> ifFollowEachOther(Long firstUserId, Long secondUserId);
}
