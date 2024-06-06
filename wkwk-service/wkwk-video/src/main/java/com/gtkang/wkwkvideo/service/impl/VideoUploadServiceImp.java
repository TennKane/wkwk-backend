package com.gtkang.wkwkvideo.service.impl;

import com.gtkang.wkwkvideo.mapper.VideoMapper;
import com.gtkang.wkwkvideo.service.VideoUploadService;
import com.wkwk.exception.DbOperationException;
import com.wkwk.exception.QiniuException;
import com.wkwk.response.ResponseResult;
import com.wkwk.utils.QiniuOssUtil;
import com.wkwk.utils.ThreadLocalUtil;
import com.wkwk.video.dto.VideoPublishDto;
import com.wkwk.video.pojo.Video;
import com.wkwk.video.vo.VideoUploadVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
@Slf4j
public class VideoUploadServiceImp implements VideoUploadService {
    @Resource
    VideoMapper videoMapper;


    /**
     * 视频发布
     */
    @Override
    public ResponseResult publish(VideoPublishDto videoPublishDto){
        //得到userId
        Long userId = ThreadLocalUtil.getUserId();
        //参数校验
        if(userId==null){
            return ResponseResult.errorResult("未登录");
        }
        if(videoPublishDto.getVideoUrl()==null){
            return ResponseResult.errorResult("视频路径为空");
        }
        //分区大于9不合规范，设为默认0
        if(videoPublishDto.getSection()>9){
            videoPublishDto.setSection(0);
        }
        //保存视频到数据库
        Video video= Video.builder().
                videoUrl(videoPublishDto.getVideoUrl()).
                coverUrl(videoPublishDto.getCoverUrl()).
                authorId(userId).
                //section默认为0
                        section(videoPublishDto.getSection()).
                //标题默认为"视频"
                        title(videoPublishDto.getTitle()!=null?videoPublishDto.getTitle():"视频").
                //状态默认为正常
                        status(0).
                build();
        try {
            videoMapper.insert(video);
        }catch (Exception e){
            log.error("保存视频信息失败{}",e);
            throw new DbOperationException("保存视频信息失败！");
        }

        VideoUploadVo videoUploadVo=new VideoUploadVo();
        BeanUtils.copyProperties(video,videoUploadVo);

        return ResponseResult.successResult(videoUploadVo);

    }
    @Override
    public ResponseResult upload(MultipartFile file){
        log.info("文件上传 ：{}",file);
        String filePath="";
        //参数校验
        if(file==null){
            log.error("文件为空");
            return ResponseResult.errorResult("文件为空" );
        }
        else{
            //生成文件名并上传文件
            try {
                filePath= QiniuOssUtil.upload(file.getBytes(),dealFileName(file));
            }catch (Exception e){
                log.error("文件上传失败{}",e);
                throw new QiniuException();
            }
        }
        return ResponseResult.successResult(filePath);
    }

    /**
     * 处理文件名 uuid+文件名+后缀
     * @param file
     * @return
     */
    private String dealFileName(MultipartFile file){
        //原始文件名
        String originalFilename = file.getOriginalFilename();
        //截取原始文件名后缀
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String objectName= UUID.randomUUID().toString()+ extension;
        return objectName;
    }
}
