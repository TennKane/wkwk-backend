package com.wkwk.interact.vo;

import com.wkwk.video.vo.VideoDetail;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 视频列表Vo类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
@Builder
public class VideoListVo {

    /**
     *  本次总数
     */
    private Integer total;

    /**
     *  视频列表
     */
    private List<VideoDetail> videoList;
}

