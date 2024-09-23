package com.gtkang.wkwkvideo.controller;

import com.gtkang.wkwkvideo.service.VideoDoLikeService;
import com.gtkang.wkwkvideo.service.VideoUploadService;
import com.wkwk.response.ResponseResult;
import com.wkwk.video.dto.VideoPublishDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;

/**
 * 视频相关功能
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@RestController
@RequestMapping("/wkwk/video")
public class VideoController {
    @Resource
    private VideoUploadService videoUploadService;
    @Resource
    VideoDoLikeService videoDoLikeService;

    //发布视频
    @PostMapping("/publish")
    public ResponseResult publish(VideoPublishDto videoPublishDto){
        return videoUploadService.publish(videoPublishDto);
    }
    //上传文件
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file){
        return videoUploadService.upload(file);
    }
    //type为1点赞，为0取消点赞
    @PostMapping("/doLike")
    public ResponseResult doLike(Long videoId,int type){
        return videoDoLikeService.doLike(videoId,type);
    }

    //type为1收藏，为0取消收藏
    @PostMapping("/doCollect")
    public ResponseResult doCollect(Long videoId,int type){
        return videoDoLikeService.doCollect(videoId,type);
    }
    //每次获取10个视频
    @GetMapping("/getVideos")
    public ResponseResult getVideo(Integer lastVideoId){return videoUploadService.getVideos(lastVideoId);}
}
