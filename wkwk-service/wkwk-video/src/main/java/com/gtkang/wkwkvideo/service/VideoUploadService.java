package com.gtkang.wkwkvideo.service;

import com.wkwk.response.ResponseResult;
import com.wkwk.video.dto.VideoPublishDto;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public interface VideoUploadService {
    ResponseResult publish(VideoPublishDto videoPublishDto);

    ResponseResult upload(MultipartFile file);

    ResponseResult getVideos(Integer lastVideoId);
}

