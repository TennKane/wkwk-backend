package com.wkwk.interact.vo;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 私信vo
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageVo {

    /**
     * 发送者id
     */
    private String senderId;

    /**
     * 私信id
     */
    private String messageId;

    /**
     * 私信内容
     */
    private String messageContent;

    /**
     * 消息类型 0－私信 1－ 朋友分享 2－系统消息
     */
    private Integer status;
}
