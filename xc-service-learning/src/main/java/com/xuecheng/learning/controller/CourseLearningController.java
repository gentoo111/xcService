package com.xuecheng.learning.controller;

import com.xuecheng.api.learning.CourseLearningControllerApi;
import com.xuecheng.framework.domain.learning.response.GetMediaResult;
import com.xuecheng.learning.service.LearningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: szz
 * @Date: 2018/9/23 下午6:51
 * @Version 1.0
 */

@RestController
public class CourseLearningController implements CourseLearningControllerApi {

    @Autowired
    private LearningService learningService;

    @Override
    public GetMediaResult getmedia(@PathVariable String courseId, @PathVariable String teachplanId) {
        return learningService.getmedia(courseId,teachplanId);
    }
}
