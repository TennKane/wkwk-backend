package com.gtkang.wkwkvideo.controller;

import com.gtkang.wkwkvideo.service.VideoDoLikeService;
import com.gtkang.wkwkvideo.service.VideoUploadService;
import com.wkwk.response.ResponseResult;
import com.wkwk.video.dto.VideoPublishDto;
import com.wkwk.video.pojo.Video;
import com.wkwk.video.vo.VideoDetail;
import com.wkwk.video.vo.VideoInfo;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.List;

/**
 * 视频相关功能
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@RestController
@RequestMapping("/wkwk/video")
public class VideoController {
    @Resource
    private VideoUploadService videoUploadService;
    @Resource
    VideoDoLikeService videoDoLikeService;

    /**
     * 发布视频
     * @param videoPublishDto 视频信息
     * @return 是否成功
     */
    @PostMapping("/publish")
    public ResponseResult publish(VideoPublishDto videoPublishDto){
        return videoUploadService.publish(videoPublishDto);
    }

    /**
     * 上传视频
     * @param file 视频文件
     * @return 是否成功
     */
    @PostMapping("/upload")
    public ResponseResult upload(MultipartFile file){
        return videoUploadService.upload(file);
    }

    /**
     * 点赞
     * @param videoId 视频id
     * @param authorId 作者id
     * @param type 1为点赞，0为取消点赞
     * @return 是否成功
     */
    @PostMapping("/doLike")
    public ResponseResult doLike(Long videoId,Long authorId,int type){
        return videoDoLikeService.doLike(videoId,authorId,type);
    }

    /**
     * 收藏
     * @param videoId 视频id
     * @param authorId 作者id
     * @param type 1为收藏，0为取消收藏
     * @return 是否成功
     */
    @PostMapping("/doCollect")
    public ResponseResult doCollect(Long videoId,Long authorId,int type){
        return videoDoLikeService.doCollect(videoId,authorId,type);
    }

    /**
     * 评论
     * @param videoId 视频id
     * @param parentId 父评论id
     * @param content 评论内容
     * @return 是否成功
     */
    @PostMapping("/doComment")
    public ResponseResult doComment(Long videoId, Long parentId,String content){
        return videoDoLikeService.doComment(videoId,parentId,content);
    }

    /**
     * 得到视频列表
     * @param lastVideoId 最后一个视频的id
     * @return 视频列表
     */
    @GetMapping("/getVideos")
    public ResponseResult getVideo(Integer lastVideoId,Integer section){
        return videoUploadService.getVideos(lastVideoId,section);
    }

    /**
     * 得到用户点赞过的视频列表
     * @param userId 用户id
     * @return 视频列表
     */
    @GetMapping("/getUserLikes")
    public ResponseResult<Integer> getUserLikes(@RequestParam("userId")Long userId){
        return videoDoLikeService.getUserLikes(userId);
    }

    /**
     * 作品总数
     * @param userId 用户id
     * @return 作品总数
     */
    @GetMapping("/getUserWorks")
    public ResponseResult<Integer> getUserWorks(@RequestParam("userId")Long userId){
        return videoDoLikeService.getUserWorks(userId);
    }


    /**
     * 得到用户收藏过的视频列表
     * @param currentPage 当前页
     * @param userId 用户id
     * @return 视频列表
     */
    @GetMapping("/getPublishedVideos")
    public ResponseResult<List<Video>> getAllVideos(Integer currentPage, Integer userId){
        return videoDoLikeService.getPublishedVideos(currentPage,userId);
    }


    /**
     * 得到用户收藏过的视频列表
     * @param currentPage 当前页
     * @param userId 用户id
     * @return 视频列表
     */
    @GetMapping("/showCollectList")
    public ResponseResult collects(Integer currentPage,Integer userId){
        return videoDoLikeService.showCollectsList(currentPage);
    }

    /**
     * 得到用户收藏过的视频列表
     * @param commentId 评论id
     * @param videoId 视频id
     * @return 视频列表
     */
    @GetMapping ("/getCommentList")
    public ResponseResult doComment(Long commentId,Long videoId)
    {
        return videoDoLikeService.getCommentList(commentId,videoId);
    }


    /**
     * 得到视频信息
     * @param videoId 视频id
     * @return 视频信息
     */
    @GetMapping("/info")
    public ResponseResult<VideoInfo> getVideoInfo(@RequestParam("videoId") Long videoId){
        return videoUploadService.getVideoInfo(videoId);
    }

    /**
     * 得到视频信息
     * @param videoId 视频id
     * @return 视频信息
     */
    @GetMapping("/detailInfo")
    public ResponseResult<VideoDetail> getVideoDetailInfo(@RequestParam("videoId")Long videoId){
        return videoUploadService.getVideoDetailInfo(videoId);
    }
}
