package com.gtkang.wkwkvideo.controller;

import com.gtkang.wkwkvideo.service.VideoUploadService;
import com.wkwk.response.ResponseResult;
import com.wkwk.video.dto.VideoPublishDto;
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
@RequestMapping("/azaz/video")
public class VideoController {
    @Resource
    private VideoUploadService videoUploadService;

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

}
