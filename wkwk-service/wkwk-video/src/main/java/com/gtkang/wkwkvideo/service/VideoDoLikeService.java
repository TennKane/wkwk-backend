package com.gtkang.wkwkvideo.service;

import com.wkwk.response.ResponseResult;
import com.wkwk.video.pojo.Comment;
import com.wkwk.video.pojo.Video;
import com.wkwk.video.pojo.VideoList;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public interface VideoDoLikeService {
    /**
     * 点赞
     * @param videoId 视频id
     * @param authorId 作者id
     * @param type 1为点赞，0为取消点赞
     * @return 是否成功
     */
    ResponseResult doLike(Long videoId,Long authorId,int type);

    /**
     * 收藏
     * @param videoId  视频id
     * @param authorId  作者id
     * @param type 1为收藏，0为取消收藏
     * @return 是否成功
     */
    ResponseResult doCollect(Long videoId,Long authorId,int type);

    /**
     * 评论
     * @param videoId 视频id
     * @param parentId 父评论id
     * @param content 评论内容
     * @return 是否成功
     */
    ResponseResult doComment(Long videoId, Long parentId,String content);

    /**
     * 得到用户点赞数
     * @param userId 用户id
     * @return 点赞数
     */
    ResponseResult<Integer> getUserLikes(Long userId);

    /**
     * 得到用户作品数
     * @param userId 用户id
     * @return 作品数
     */
    ResponseResult<Integer> getUserWorks(Long userId);

    /**
     * @param userId 用户id
     * @return 收藏数
     */
    ResponseResult<VideoList> getPublishedVideos(Integer currentPage, Integer userId);

    /**
     * 得到用户收藏数
     * @param currentPage 当前页
     * @return 收藏数
     */
    ResponseResult<List<Video>> showCollectsList(Integer currentPage, Integer userId);

    /**
     * 得到视频列表
     * @param commentId 最后一个评论的id
     * @param videoId 视频id
     * @return 视频列表
     */
    ResponseResult<List<Comment>> getCommentList(Long commentId, Long videoId);

}
