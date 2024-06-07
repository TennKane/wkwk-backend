package com.gtkang.wkwkinteract.controller;

import com.gtkang.wkwkinteract.service.PrivateMessageService;
import com.wkwk.interact.dto.MessageListDto;
import com.wkwk.interact.dto.MessageSendDto;
import com.wkwk.interact.vo.MessageListVo;
import com.wkwk.response.ResponseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 私信控制器
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@RestController
@RequestMapping("/azaz/interact/message")
public class PrivateMessageController {
    @Resource
    private PrivateMessageService privateMessageService;

    /**
     * 发送私信
     * @param messageSendDto 私信发送dto
     * @return ResponseResult
     */
    @PostMapping("/send")
    public ResponseResult sendPrivateMessage(MessageSendDto messageSendDto) {
        return privateMessageService.sendPrivateMessage(messageSendDto);
    }

    /**
     * 私信列表
     * @param messageListDto 私信列表dto
     * @return  ResponseResult
     */
    @GetMapping("/list")
    public ResponseResult<MessageListVo> privateMessageList(MessageListDto messageListDto) {
        return privateMessageService.privateMessageList(messageListDto);
    }
}
