package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午2:26
 * @Version 1.0
 */
@Mapper
public interface TeachplanMapper {

    public TeachplanNode findList(String courseId);
}
