package com.gtkang.wkwkvideo.service;

import org.springframework.stereotype.Service;

/**
 * 数据库操作接口
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public interface DbOpsService {
    void addIntSafely(String key, int num);
    void insertIntoMongo(Long userId,Long videoId,int type,Object ops);
    Long getSumFromDb(Long videoId);
}