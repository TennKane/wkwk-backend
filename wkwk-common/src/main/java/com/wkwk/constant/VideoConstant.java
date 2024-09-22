package com.wkwk.constant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class VideoConstant {
    //以videoId为key的集合，记录点赞的user集合
    public static final String SET_LIKE_KEY="set_like:";
    public static final String SET_COLLECT_KEY="set_collect:";
    public static final String SET_COMMENT_KEY="set_comment:";
    //记录video的点赞数的key
    public static final String STRING_LIKE_KEY="string_like:";
    public static final String STRING_COLLECT_KEY="string_collect:";
    public static final String STRING_COMMENT_KEY="string_comment:";
    //记录
    public static final int LIKE_TYPE=1;
    public static final int COLLECT_TYPE=2;
    public static final int COMMENTTYPE=3;
    //存视频的链表头
    public static final String VIDEO_LIST_KEY="video_list:";
    //存当前用户应该看的listId的键
    public static final String NOW_LIST_ID="now_list_id:";
}
