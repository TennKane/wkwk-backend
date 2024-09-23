package com.gtkang.wkwkvideo.service.impl;

import com.alibaba.fastjson2.JSON;
import com.gtkang.wkwkvideo.mapper.VideoMapper;
import com.gtkang.wkwkvideo.service.DbOpsService;
import com.gtkang.wkwkvideo.service.VideoUploadService;
import com.wkwk.constant.VideoConstant;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
@Service
@Slf4j
public class VideoUploadServiceImpl implements VideoUploadService {
    @Resource
    VideoMapper videoMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    DbOpsService dbOpsService;


    /**
     * 视频发布
     */
    @Transactional
    @Override
    public ResponseResult publish(VideoPublishDto videoPublishDto){
        //得到userId
        Long userId = ThreadLocalUtil.getUserId();
        //参数校验
        if(userId==null){
            return ResponseResult.errorResult("未登录");
        }
        if(videoPublishDto.getVideoUrl() == null || videoPublishDto.getVideoUrl().isEmpty()){
            return ResponseResult.errorResult("视频路径为空");
        }
        //默认封面
        if(videoPublishDto.getCoverUrl()==null||videoPublishDto.getCoverUrl().isEmpty()){
            videoPublishDto.setCoverUrl(videoPublishDto.getCoverUrl()+"?vframe/jpg/offset/0");
        }
        //分区大于9不合规范，设为默认0
        if(videoPublishDto.getSection()>9){
            videoPublishDto.setSection(0);
        }
        if (videoPublishDto.getCoverUrl() == null || videoPublishDto.getCoverUrl().isEmpty()){
            videoPublishDto.setCoverUrl(videoPublishDto.getVideoUrl() + "?vframe/jpg/offset/0");
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
                likes(0L).
                collects(0L).
                comments(0L).
                build();
        try {
            videoMapper.insert(video);
            //将对象存储在redis中的list里key为前缀+id/11
            String key=VideoConstant.VIDEO_LIST_KEY+video.getId()/11;
            stringRedisTemplate.opsForList().leftPush(key, JSON.toJSONString(video));
            String x=JSON.toJSONString(video);
            System.out.println(x);
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
                filePath=QiniuOssUtil.upload(file.getBytes(),dealFileName(file));
            }catch (Exception e){
                log.error("文件上传失败{}",e);
                throw new QiniuException();
            }
        }
        return ResponseResult.successResult(filePath);
    }

    /**
     * 获取视频,一次得10个
     * 在发布视频时根据VIDEO_LIST_KEY+视频id/11将视频存入相应的list中，这样每个list都会有10条视频
     * 在每个用户观看视频时根据NOW_List_ID+useId键在redis中取出对应id的list
     * @return
     */
    @Override
    public ResponseResult getVideos(Integer lastVideoId) {
        //根据listId取出对应视频
        List<String> list = stringRedisTemplate.opsForList().range(VideoConstant.VIDEO_LIST_KEY + lastVideoId, 0, -1);
        if(list==null||list.isEmpty()){
            return ResponseResult.errorResult("到底了");
        }
        List <Video>result=new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            //将redis中的数据反序列化为对象
            Video video = JSON.parseObject(list.get(i), Video.class);
            //得到videoId
            Long videoId = video.getId();
            //获取点赞数，评论数，收藏数
            String likes = stringRedisTemplate.opsForValue().get(VideoConstant.STRING_LIKE_KEY + videoId);
            if(likes==null){
                //要是发现redis中like字段过期，
                // 则从数据库中查询数据返回，并同时把此视频所有字段刷新到redis
                likes=dbOpsService.getSumFromDb(videoId).toString();
            }
            String collects = stringRedisTemplate.opsForValue().get(VideoConstant.STRING_COLLECT_KEY + videoId);
            String comments = stringRedisTemplate.opsForValue().get(VideoConstant.STRING_COMMENT_KEY + videoId);
            //如果当前key为空，直接从数据库中取
            video.setLikes(Long.parseLong(likes));
            video.setCollects(Long.parseLong(collects));
            video.setComments(Long.parseLong(comments));
            result.add(video);
        }
        return ResponseResult.successResult(result);
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
