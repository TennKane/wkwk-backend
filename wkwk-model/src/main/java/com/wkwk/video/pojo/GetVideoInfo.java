package com.wkwk.video.pojo;

import lombok.Data;

import java.util.List;

/**
 * 上传视频流需要的类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class GetVideoInfo {
    /**
     * 视频列表
     */
    private List<VideoDetailInfo> videoList;
    /**
     * 视频总数
     */
    private Integer total;
    /**
     * 最后一个视频的id
     */
    private Integer lastVideoId;
}

