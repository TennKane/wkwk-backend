package com.wkwk.video.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 视频简略信息
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class VideoInfo {

    /**
     * 视频id
     */
    private String videoId;

    /**
     * 作者id
     */
    private String authorId;

    /**
     * 封面url
     */
    private String coverUrl;

    /**
     * 视频url
     */
    private String videoUrl;

    /**
     * 视频标题
     */
    private String title;
}
