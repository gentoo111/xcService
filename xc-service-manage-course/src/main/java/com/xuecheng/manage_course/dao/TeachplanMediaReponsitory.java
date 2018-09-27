package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachplanMediaReponsitory extends JpaRepository<TeachplanMedia,String> {

    //根据课程id查询课程计划媒资信息
    List<TeachplanMedia> findByCourseId(String courseId);
}
