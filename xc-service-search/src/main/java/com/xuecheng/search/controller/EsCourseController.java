package com.xuecheng.search.controller;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.search.service.EsCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/9/15 下午5:28
 * @Version 1.0
 */
@RestController
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    private EsCourseService esCourseService;

    @Override
    public QueryResponseResult<CoursePub> list(@PathVariable("page") int page,
                                               @PathVariable("size")int size,
                                               CourseSearchParam courseSearchParam){
        return esCourseService.list(page,size,courseSearchParam);
    }

    @Override
    public Map<String, CoursePub> getall(@PathVariable("id") String id) {
        return esCourseService.getall(id);
    }

    @Override
    public TeachplanMediaPub getmedia(@PathVariable("teachplanId") String teachplanId) {
        String[] teachplanIds = new String[]{teachplanId};

        List<TeachplanMediaPub> teachplanMediaPubList = esCourseService.getall(teachplanIds);
        if(teachplanMediaPubList!=null && teachplanMediaPubList.size()>0){
            return teachplanMediaPubList.get(0);
        }
        return new TeachplanMediaPub();
    }
}
