package com.wkwk.video.pojo;

import lombok.Data;

import java.util.List;

/**
 * 查询收藏列表和发布列表需要的类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class VideoList {
    public List<Video> videoList;
    public Integer total;

}

