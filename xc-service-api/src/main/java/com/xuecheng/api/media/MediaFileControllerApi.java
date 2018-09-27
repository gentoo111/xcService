package com.xuecheng.api.media;

import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by mrt on 2018/7/12.
 */
@Api(value = "媒体文件管理",description = "媒体文件管理接口",tags = {"媒体文件管理接口"})
public interface MediaFileControllerApi {
    final String API_PRE = "/media/file";


    @ApiOperation("查询文件列表")
    @GetMapping(API_PRE+"/list/{page}/{size}")
    public QueryResponseResult<MediaFile> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryMediaFileRequest queryMediaFileRequest) ;

    @ApiOperation("开始处理某个文件")
    @PostMapping(API_PRE+"/process/{id}")
    public ResponseResult process(@PathVariable("id") String id) ;

}
