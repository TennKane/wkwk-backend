package com.wkwk.video.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_comment")
public class Comment {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("video_id")
    private Long videoId;


    @TableField("user_id")
    private Long userId;

    @TableField("user_name")
    private String userName;

    @TableField("image")
    private String image;

    @TableField("parent_id")
    private Long parentId;


    @TableField("content")
    private String content;

    @TableField("status")
    private Integer status;

    @TableField("create_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @TableField("update_time")
    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;




}
