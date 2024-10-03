package com.wkwk.video.pojo;

import lombok.Data;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class VideoDetailInfo {
    private Long id;
    private Long authorId;
    private String title;
    private Integer section;
    private String coverUrl;
    private String videoUrl;
    private Integer status;
    private Long likes;
    private Long collects;
    private Long comments;
    private boolean isLiked;
    private boolean isCollected;
    private String userName;
    private String image;
    private String createTime;

}
