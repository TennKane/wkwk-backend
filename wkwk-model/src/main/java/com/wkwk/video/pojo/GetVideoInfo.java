package com.wkwk.video.pojo;

import lombok.Data;

import java.util.List;

/**
 * 上传视频流需要的类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class GetVideoInfo {
    List<VideoDetailInfo> videoList;
    Integer total;
    Integer lastVideoId;

}

