package com.gtkang.wkwkvideo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gtkang.wkwkvideo.mapper.CommentMapper;
import com.gtkang.wkwkvideo.mapper.VideoMapper;
import com.gtkang.wkwkvideo.service.DbOpsService;
import com.gtkang.wkwkvideo.service.VideoDoLikeService;
import com.gtkang.wkwkvideo.service.VideoUploadService;
import com.wkwk.clients.UserClient;
import com.wkwk.constant.VideoConstant;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.vo.UserPersonalInfoVo;
import com.wkwk.utils.ThreadLocalUtil;
import com.wkwk.video.pojo.Comment;
import com.wkwk.video.pojo.Video;
import com.wkwk.video.pojo.VideoList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
@Slf4j
public class VideoDoLikeServiceImpl implements VideoDoLikeService {
    @Resource
    DbOpsService dbOpsService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    MongoTemplate mongoTemplate;
    @Resource
    VideoUploadService videoUploadService;
    @Resource
    VideoMapper videoMapper;
    @Resource
    CommentMapper commentMapper;
    @Resource
    UserClient userClient;

    /**
     * 点赞
     * @param videoId 视频id
     * @param authorId 作者id
     * @param type 1为点赞，0为取消点赞
     * @return 是否成功
     */
    @Override
    public ResponseResult doLike(Long videoId,Long authorId,int type){
        //set集合的key
        String setKey= VideoConstant.SET_LIKE_KEY+videoId;
        //kv类型的key(key为videoId,value为点赞总数)
        String strKey=VideoConstant.STRING_LIKE_KEY+videoId;
        //视频作者的点赞总数的key
        String userKey=VideoConstant.USER_LIKES_SUM+authorId;
        Long userId = ThreadLocalUtil.getUserId();
        //当前用户点赞的视频集合key
        String nowUserKey=VideoConstant.USER_SET_LIKE_KEY+userId.toString();
        //点赞
        if(type==1){
            //添加到redis，以set方式存储，key为videoId，value为userId
            if(Boolean.FALSE.equals(this.stringRedisTemplate.opsForSet().isMember(setKey, String.valueOf(userId)))){
                //添加到redis
                this.stringRedisTemplate.opsForSet().add(setKey, String.valueOf(userId));
                this.stringRedisTemplate.opsForSet().add(nowUserKey,String.valueOf(videoId));
                //异步添加到mongodb
                dbOpsService.insertIntoMongo(userId,videoId, VideoConstant.LIKE_TYPE,1);
                //redis数据加一
                dbOpsService.addIntSafely(strKey,1);
                dbOpsService.addIntSafely(userKey,1);
                return ResponseResult.successResult("点赞成功");
            }
            else {
                return ResponseResult.errorResult("重复点赞");
            }
        }
        //取消点赞
        else {
            //判断是否点过赞
            if(Boolean.TRUE.equals(this.stringRedisTemplate.opsForSet().isMember(setKey, String.valueOf(userId)))){
                //取消点赞
                this.stringRedisTemplate.opsForSet().remove(setKey, userId.toString());
                this.stringRedisTemplate.opsForSet().remove(nowUserKey,String.valueOf(videoId));
                //异步更新到mongodb
                dbOpsService.insertIntoMongo(userId,videoId,VideoConstant.LIKE_TYPE,0);
                //redis数据减一
                dbOpsService.addIntSafely(strKey,-1);
                dbOpsService.addIntSafely(userKey,-1);
                return ResponseResult.successResult();
            }
            else {
                return ResponseResult.errorResult("重复取消");
            }
        }
    }

    /**
     * 收藏
     * @param videoId  视频id
     * @param authorId  作者id
     * @param type 1为收藏，0为取消收藏
     * @return 是否成功
     */
    @Override
    public ResponseResult doCollect(Long videoId,Long authorId, int type) {
        //set集合的key
        String setKey= VideoConstant.SET_COLLECT_KEY+videoId;
        //kv类型的key(key为videoId,value为点赞总数)
        String strKey=VideoConstant.STRING_COLLECT_KEY+videoId;
        //视频作者的点赞总数的key
        String userKey=VideoConstant.USER_COLLECT_SUM+authorId;
        //获取userId
        Long userId = ThreadLocalUtil.getUserId();
        //当前用户收藏的视频集合key
        String nowUserKey=VideoConstant.USER_LIST_COLLECT_KEY+userId.toString();
        //收藏操作
        if(type==1){
            //添加到redis，以set方式存储，key为videoId，value为userId
            if(Boolean.FALSE.equals(this.stringRedisTemplate.opsForSet().isMember(setKey, String.valueOf(userId)))){
                //添加到redis
                this.stringRedisTemplate.opsForSet().add(setKey, String.valueOf(userId));
                this.stringRedisTemplate.opsForList().leftPush(nowUserKey,String.valueOf(videoId));
                //异步添加到mongodb
                dbOpsService.insertIntoMongo(userId,videoId, VideoConstant.COLLECT_TYPE,1);
                //redis数据加一
                dbOpsService.addIntSafely(strKey,1);
                dbOpsService.addIntSafely(userKey,1);
                return ResponseResult.successResult();
            }
            else {
                return ResponseResult.errorResult("收藏失败");
            }
        }
        //取消收藏
        else {
            //判断是否收藏
            if(Boolean.TRUE.equals(this.stringRedisTemplate.opsForSet().isMember(setKey, String.valueOf(userId)))){
                //取消收藏
                this.stringRedisTemplate.opsForSet().remove(setKey, userId.toString());
                this.stringRedisTemplate.opsForSet().remove(nowUserKey,String.valueOf(videoId));
                //异步更新到mongodb
                dbOpsService.insertIntoMongo(userId,videoId,VideoConstant.COLLECT_TYPE,0);
                //redis数据减一
                dbOpsService.addIntSafely(strKey,-1);
                dbOpsService.addIntSafely(userKey,-1);
                return ResponseResult.successResult();
            }
            else {
                return ResponseResult.errorResult("取消收藏失败");
            }
        }
    }

