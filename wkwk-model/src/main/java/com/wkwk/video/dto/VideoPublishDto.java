package com.wkwk.video.dto;

import lombok.Data;

@Data
public class VideoPublishDto {
    private String videoUrl;
    private String coverUrl;
    private String title;
    private Integer section;
}
