package com.wkwk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wkwk.interact.pojo.Follow;
import org.apache.ibatis.annotations.Mapper;

/**
 * 关注mapper接口类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
}
