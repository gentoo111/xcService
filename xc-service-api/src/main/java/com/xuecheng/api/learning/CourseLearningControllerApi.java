package com.xuecheng.api.learning;

import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Created by mrt on 2018/7/14.
 */
@Api(value = "录播课程学习管理",description = "录播课程学习管理")
public interface CourseLearningControllerApi {

    final String API_PRE = "/learning/course";

    @GetMapping(API_PRE+"/getmedia/{courseId}/{teachplanId}")
    @ApiOperation("获取课程学习地址")
    public GetMediaResult getmedia(@PathVariable String courseId, @PathVariable String teachplanId);

}
