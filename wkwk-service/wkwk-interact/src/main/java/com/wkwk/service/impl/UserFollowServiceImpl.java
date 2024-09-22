package com.wkwk.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gtkang.feignapi.clients.UserClient;
import com.wkwk.constant.InteractConstant;
import com.wkwk.exception.ErrorOperationException;
import com.wkwk.exception.ErrorParamException;
import com.wkwk.exception.NullParamException;
import com.wkwk.exception.UserNotLoginException;
import com.wkwk.interact.dto.UserFollowDto;
import com.wkwk.interact.pojo.Follow;
import com.wkwk.mapper.FollowMapper;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.vo.UserPersonalInfoVo;
import com.wkwk.utils.ThreadLocalUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 私信service
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public class UserFollowServiceImpl implements com.wkwk.service.UserFollowService{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private UserClient userClient;

    @Resource
    private FollowMapper followMapper;

    /**
     * 关注或者取消关注
     * @param userFollowDto 关注信息
     * @return 关注结果
     */
    @Override
    public ResponseResult doFollow(UserFollowDto userFollowDto) {
        // 1.校验参数
        if (userFollowDto == null || userFollowDto.getUserId() == null || userFollowDto.getType() == null) {
            throw new NullParamException();
        }
        if (!userFollowDto.getType().equals(InteractConstant.FOLLOW_CODE)
                && !userFollowDto.getType().equals(InteractConstant.UNFOLLOW_CODE)) {
            throw new ErrorParamException("关注类型错误!");
        }
        // 2.关注或者取消关注
        Long myId = ThreadLocalUtil.getUserId();
        Long otherId = userFollowDto.getUserId();
        // 2.1.不能对自己进行此操作
        if (myId.equals(otherId)) {
            throw new ErrorParamException("不能对自己进行此操作!");
        }
        // 创建锁对象
        RLock redisLock = redissonClient.getLock(InteractConstant.REDIS_LOCK_FOLLOW_KEY + myId);
        // 尝试获取锁
        boolean isLock = redisLock.tryLock();
        if(!isLock){
            throw new ErrorOperationException("请勿频繁操作!");
        }
        try {
            if (userFollowDto.getType().equals(InteractConstant.FOLLOW_CODE)) {
                // 关注
                //  将关注信息存入redis
                Long result = stringRedisTemplate.opsForSet().add(InteractConstant.REDIS_FOLLOW_KEY + myId, otherId.toString());
                if (result == null || result == 0) {
                    throw new ErrorOperationException("已关注，请勿重复操作!");
                }
                // 查询是否是互关好友，是的话加入互关列表
                Boolean isFriend = stringRedisTemplate.opsForSet().isMember(InteractConstant.REDIS_FOLLOW_KEY + otherId, myId.toString());
                if (Boolean.TRUE.equals(isFriend)){
                    stringRedisTemplate.opsForSet().add(InteractConstant.REDIS_FRIEND_KEY + myId, otherId.toString());
                    stringRedisTemplate.opsForSet().add(InteractConstant.REDIS_FRIEND_KEY + otherId, myId.toString());
                }
                // 粉丝数加一
                stringRedisTemplate.opsForValue().increment(InteractConstant.REDIS_FANS_NUM_KEY + otherId);
                // 将关注信息存入数据库
                followMapper.insert(Follow.builder()
                        .userId(myId)
                        .followId(otherId)
                        .build());
                return ResponseResult.successResult();
            }
            // 取消关注，查看是否确实关注了
            Boolean isFollow = stringRedisTemplate.opsForSet().isMember(InteractConstant.REDIS_FOLLOW_KEY + myId, otherId.toString());
            if (!Boolean.TRUE.equals(isFollow)) {
                throw new ErrorOperationException("请先关注!");
            }
            // 粉丝数减一
            stringRedisTemplate.opsForValue().decrement(InteractConstant.REDIS_FANS_NUM_KEY + otherId);
            // 查询是否是互关好友，是的话从互关列表中删除
            Boolean isFriend = stringRedisTemplate.opsForSet().isMember(InteractConstant.REDIS_FOLLOW_KEY + otherId, myId.toString());
            if (Boolean.TRUE.equals(isFriend)){
                stringRedisTemplate.opsForSet().remove(InteractConstant.REDIS_FRIEND_KEY + myId, otherId.toString());
                stringRedisTemplate.opsForSet().remove(InteractConstant.REDIS_FRIEND_KEY + otherId, myId.toString());
            }
            // 将关注信息从redis中删除
            Long ifRemove = stringRedisTemplate.opsForSet().remove(InteractConstant.REDIS_FOLLOW_KEY + myId, otherId.toString());
            if (ifRemove != null && ifRemove != 0) {
                // redis成功删除，删除数据库中的数据
                followMapper.delete(new QueryWrapper<Follow>()
                        .eq("user_id", myId)
                        .eq("follow_id", otherId));
                return ResponseResult.successResult();
            }
            return ResponseResult.successResult();
        } finally {
            // 释放锁
            redisLock.unlock();
        }
    }

    /**
     * 判断是否互相关注
     * @param firstUser 第一个用户
     * @param secondUser 第二个用户
     * @return 是否关注
     */
    @Override
    public ResponseResult<Boolean> ifFollow(Long firstUser, Long secondUser) {
        // 1.校验参数
        if (firstUser == null || secondUser == null) {
            throw new NullParamException();
        }
        // 2.判断是否互相关注
        Boolean isFollow = stringRedisTemplate.opsForSet().isMember(InteractConstant.REDIS_FOLLOW_KEY + firstUser, secondUser.toString());
        if (Boolean.TRUE.equals(isFollow)) {
            return ResponseResult.successResult(Boolean.TRUE);
        }
        return ResponseResult.successResult(Boolean.FALSE);
    }

    /**
     * 获取互关朋友列表
     * @return 互关朋友列表
     */
    @Override
    public ResponseResult<List<UserPersonalInfoVo>> getFriends() {
        Long myId = ThreadLocalUtil.getUserId();
        if (myId == null) {
            throw new UserNotLoginException();
        }
        // 1.获取互关朋友列表
        Set<String> friendIds = stringRedisTemplate.opsForSet().members(InteractConstant.REDIS_FRIEND_KEY + myId);
        List<UserPersonalInfoVo> friends = null;
        if (friendIds == null || friendIds.isEmpty()) {
            // 2.查询互关朋友信息
            return ResponseResult.successResult(friends);
        }
        // 2.查询互关朋友信息
        for (String friendId : friendIds) {
            if (friends == null) {
                friends = new ArrayList<>();
            }
            ResponseResult<UserPersonalInfoVo> userPersonalInfo = userClient.getUserPersonalInfo(Long.valueOf(friendId));
            if (userPersonalInfo == null || userPersonalInfo.getData() == null) {
                continue;
            }
            friends.add(userPersonalInfo.getData());
        }
        return ResponseResult.successResult(friends);
    }

    /**
     * 获取关注列表
     * @return 关注列表
     */
    @Override
    public ResponseResult<List<UserPersonalInfoVo>> getFollowList() {
        Long myId = ThreadLocalUtil.getUserId();
        if (myId == null) {
            throw new UserNotLoginException();
        }
        // 获取关注列表
        Set<String> followIds = stringRedisTemplate.opsForSet().members(InteractConstant.REDIS_FOLLOW_KEY + myId);
        List<UserPersonalInfoVo> follows = new ArrayList<>();
        if (followIds == null || followIds.isEmpty()) {
            return ResponseResult.successResult(follows);
        }
        // 查询关注列表信息
        for (String followId : followIds) {
            ResponseResult<UserPersonalInfoVo> userPersonalInfo = userClient.getUserPersonalInfo(Long.valueOf(followId));
            if (userPersonalInfo == null || userPersonalInfo.getData() == null) {
                continue;
            }
            follows.add(userPersonalInfo.getData());
        }
        return ResponseResult.successResult(follows);
    }

    /**
     * 获取关注数
     * @return 关注数
     */
    @Override
    public ResponseResult<Integer> getFollowNum(Long userId) {
        if (userId == null) {
            throw new UserNotLoginException();
        }
        // 获取关注数，即set的长度
        Long size = stringRedisTemplate.opsForSet().size(InteractConstant.REDIS_FOLLOW_KEY + userId);
        return ResponseResult.successResult(size == null ?  0 : size.intValue());
    }

    /**
     * 获取粉丝数
     * @return 粉丝数
     */
    @Override
    public ResponseResult<Integer> getFansNum(Long userId) {
        if (userId == null) {
            throw new UserNotLoginException();
        }
        // 获取粉丝数
        String fansNum = stringRedisTemplate.opsForValue().get(InteractConstant.REDIS_FANS_NUM_KEY + userId);
        return ResponseResult.successResult(fansNum == null ?  0 : Integer.parseInt(fansNum));
    }
}