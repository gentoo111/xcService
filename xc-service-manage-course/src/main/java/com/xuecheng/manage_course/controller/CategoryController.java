package com.xuecheng.manage_course.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午2:47
 * @Version 1.0
 */
@RestController
public class CategoryController implements CategoryControllerApi {

    @Autowired
    private CategoryService categoryService;

    @Override
    public CategoryNode findList() {
        return categoryService.findList();
    }
}
