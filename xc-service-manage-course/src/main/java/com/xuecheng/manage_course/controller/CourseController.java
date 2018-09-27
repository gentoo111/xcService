package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.CourseView;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午1:41
 * @Version 1.0
 */
@RestController
public class CourseController extends BaseController implements CourseControllerApi {

    @Autowired
    private CourseService courseService;

    @Override
    //指定拥有哪些权限可以访问此方法
    //@PreAuthorize("hasAuthority('course_find_list')")
    public QueryResponseResult<CourseInfo> findCourseList(@PathVariable("page") int page,
                                                          @PathVariable("size") int size,
                                                          CourseListRequest courseListRequest){
        //当前用户所属公司
        XcOauth2Util xcOauth2Util=new XcOauth2Util();
        XcOauth2Util.UserJwt userJwtFromHeader = xcOauth2Util.getUserJwtFromHeader(request);

        String companyId=userJwtFromHeader.getCompanyId();

        return courseService.findCourseList(page,size,courseListRequest);
    }

    @Override
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        return courseService.addCourseBase(courseBase);
    }

    @Override
    public AddCourseResult updateCoursebase(@PathVariable String id,@RequestBody CourseBase courseBase){
        return courseService.updateCoursebase(courseBase);
    }

    @Override
    public CourseBase getCoursebaseById(@PathVariable String id) {
        return courseService.getCoursebaseById(id);
    }

    @Override
    //@PreAuthorize("hasAuthority('teachplan_find_list')")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    @Override
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan){
        return courseService.addTeachplan(teachplan);
    }

    @Override
    public CourseMarket getCourseMarketById(@PathVariable String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    @Override
    public ResponseResult updateCourseMarket(@PathVariable String id, @RequestBody CourseMarket courseMarket) {
        CourseMarket courseMarket_u = courseService.updateCourseMarket(id, courseMarket);
        if (courseMarket_u != null) {
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

    @Override
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.saveCoursePic(courseId,pic);
    }

    @Override
    //@PreAuthorize("hasAuthority('coursepic_find_list')")
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId) {
        return courseService.findCoursepicList(courseId);
    }

    @Override
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        return courseService.deleteCoursePic(courseId);
    }

    @Override
    public CourseView courseview(@PathVariable("id") String id) {
        return courseService.getCourseView(id);
    }

    @Override
    public CoursePublishResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    @Override
    public CoursePublishResult publish(@PathVariable("id") String id){
        return courseService.publish(id);
    }

    @Override
    public ResponseResult savemedia(@RequestBody TeachplanMedia teachplanMedia){
        return courseService.savemedia(teachplanMedia);
    }
}
