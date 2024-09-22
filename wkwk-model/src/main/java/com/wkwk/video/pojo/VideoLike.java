package com.wkwk.video.pojo;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
@Document("video_like")
public class VideoLike implements Serializable {
    //主键
    public String id;
    //用户id
    public Long userId;
    //视频id
    public Long videoId;
    //是否点赞(1是0否)
    public Integer isLike;
    //是否收藏(1是0否)
    public Integer isCollect;
    //评论
    public ArrayList<String> commentList;
}
