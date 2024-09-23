package com.gtkang.wkwkvideo.service;

import com.wkwk.response.ResponseResult;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public interface VideoDoLikeService {
    ResponseResult doLike(Long videoId, int type);
    ResponseResult doCollect(Long videoId,int type);
}
