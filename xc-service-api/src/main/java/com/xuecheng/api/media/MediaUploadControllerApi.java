package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.response.CheckChunkResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: szz
 * @Date: 2018/9/19 上午11:14
 * @Version 1.0
 * 1、上传注册，检查上传环境
 * 检查文件是否上传，已上传则直接返回。
 * 检查文件上传路径是否存在，不存在则创建。
 * 2、分块检查
 * 检查分块文件是否上传，已上传则返回true。
 * 未上传则检查上传路径是否存在，不存在则创建。
 * 3、分块上传
 * 将分块文件上传到指定的路径。
 * 4、合并分块
 * 将所有分块文件合并为一个文件。
 * 在数据库记录文件信息。
 */
public interface MediaUploadControllerApi {

    String API_PRE = "/media/upload";

    @ApiOperation(value = "文件上传前注册")
    @PostMapping(API_PRE + "/register")
    public ResponseResult register(
            @RequestParam("fileMd5")String fileMd5,
            @RequestParam("fileName")String fileName,
            @RequestParam("fileSize")Long fileSize,
            @RequestParam("mimetype")String mimetype,
            @RequestParam("fileExt")String fileExt

    );


    @ApiOperation(value = "分块检查")
    @PostMapping(API_PRE +"/checkchunk")
    public CheckChunkResult checkchunk(
            @RequestParam("fileMd5")String fileMd5,
            @RequestParam("chunk")String chunk,
            @RequestParam("chunkSize")String chunkSize
    );

    @ApiOperation(value = "分块上传")
    @PostMapping(API_PRE +"/uploadchunk")
    public ResponseResult uploadchunk(
            @RequestParam("file")MultipartFile file,
            @RequestParam("fileMd5")String fileMd5,
            @RequestParam("chunk")String chunk
            );

    @ApiOperation(value = "合并文件")
    @PostMapping(API_PRE +"/mergechunks")
    public ResponseResult mergechunks(
            @RequestParam("fileMd5")String fileMd5,
            @RequestParam("fileName")String fileName,
            @RequestParam("fileSize")Long fileSize,
            @RequestParam("mimetype")String mimetype,
            @RequestParam("fileExt")String fileExt
    );

}
