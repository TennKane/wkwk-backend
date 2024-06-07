package com.gtkang.wkwkinteract.service.impl;

import com.alibaba.fastjson2.JSON;
import com.gtkang.feignapi.clients.UserClient;
import com.gtkang.wkwkinteract.mapper.PrivateMessageMapper;
import com.gtkang.wkwkinteract.service.PrivateMessageService;
import com.wkwk.constant.InteractConstant;
import com.wkwk.exception.ErrorParamException;
import com.wkwk.exception.NullParamException;
import com.wkwk.interact.dto.MessageListDto;
import com.wkwk.interact.dto.MessageSendDto;
import com.wkwk.interact.pojo.PrivateMessage;
import com.wkwk.interact.vo.MessageListVo;
import com.wkwk.interact.vo.MessageVo;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.dto.UserPersonInfo;
import com.wkwk.user.vo.UserPersonalInfoVo;
import com.wkwk.utils.ThreadLocalUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

    @Resource
    private PrivateMessageMapper privateMessageMapper;

    //@Resource
    //private VideoClient videoClient;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private UserClient userClient;


    /**
     * 发送私信
     * @param messageSendDto 私信发送dto
     * @return ResponseResult
     */
    @Override
    public ResponseResult sendPrivateMessage(MessageSendDto messageSendDto) {
        //1.1 校验参数
        if (messageSendDto == null || messageSendDto.getReceiverId() == null
                || messageSendDto.getContent() == null ) {
            throw new NullParamException();
        }
        //1.2 校验参数
        if (messageSendDto.getContent().length() > InteractConstant.MESSAGE_MAX_LENGTH) {
            throw new ErrorParamException("私信内容不能超过" + InteractConstant.MESSAGE_MAX_LENGTH + "个字符！");
        }
        //2.检查是否互关
        Long userId = ThreadLocalUtil.getUserId();
        // Boolean ifFollowEachOther = videoClient.ifFollowEachOther(userId, messageSendDto.getReceiverId()).getData();
        boolean ifFollowEachOther = true;
        if (!ifFollowEachOther) {
            //2.1 未互关，检查是否已发送三条私信
            Integer count = privateMessageMapper.countBySenderId(userId);
            if (count >= InteractConstant.MESSAGE_MAX_COUNT) {
                throw new ErrorParamException("最多向未互关朋友发送三条私信！");
            }
        }
        //3. 发送私信
        PrivateMessage privateMessage = PrivateMessage.builder()
                .messageContent(messageSendDto.getContent())
                .receiverId(messageSendDto.getReceiverId())
                .senderId(ThreadLocalUtil.getUserId())
                .createTime(LocalDateTime.now())
                .status(0)
                .build();
        //3.1 向数据库中添加私信，并且填充id
        privateMessageMapper.insert(privateMessage);
        //3.2 将私信转换为vo
        //3.3 获取发送者信息
        UserPersonalInfoVo senderInfoVo = userClient.getUserPersonalInfo(privateMessage.getSenderId()).getData();
        UserPersonInfo senderInfo = UserPersonInfo.builder().id(privateMessage.getSenderId()).build();
        BeanUtils.copyProperties(senderInfoVo, senderInfo);
        // 5.2 获得接收者信息
        UserPersonalInfoVo receiverInfoVo = userClient.getUserPersonalInfo(privateMessage.getReceiverId()).getData();
        UserPersonInfo receiverInfo = UserPersonInfo.builder().id(privateMessage.getReceiverId()).build();
        BeanUtils.copyProperties(receiverInfoVo, receiverInfo);
        // 5.3 封装私信vo
        MessageVo messageVo = MessageVo.builder()
                .messageId(privateMessage.getId())
                .messageContent(privateMessage.getMessageContent())
                .sender(senderInfo)
                .receiver(receiverInfo)
                .createdTime(privateMessage.getCreateTime())
                .build();
        //3.2 向redis中添加私信,key为小id-大id
        String messageKey = InteractConstant.REDIS_PRIVATE_MESSAGE_KEY + Math.min(userId, messageSendDto.getReceiverId())
                + "-" + Math.max(userId, messageSendDto.getReceiverId()) + ":";
        //3.3 设置私信过期时间为一周
        stringRedisTemplate.opsForList().leftPush(messageKey, JSON.toJSONString(messageVo));
        stringRedisTemplate.expire(messageKey, 7, java.util.concurrent.TimeUnit.DAYS);
        //3.4 检查私信数量是否超过30条，超过则删除最早的一条
        Long size = stringRedisTemplate.opsForList().size(messageKey);
        if (size == null || size > InteractConstant.REDIS_PRIVATE_MESSAGE_MAX_COUNT) {
            stringRedisTemplate.opsForList().rightPop(messageKey);
        }
        return ResponseResult.successResult();
    }

    /**
     * 私信列表
     * @param messageListDto 私信列表dto
     * @return ResponseResult
     */
    @Override
    public ResponseResult<MessageListVo> privateMessageList(MessageListDto messageListDto) {
        //1. 校验参数
        if (messageListDto == null || messageListDto.getFriendId() == null) {
            throw new NullParamException();
        }
        //2. 从redis中获取私信
        Long userId = ThreadLocalUtil.getUserId();
        Long friendId = messageListDto.getFriendId();
        Long lastMessageId = messageListDto.getLastMessageId();
        if (lastMessageId == null) {
            lastMessageId = 0L;
        }
        //3. 查询redis中是否包含私信key
        String messageKey = InteractConstant.REDIS_PRIVATE_MESSAGE_KEY + Math.min(userId, friendId)
                + "-" + Math.max(userId, friendId) + ":";
        //3.1 查询redis中是否包含私信key
        Boolean hasKey = stringRedisTemplate.hasKey(messageKey);
        if (hasKey == null || !hasKey) {
            //3.1.1 redis中没有私信key，从数据库中查询
            return getMessagesInDb(userId, friendId, lastMessageId, messageKey);
        }
        //4. 从redis中获取私信
        List<String> messageList = stringRedisTemplate.opsForList().range(messageKey, 0, -1);
        if (messageList == null || messageList.isEmpty()) {
            // 4.1 redis中没有私信，从数据库中查询
            return getMessagesInDb(userId, friendId, lastMessageId, messageKey);
        }
        // 5. 将私信转换为vo
        List<MessageVo> messageVoList = new ArrayList<>();
        for (String message : messageList) {
            MessageVo messageVo = JSON.parseObject(message, MessageVo.class);
            messageVoList.add(messageVo);
        }
        lastMessageId = messageVoList.get(messageVoList.size() - 1).getMessageId();
        Integer total = messageVoList.size();
        MessageListVo messageListVo = MessageListVo.builder()
                .messages(messageVoList)
                .total(total)
                .lastMessageId(lastMessageId)
                .build();
        return ResponseResult.successResult(messageListVo);
    }

    /**
     * 从数据库中获取私信
     * @param userId 用户id
     * @param friendId 好友id
     * @param lastMessageId 最后一条私信id
     * @param messageKey  redis中的私信key
     * @return ResponseResult
     */
    private ResponseResult<MessageListVo> getMessagesInDb(Long userId, Long friendId,
                                                          Long lastMessageId, String messageKey) {
        // 1. 从数据库中查询私信
        List<PrivateMessage> privateMessageList = privateMessageMapper.selectByUserIdAndFriendId(userId, friendId, lastMessageId);
        // 2. 将私信转换为vo
        List<MessageVo> messageVoList = new ArrayList<>();
        for (PrivateMessage privateMessage : privateMessageList) {
            // 2.1 获得发送者信息
            UserPersonalInfoVo senderInfoVo = userClient.getUserPersonalInfo(privateMessage.getSenderId()).getData();
            UserPersonInfo senderInfo = UserPersonInfo.builder().id(privateMessage.getSenderId()).build();
            BeanUtils.copyProperties(senderInfoVo, senderInfo);
            // 2.2 获得接收者信息
            UserPersonalInfoVo receiverInfoVo = userClient.getUserPersonalInfo(privateMessage.getReceiverId()).getData();
            UserPersonInfo receiverInfo = UserPersonInfo.builder().id(privateMessage.getReceiverId()).build();
            BeanUtils.copyProperties(receiverInfoVo, receiverInfo);
            // 2.3 封装私信vo
            MessageVo messageVo = MessageVo.builder()
                    .messageId(privateMessage.getId())
                    .messageContent(privateMessage.getMessageContent())
                    .sender(senderInfo)
                    .receiver(receiverInfo)
                    .createdTime(privateMessage.getCreateTime())
                    .build();
            messageVoList.add(messageVo);
        }
        lastMessageId = messageVoList.get(messageVoList.size() - 1).getMessageId();
        // 3. 封装私信列表vo
        Integer total = messageVoList.size();
        MessageListVo messageListVo = MessageListVo.builder()
                .messages(messageVoList)
                .total(total)
                .lastMessageId(lastMessageId)
                .build();
        // 4. 将私信存入redis
        for (int i = messageVoList.size() - 1;i >= 0;i--) {
            stringRedisTemplate.opsForList().leftPush(messageKey, JSON.toJSONString(messageVoList.get(i)));
        }
        stringRedisTemplate.expire(messageKey, 1, java.util.concurrent.TimeUnit.DAYS);
        return ResponseResult.successResult(messageListVo);
    }

}

