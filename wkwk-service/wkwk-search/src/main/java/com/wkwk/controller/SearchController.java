package com.wkwk.controller;

import com.wkwk.interact.vo.UserListVo;
import com.wkwk.interact.vo.VideoListVo;
import com.wkwk.response.ResponseResult;
import com.wkwk.service.SearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 搜索模块
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@RestController
@RequestMapping("/wkwk/search")
public class SearchController {

    @Resource
    private SearchService searchService;

    /**
     * 搜索用户
     * @param keyword 关键字
     * @param page 页码
     * @param pageSize 页大小
     * @return 用户列表
     */
    @GetMapping("/user")
    public ResponseResult<UserListVo> searchUser(String keyword, Integer page, Integer pageSize) {
        return searchService.searchUser(keyword, page, pageSize);
    }

    /**
     * 搜索视频
     * @param keyword 关键字
     * @param page 页码
     * @param pageSize 页大小
     * @return 视频列表
     */
    @GetMapping("/video")
    public ResponseResult<VideoListVo> searchVideo(String keyword, Integer page, Integer pageSize) {
        return searchService.searchVideo(keyword, page, pageSize);
    }
}
