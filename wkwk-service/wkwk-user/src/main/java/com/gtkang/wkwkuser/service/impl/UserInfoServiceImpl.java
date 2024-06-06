package com.gtkang.wkwkuser.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.gtkang.wkwkuser.mapper.UserMapper;
import com.gtkang.wkwkuser.service.UserInfoService;
import com.wkwk.exception.ErrorParamException;
import com.wkwk.exception.UserNotExitedException;
import com.wkwk.exception.UserNotLoginException;
import com.wkwk.response.ResponseResult;
import com.wkwk.user.dto.UserPersonInfo;
import com.wkwk.user.pojo.User;
import com.wkwk.user.vo.UserPersonalInfoVo;
import com.wkwk.utils.QiniuOssUtil;
import com.wkwk.utils.ThreadLocalUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 用户信息服务实现类
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
@Log4j2
public class UserInfoServiceImpl implements UserInfoService {

    @Resource
    private UserMapper userMapper;

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
        // 3. 封装返回结果
        return ResponseResult.successResult(userPersonalInfoVo);
    }

    /**
     * 更新用户个人信息
     * @param userPersonInfo 用户个人信息
     * @return ResponseResult
     */
    @Override
    public ResponseResult updateUserPersonalInfo(UserPersonInfo userPersonInfo) {
        // 0. 校验参数
        if (userPersonInfo == null || (userPersonInfo.getSignature() == null &&
                userPersonInfo.getImage() == null && userPersonInfo.getUsername() == null)){
            throw new ErrorParamException("参数不能为空！");
        }
        // 1. 获取到用户的id
        Long userId = ThreadLocalUtil.getUserId();
        log.info("用户个人信息更新: {}", userPersonInfo);
        // 1.1 校验用户id是否为空
        if (userId == null){
            throw new UserNotLoginException();
        }
        // 2. 更新用户信息
        User user = new User();
        BeanUtils.copyProperties(userPersonInfo, user);
        user.setId(userId);
        userMapper.updateById(user);
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
        String name = fileName.substring(0, fileName.lastIndexOf("."));
        //2.2 拼接文件名
        name = UUID.randomUUID() + suffix;
        //3. 上传文件
        String url = null;
        try {
            url = QiniuOssUtil.upload(imageFile.getBytes(), name);
        } catch (Exception e) {
            log.error("上传头像失败: {}", e.getMessage());
        }
        return ResponseResult.successResult(url);
    }
}
