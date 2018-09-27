package com.xuecheng.manage_course;


import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.manage_course.dao.CategoryMapper;
import com.xuecheng.manage_course.dao.CourseBaseMapper;
import com.xuecheng.manage_course.dao.CourseBaseReponsitory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestDao {

    @Autowired
    private CourseBaseReponsitory courseBaseReponsitory;

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Test
    public void testJpa(){
        CourseBase one = courseBaseReponsitory.findOne("297e7c7c62b888f00162b8a7dec20000");
        System.out.println(one);
    }

    @Test
    public void testMapper(){
        CourseBase one = courseBaseMapper.selectById("297e7c7c62b888f00162b8a7dec20000");
        System.out.println(one);
    }

    @Test
    public void testMapperXml(){
        CourseBase one = courseBaseMapper.findCourseBaseById("297e7c7c62b888f00162b8a7dec20000");
        System.out.println(one);
    }

    @Test
    public void testMapperListPage(){
        PageHelper.startPage(1, 10);
        Page<CourseInfo> courseListPage = courseBaseMapper.findCourseListPage(null);
        System.out.println(courseListPage.getTotal());
        List<CourseInfo> result = courseListPage.getResult();

        for (CourseInfo courseInfo : result) {
            System.out.println(courseInfo.getName());
        }

    }

    @Test
    public void testMapperCategoryList(){
        CategoryNode list = categoryMapper.findList();
        System.out.println(list.getChildren().size());
    }
}
