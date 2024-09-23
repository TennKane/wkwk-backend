package com.gtkang.feignapi.clients;


import com.wkwk.response.ResponseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 视频客户端
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@FeignClient("wkwk-service-video")
public interface VideoClient {

    /**
     * 得到用户被赞数
     * @param userId 用户id
     * @return 被赞数
     */
    @GetMapping("/wkwk/video/getUserLikes")
    ResponseResult<Integer> getUserLikes(@RequestParam("userId") Long userId);

    /**
     * 得到用户作品总数
     * @param userId 用户id
     * @return 作品总数
     */
    @GetMapping("/wkwk/video/getUserWorks")
    ResponseResult<Integer> getUserWorks(@RequestParam("userId")Long userId);

    /**
     * 得到视频简略信息
     * @param videoId 视频id
     * @return 视频简略信息
     */
    @GetMapping("/info")
    ResponseResult<VideoInfo> getVideoInfo(@RequestParam("videoId") Long videoId);

    /**
     * 得到视频详细信息
     * @param videoId 视频id
     * @return 视频详细信息
     */
    @GetMapping("/detailInfo")
    ResponseResult<VideoDetail> getVideoDetailInfo(@RequestParam("videoId") Long videoId);
}
