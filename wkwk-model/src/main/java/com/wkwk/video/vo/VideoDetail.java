package com.wkwk.video.vo;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
@Builder
public class VideoDetail {
    /**
     * 视频id
     */
    private String videoId;

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

    /**
     * 视频点赞数
     */
    private Integer likes;

    /**
     * 是否点赞
     */
    private Boolean isLiked;

    /**
     * 评论数
     */
    private Integer comments;

    /**
     * 视频收藏数
     */
    private Integer collects;

    /**
     * 是否收藏
     */
    private Boolean isCollected;

    /**
     * 作者id
     */
    private String authorId;

    /**
     * 作者昵称
     */
    private String username;

    /**
     * 作者头像
     */
    private String image;

    /**
     * 视频时间
     */
    private LocalDateTime createTime;
}