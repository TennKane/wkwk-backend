package com.wkwk.interact.dto;

import lombok.Data;

/**
 * 私信发送dto
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
public class MessageSendDto {
    /**
     * 接收者id
     */
    private Long receiverId;
    /**
     * 私信内容
     */
    private String content;
    /**
     * 私信类型0-普通私信 1-视频分享
     */
    private Integer status;
}
