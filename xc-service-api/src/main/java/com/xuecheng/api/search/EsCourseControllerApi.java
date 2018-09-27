package com.xuecheng.api.search;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.TeachplanMediaPub;
import com.xuecheng.framework.domain.course.ext.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/9/15 下午5:23
 * @Version 1.0
 */
@Api(value = "课程搜索接口",description = "课程搜索接口")
public interface EsCourseControllerApi {

    final String API_PRE="/search/course";
    @ApiOperation("根据配置信息id查询课程列表,因为传递的查询参数可能会有中文,所以使用post请求防止乱码")
    @GetMapping(API_PRE+"/list/{page}/{size}")
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page,
                                               @PathVariable("size")int size,
                                               CourseSearchParam courseSearchParam);


    @ApiOperation("根据id查询课程信息")
    @GetMapping(value =API_PRE+"/getall/{id}")
    public Map<String, CoursePub> getall(@PathVariable("id") String id);

    @ApiOperation("根据课程计划查询媒资信息")
    @GetMapping(value=API_PRE+"/getmedia/{teachplanId}")
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId);
}
