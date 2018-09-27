package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachplanReponsitory extends JpaRepository<Teachplan,String> {
    //当parentId为0的时候,只会返回一条记录
    List<Teachplan> findByCourseidAndParentid(String courseId, String parentId);
}
