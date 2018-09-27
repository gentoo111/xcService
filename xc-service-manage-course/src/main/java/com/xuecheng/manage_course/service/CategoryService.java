package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午2:49
 * @Version 1.0
 */
@Service
public class CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    public CategoryNode findList(){
        return categoryMapper.findList();
    }
}