    /**
     * 评论视频
     * @param videoId 视频id
     * @param parentId 上一级评论id，若无上一级，则为null
     * @param content 评论内容
     */
    @Override
    public ResponseResult doComment(Long videoId, Long parentId,String content){
        //得到userId
        Long userId = ThreadLocalUtil.getUserId();
        //视频的评论总数的key
        String key=VideoConstant.STRING_COMMENT_KEY+videoId.toString();
        //redis数据加一
        dbOpsService.addIntSafely(key,1);
        //得到当前用户信息
        ResponseResult<UserPersonalInfoVo> res = userClient.getUserPersonalInfo(userId);
        UserPersonalInfoVo user = res.getData();
        //得到评论，插入数据库
        Comment comment = Comment.builder()
                .userId(userId)
                .content(content)
                .videoId(videoId)
                .parentId(parentId==null?0L:parentId)
                .userName(user.getUsername())
                .image(user.getImage())
                .status(1)
                .build();
        commentMapper.insert(comment);
        return ResponseResult.successResult();
    }

    /**
     *  得到视频的评论列表
     * @param commentId 最后一个评论的id
     * @param videoId 视频id
     * @return 评论列表
     */
    @Override
    public ResponseResult getCommentList(Long commentId,Long videoId){
        QueryWrapper<Comment> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("parent_id",commentId).eq("video_id",videoId);
        List<Comment> list = commentMapper.selectList(queryWrapper);
        return ResponseResult.successResult(list);
    }


    /**
     * 得到用户的获赞数
     * @param userId1 用户id
     * @return 点赞数
     */
    @Override
    public ResponseResult<Integer> getUserLikes(Long userId1){
        String userId = userId1.toString();
        String userKey=VideoConstant.USER_LIKES_SUM+userId;
        String s = stringRedisTemplate.opsForValue().get(userKey);
        if(s==null){
            return ResponseResult.successResult(0);
        }else{
            return ResponseResult.successResult(Integer.parseInt(s));
        }
    }


    /**
     * 得到用户作品数
     * @param userId 用户id
     * @return 作品数
     */
    @Override
    public ResponseResult<Integer> getUserWorks(Long userId){
        QueryWrapper<Video>wrapper=new QueryWrapper<>();
        wrapper.eq("author_id",userId);
        Long num = videoMapper.selectCount(wrapper);
        return ResponseResult.successResult(num.intValue());
    }


    /**
     * 得到用户发布的视频
     * @param currentPage 当前页
     * @param userId 用户id
     * @return 收藏数
     */
    @Override
    public ResponseResult<List<Video>> getPublishedVideos(Integer currentPage,Integer userId){
        //得到对应key
        String key=VideoConstant.USER_VIDEO_LIST+userId.toString();
        //得到用户发布的当前页数的videoId
        List<String> videoIds = stringRedisTemplate.opsForList().range(key, (currentPage-1)* 10L,currentPage*10);
        if(videoIds==null){
            return ResponseResult.successResult(new ArrayList<>());
        }
        List<Video>videos=new ArrayList<>();
        //得到videoId对应的实体类
        for (String videoId : videoIds) {
            Video video = videoUploadService.getVideoById(Integer.parseInt(videoId));
            if(video!=null) {
                videos.add(video);
            }
        }
        VideoList videoList=new VideoList();
        videoList.setVideoList(videos);
        //得到视频总数
        videoList.setTotal(Objects.requireNonNull(stringRedisTemplate.opsForList().size(key)).intValue());
        return ResponseResult.successResult(videos);
    }

    /**
     * 得到用户收藏数
     * @param currentPage 当前页
     * @return 收藏数
     */
    @Override
    public ResponseResult showCollectsList(Integer currentPage){
        Long userId = ThreadLocalUtil.getUserId();
        String key=VideoConstant.USER_LIST_COLLECT_KEY+userId.toString();
        //得到用户发布的当前页数的videoId
        List<String> videoIds = stringRedisTemplate.opsForList().range(key, (currentPage-1)* 10L,currentPage*10);
        if(videoIds==null){
            return ResponseResult.successResult("没有辣");
        }
        List<Video>videos=new ArrayList<>();
        //得到videoId对应的实体类
        for (String videoId : videoIds) {
            Video video = videoUploadService.getVideoById(Integer.parseInt(videoId));
            if(video!=null) {
                videos.add(video);
            }
        }
        VideoList videoList=new VideoList();
        videoList.setVideoList(videos);
        //得到视频总数
        videoList.setTotal(Objects.requireNonNull(stringRedisTemplate.opsForList().size(key)).intValue());
        return ResponseResult.successResult(videos);
    }

}
