package com.wkwk.interact.dto;

import lombok.Data;

/**
 * 分享视频dto
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class ShareVideoDto {
    /**
     * 被分享的视频id
     */
    private Long videoId;

    /**
     * 接收者id
     */
    private Long receiverId;
}
