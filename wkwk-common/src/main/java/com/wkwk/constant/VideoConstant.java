package com.wkwk.constant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class VideoConstant {
    //以videoId为key的集合，记录点赞的user集合
    public static final String SET_LIKE_KEY="video_set_like:";
    public static final String SET_COLLECT_KEY="video_set_collect:";
    public static final String SET_COMMENT_KEY="video_set_comment:";


    //以userId为key的集合，记录点赞过的视频id
    public static final String USER_SET_LIKE_KEY="use_set_like:";
    //以userId为key的list，记录收藏过的视频id
    public static final String USER_LIST_COLLECT_KEY="use_set_collect:";
    public static final String USER_SET_COMMENT_KEY="use_set_comment:";


    //与videoId对应的video对象
    public static final String VIDEO_ID="video_id:";

    //记录user总点赞数的key
    public static final String USER_LIKES_SUM="user_likes_sum:";
    public static final String USER_COLLECT_SUM="user_collect_sum:";
    public static final String USER_COMMENT_SUM="user_comment_sum:";

    //记录userId下的所有视频id,以list存储
    public static final String USER_VIDEO_LIST="user_video_list:";



    //记录video的点赞数的key
    public static final String STRING_LIKE_KEY="string_like:";
    public static final String STRING_COLLECT_KEY="string_collect:";
    public static final String STRING_COMMENT_KEY="string_comment:";



    //记录
    public static final int LIKE_TYPE=1;
    public static final int COLLECT_TYPE=2;
    public static final int COMMENT_TYPE=3;




    //存视频id的链表头
    public static final String VIDEO_LIST_KEY="video_list:";
    //存当前用户应该看的listId的键
    public static final String NOW_LIST_ID="now_list_id:";
}
