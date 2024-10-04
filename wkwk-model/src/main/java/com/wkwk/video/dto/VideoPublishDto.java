package com.wkwk.video.dto;

import lombok.Data;

@Data
public class VideoPublishDto {
    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 封面地址
     */
    private String coverUrl;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频描述
     */
    private Integer section;
}
