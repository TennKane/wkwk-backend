package com.gtkang.wkwkvideo.mapper;

import com.wkwk.video.pojo.Video;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Mapper
public interface VideoMapper extends com.baomidou.mybatisplus.core.mapper.BaseMapper<Video>{
    @Select("select * from tb_video order by id desc limit 1")
    Video getLastVideo();
}