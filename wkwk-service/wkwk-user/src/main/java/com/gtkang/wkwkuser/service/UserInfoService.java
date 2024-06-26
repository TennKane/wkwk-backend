package com.gtkang.wkwkuser.service;

import com.wkwk.response.ResponseResult;
import com.wkwk.user.dto.UserPersonInfo;
import com.wkwk.user.vo.UserPersonalInfoVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户信息服务接口
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
public interface UserInfoService {
    /**
     * 获取用户个人信息
     * @param userId 用户id
     * @return ResponseResult<UserPersonalInfoVo> 用户个人信息
     */
    ResponseResult<UserPersonalInfoVo> getUserPersonalInfo(Long userId);

    /**
     * 更新用户个人信息
     * @param userPersonInfo 用户个人信息
     * @return ResponseResult 更新结果
     */
    ResponseResult updateUserPersonalInfo(UserPersonInfo userPersonInfo);

    /**
     * 上传用户头像
     * @param imageFile 用户头像文件
     * @return ResponseResult<String> 图片地址
     */
    ResponseResult<String> uploadUserImage(MultipartFile imageFile);
}
