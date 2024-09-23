package com.wkwk.interact.vo;

import com.wkwk.user.vo.UserPersonalInfoVo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 用户列表Vo
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
@Builder
public class UserListVo {

    /**
     * 总数
     */
    private Integer total;

    /**
     * 用户列表
     */
    private List<UserPersonalInfoVo> list;

}

