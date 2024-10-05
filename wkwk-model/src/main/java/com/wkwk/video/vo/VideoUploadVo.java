package com.wkwk.video.vo;

import lombok.Data;


@Data
public class VideoUploadVo {
    /**
     * 视频URL
     */
    private String videoUrl;
    /**
     * 视频封面
     */
    private String coverUrl;
}
