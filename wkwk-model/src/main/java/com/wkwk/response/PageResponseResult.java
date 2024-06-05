package com.wkwk.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 分页查询结果返回类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageResponseResult<T> extends ResponseResult<T> implements Serializable {
    /**
     * 当前页
     */
    private Integer currentPage;
    /**
     * 每页大小
     */
    private Integer size;
    /**
     * 总数
     */
    private Integer total;

}
