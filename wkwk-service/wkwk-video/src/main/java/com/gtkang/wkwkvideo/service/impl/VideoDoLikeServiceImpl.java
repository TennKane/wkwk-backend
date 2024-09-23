package com.gtkang.wkwkvideo.service.impl;

import com.gtkang.wkwkvideo.service.DbOpsService;
import com.gtkang.wkwkvideo.service.VideoDoLikeService;
import com.wkwk.constant.VideoConstant;
import com.wkwk.response.ResponseResult;
import com.wkwk.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
    /**
     * 点赞操作
     * @param videoId 视频id
     * @param type type为1点赞，0取消点赞
     * @return
     */
    @Override
    public ResponseResult doLike(Long videoId,int type){
        //set集合的key
        String setKey= VideoConstant.SET_LIKE_KEY+videoId;
        //kv类型的key(key为videoId,value为点赞总数)
        String strKey=VideoConstant.STRING_LIKE_KEY+videoId;
        Long userId = ThreadLocalUtil.getUserId();
        //点赞
        if(type==1){
            //添加到redis，以set方式存储，key为videoId，value为userId
            if(this.stringRedisTemplate.opsForSet().add(setKey, userId.toString())==1){
                //异步添加到mongodb
                dbOpsService.insertIntoMongo(userId,videoId, VideoConstant.LIKE_TYPE,1);
                //redis数据加一
                dbOpsService.addIntSafely(strKey,1);
                return ResponseResult.successResult("点赞成功");
            }
            else {
                return ResponseResult.errorResult("点赞失败");
            }
        }
        //取消点赞
        else {
            if(this.stringRedisTemplate.opsForSet().remove(setKey, userId)==1){
                //异步更新到mongodb
                dbOpsService.insertIntoMongo(userId,videoId,VideoConstant.LIKE_TYPE,0);
                //redis数据减一
                dbOpsService.addIntSafely(strKey,-1);
                return ResponseResult.successResult();
            }
            else {
                return ResponseResult.errorResult("点赞失败");
            }
        }
    }

    @Override
    public ResponseResult doCollect(Long videoId, int type) {
        //set集合的key
        String setKey= VideoConstant.SET_COLLECT_KEY+videoId;
        //kv类型的key(key为videoId,value为点赞总数)
        String strKey=VideoConstant.STRING_COLLECT_KEY+videoId;
        //获取userId
        Long userId = ThreadLocalUtil.getUserId();
        //收藏操作
        if(type==1){
            //添加到redis，以set方式存储，key为videoId，value为userId
            if(this.stringRedisTemplate.opsForSet().add(setKey, userId.toString())==1){
                //异步添加到mongodb
                dbOpsService.insertIntoMongo(userId,videoId, VideoConstant.COLLECT_TYPE,1);
                //redis数据加一
                dbOpsService.addIntSafely(strKey,1);
                return ResponseResult.successResult();
            }
            else {
                return ResponseResult.errorResult("点赞失败");
            }
        }
        //取消收藏
        else {
            if(this.stringRedisTemplate.opsForSet().remove(setKey, userId)==1){
                //异步更新到mongodb
                dbOpsService.insertIntoMongo(userId,videoId,VideoConstant.COLLECT_TYPE,0);
                //redis数据减一
                dbOpsService.addIntSafely(strKey,-1);
                return ResponseResult.successResult();
            }
            else {
                return ResponseResult.errorResult("点赞失败");
            }
        }
    }



}
