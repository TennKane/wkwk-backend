package com.wkwk.service;

import com.wkwk.interact.dto.MessageListDto;
import com.wkwk.interact.dto.MessageSendDto;
import com.wkwk.interact.vo.ChatListVo;
import com.wkwk.interact.vo.MessageListVo;
import com.wkwk.response.ResponseResult;
import org.springframework.stereotype.Service;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public interface PrivateMessageService {
    /**
     * 发送私信
     * @param messageSendDto 私信发送dto
     * @return ResponseResult
     */
    ResponseResult sendPrivateMessage(MessageSendDto messageSendDto);

    /**
     * 私信列表
     * @param messageListDto 私信列表dto
     * @return ResponseResult
     */
    ResponseResult<MessageListVo> privateMessageList(MessageListDto messageListDto);

    /**
     * 私信列表
     * @return ResponseResult
     */
    ResponseResult<ChatListVo> chatList();
}
