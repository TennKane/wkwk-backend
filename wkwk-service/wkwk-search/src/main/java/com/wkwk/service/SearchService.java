package com.wkwk.service;

import com.wkwk.interact.vo.UserListVo;
import com.wkwk.interact.vo.VideoListVo;
import com.wkwk.response.ResponseResult;
import org.springframework.stereotype.Service;

/**
 * 搜索模块接口类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public interface SearchService {
    /**
     * 搜索用户
     * @param keyword 关键字
     * @param page 页码
     * @param pageSize 页大小
     * @return 用户列表
     */
    ResponseResult<UserListVo> searchUser(String keyword, Integer page, Integer pageSize);

    /**
     * 搜索视频
     * @param keyword 关键字
     * @param page 页码
     * @param pageSize 页大小
     * @return 视频列表
     */
    ResponseResult<VideoListVo> searchVideo(String keyword, Integer page, Integer pageSize);
}

