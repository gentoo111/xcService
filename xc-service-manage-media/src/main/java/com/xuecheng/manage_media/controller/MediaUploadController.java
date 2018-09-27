package com.xuecheng.manage_media.controller;

import com.xuecheng.api.media.MediaUploadControllerApi;
import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_media.service.MediaUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: szz
 * @Date: 2018/9/19 上午11:43
 * @Version 1.0
 */

@RestController
public class MediaUploadController implements MediaUploadControllerApi {

    @Autowired
    private MediaUploadService mediaUploadService;

    @Override
    public ResponseResult register(@RequestParam("fileMd5")String fileMd5,
            @RequestParam("fileName")String fileName,
            @RequestParam("fileSize")Long fileSize,
            @RequestParam("mimetype")String mimetype,
            @RequestParam("fileExt")String fileExt){
        return mediaUploadService.register(fileMd5,fileName,fileSize,mimetype,fileExt);
    }

    @Override
    public CheckChunkResult checkchunk(@RequestParam("fileMd5")String fileMd5,
                                       @RequestParam("chunk")String chunk,
                                       @RequestParam("chunkSize")String chunkSize) {
        return mediaUploadService.checkchunk(fileMd5,chunk,chunkSize);
    }

    @Override
    public ResponseResult uploadchunk(@RequestParam("file")MultipartFile file,
                                      @RequestParam("fileMd5")String fileMd5,
                                      @RequestParam("chunk")String chunk) {
        return mediaUploadService.uploadchunk(file,fileMd5,chunk);
    }

    @Override
    public ResponseResult mergechunks(@RequestParam("fileMd5")String fileMd5,
                                      @RequestParam("fileName")String fileName,
                                      @RequestParam("fileSize")Long fileSize,
                                      @RequestParam("mimetype")String mimetype,
                                      @RequestParam("fileExt")String fileExt) {
        return mediaUploadService.mergechunks(fileMd5,fileName,fileSize,mimetype,fileExt);
    }
}
