package com.wkwk.service;

import com.wkwk.interact.dto.UserFollowDto;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.vo.UserPersonalInfoVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关注服务接口
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public interface UserFollowService {
    /**
     * 关注或者取消关注
     * @param userFollowDto 关注信息
     * @return  关注结果
     */
    ResponseResult doFollow(UserFollowDto userFollowDto);

    /**
     * 判断是否互相关注
     * @param firstUser 第一个用户
     * @param secondUser 第二个用户
     * @return  是否关注
     */
    ResponseResult<Boolean> ifFollow(Long firstUser, Long secondUser);

    /**
     * 获取互关朋友列表
     * @return 互关朋友列表
     */
    ResponseResult<List<UserPersonalInfoVo>> getFriends();

    /**
     * 获取关注列表
     * @return 关注列表
     */
    ResponseResult<List<UserPersonalInfoVo>> getFollowList();

    /**
     * 获取关注数
     * @param userId 用户id
     * @return 关注数
     */
    ResponseResult<Integer> getFollowNum(Long userId);

    /**
     * 获取粉丝列
     * @param userId 用户id
     * @return 粉丝列表
     */
    ResponseResult<Integer> getFansNum(Long userId);
}

