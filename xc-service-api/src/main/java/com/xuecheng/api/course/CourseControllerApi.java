package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by szz on 2018/6/23.
 */
@Api(value="课程管理的接口",description="课程管理的接口，提供课程添加、删除、修改、查询操作")
public interface CourseControllerApi {
    final String API_PRE = "/course";

    @ApiOperation(value = "分页查询课程列表")
    @GetMapping(API_PRE+"/coursebase/list/{page}/{size}")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page,
                                                          @PathVariable("size") int size,
                                                          CourseListRequest courseListRequest);

    @ApiOperation(value = "新增课程基本信息")
    @PostMapping(API_PRE+"/coursebase/add")
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase);

    @ApiOperation(value = "更新课程基本信息")
    @PutMapping(API_PRE+"/coursebase/update/{id}")
    public AddCourseResult updateCoursebase(@PathVariable("id") String id,@RequestBody CourseBase courseBase);

    @ApiOperation(value = "根据主键查询课程基本信息")
    @GetMapping(API_PRE + "/coursebase/get/{id}")
    public CourseBase getCoursebaseById(@PathVariable("id") String id);

    @ApiOperation(value = "查询课程计划")
    @GetMapping(API_PRE + "/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId);

    @ApiOperation(value = "添加课程计划")
    @PostMapping(API_PRE + "/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan);

    @ApiOperation("获取课程营销信息")
    @GetMapping(API_PRE+"/coursemarket/get/{courseId}")
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId);

    @ApiOperation("更新课程营销信息")
    @PutMapping(API_PRE+"/coursemarket/update/{id}")
    public ResponseResult updateCourseMarket(@PathVariable("id") String id,@RequestBody CourseMarket courseMarket);

    //保存图片地址到course_pic
    @ApiOperation("添加课程图片")
    @PostMapping(API_PRE+"/coursepic/add")
    public ResponseResult addCoursePic(@RequestParam("courseId")String courseId,@RequestParam("pic")String pic);

    //根据课程id 查询图片
    @ApiOperation("获取课程图片")
    @GetMapping(API_PRE+"/coursepic/list/{courseId}")
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId);

    @ApiOperation("删除课程图片")
    @DeleteMapping(API_PRE+"/coursepic/delete")
    public ResponseResult deleteCoursePic(@RequestParam("courseId")String courseId);

    @ApiOperation("课程视图查询")
    @GetMapping(API_PRE+"/courseview/{id}")
    public CourseView courseview(@PathVariable("id") String id);

    @ApiOperation("课程预览")
    @PostMapping(API_PRE+"/preview/{id}")
    public CoursePublishResult preview(@PathVariable("id") String id);

    @ApiOperation("发布课程")
    @PostMapping(API_PRE+"/publish/{id}")
    public CoursePublishResult publish(@PathVariable("id") String id);


    @ApiOperation(value = "保存媒资信息")
    @PostMapping(API_PRE+"/savemedia")
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia);

}
