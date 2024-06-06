package com.wkwk.interact.vo;


import com.wkwk.user.dto.UserPersonInfo;
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
    * 发送者
    */
    private UserPersonInfo sender;
    /**
     * 发送者
     */
    private UserPersonInfo receiver;

    /**
     * 私信id
     */
    private Long messageId;

    /**
    * 私信内容
    */
    private String messageContent;

    /**
    * 发送时间
    */
    private LocalDateTime createdTime;
}
