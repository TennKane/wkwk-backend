package com.wkwk.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gtkang.feignapi.clients.InteractClient;
import com.wkwk.constant.UserConstant;
import com.wkwk.mapper.UserMapper;
import com.wkwk.service.UserInfoService;
import com.wkwk.exception.ErrorParamException;
import com.wkwk.exception.UserNotExitedException;
import com.wkwk.exception.UserNotLoginException;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.dto.UserPersonInfoDto;
import com.wkwk.user.pojo.User;
import com.wkwk.user.vo.UserHomePageVo;
import com.wkwk.user.vo.UserPersonalInfoVo;
import com.wkwk.utils.QiniuOssUtil;
import com.wkwk.utils.ThreadLocalUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 用户信息服务实现类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
@Log4j2
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private InteractClient interactClient;

    /**
     * 获取用户个人信息
     * @param userId 用户id
     * @return ResponseResult
     */
    @Override
    public ResponseResult<UserPersonalInfoVo> getUserPersonalInfo(Long userId) {
        // 1. 获取到用户的id,如果为空,则说明是查自己，从ThreadLocal中获取
        if (userId == null){
            userId = ThreadLocalUtil.getUserId();
        }
        log.info("用户个人信息查询: {}", userId);
        // 1.1 校验用户id是否为空
        if (userId == null){
            throw new UserNotLoginException();
        }
        //2.1从redis获取用户信息
        String userInfoRedis = stringRedisTemplate.opsForValue().get(UserConstant.REDIS_USER_INFO + userId);
        if (userInfoRedis != null){
            // redis 中存在用户信息，直接返回
            UserPersonalInfoVo userPersonalInfoVo = JSON.parseObject(userInfoRedis, UserPersonalInfoVo.class);
            return ResponseResult.successResult(userPersonalInfoVo);
        }
        // 2. 根据用户id查询用户信息, 只需要 用户名、头像、签名
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("username", "image", "signature").eq("id", userId);
        User user = userMapper.selectOne(queryWrapper);
        // 2.1 校验用户信息是否为空
        if (user == null){
            throw new UserNotExitedException();
        }
        // 2.2 拷贝属性
        UserPersonalInfoVo userPersonalInfoVo = new UserPersonalInfoVo();
        BeanUtils.copyProperties(user, userPersonalInfoVo);
        userPersonalInfoVo.setId(String.valueOf(userId));
        // 将用户信息存入 redis
        stringRedisTemplate.opsForValue().set(UserConstant.REDIS_USER_INFO + userId, JSON.toJSONString(userPersonalInfoVo),
                UserConstant.REDIS_USER_INFO_TTL, TimeUnit.SECONDS);

        // 3. 封装返回结果
        return ResponseResult.successResult(userPersonalInfoVo);
    }

    /**
     * 更新用户个人信息
     * @param userPersonInfoDto 用户个人信息
     * @return ResponseResult
     */
    @Override
    public ResponseResult updateUserPersonalInfo(UserPersonInfoDto userPersonInfoDto) {
        log.info("用户个人信息更新: {}", userPersonInfoDto);
        // 0. 校验参数
        if (userPersonInfoDto == null || (userPersonInfoDto.getSignature() == null &&
                userPersonInfoDto.getImage() == null && userPersonInfoDto.getUsername() == null)){
            throw new ErrorParamException("参数不能为空！");
        }
        if (userPersonInfoDto.getUsername().length() > 15) {
            throw new ErrorParamException("用户名不能超过15个字符！");
        }
        if (userPersonInfoDto.getSignature().length() > 100) {
            throw new ErrorParamException("签名不能超过100个字符！");
        }
        // 1. 获取到用户的id
        Long userId = ThreadLocalUtil.getUserId();

        // 1.1 校验用户id是否为空
        if (userId == null){
            throw new UserNotLoginException();
        }
        // 2. 更新用户信息
        //从redis中获取删除信息
        stringRedisTemplate.delete(UserConstant.REDIS_USER_INFO + userId);
        User user = new User();
        BeanUtils.copyProperties(userPersonInfoDto, user);
        user.setId(userId);
        try {
            userMapper.updateById(user);
        } catch (Exception e) {
            log.error("用户信息更新失败: {}", e.getMessage());
            throw new ErrorParamException("用户不存在！");
        }
        return ResponseResult.successResult();
    }

    /**
     * 上传用户头像
     * @param imageFile 用户头像文件
     * @return ResponseResult<String> 图片地址
     */
    @Override
    public ResponseResult<String> uploadUserImage(MultipartFile imageFile) {
        //1. 获取文件名
        //1.1 校验文件是否为空
        if (imageFile == null){
            throw new ErrorParamException("头像不能为空！");
        }
        String fileName = imageFile.getOriginalFilename();
        if (fileName == null){
            throw new ErrorParamException("不支持的文件类型！");
        }
        //2. 分别获取文件后缀与文件名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //2.1 检查是否是想要的文件类型
        if (!".jpg".equals(suffix) && !".png".equals(suffix) && !".jpeg".equals(suffix) ){
            throw new ErrorParamException("不支持的文件类型！");
        }
        //2.2 拼接文件名
        String name = UUID.randomUUID() + suffix;
        //3. 上传文件
        String url = null;
        try {
            url = QiniuOssUtil.upload(imageFile.getBytes(), name);
        } catch (Exception e) {
            log.error("上传头像失败: {}", e.getMessage());
        }
        return ResponseResult.successResult(url);
    }

    /**
     *  获取用户主页信息
     * @param userId 用户id
     * @return ResponseResult<UserHomePageVo> 用户主页信息
     */
    @Override
    public ResponseResult<UserHomePageVo> getUserHomePage(Long userId) {
        // 1. 获取基本信息
        ResponseResult<UserPersonalInfoVo> userPersonalInfoVoResponseResult = getUserPersonalInfo(userId);
        if (userPersonalInfoVoResponseResult == null || userPersonalInfoVoResponseResult.getData() == null){
            throw new UserNotExitedException();
        }
        UserPersonalInfoVo userPersonalInfo = userPersonalInfoVoResponseResult.getData();
        // 2. 获取关注数
        Integer followNum = interactClient.getFollowNum(userId).getData();
        // 3. 获取粉丝数
        Integer fansNum = interactClient.getFansNum(userId).getData();
        // TODO 获取被点赞数以及作品数
        return null;
    }
}
