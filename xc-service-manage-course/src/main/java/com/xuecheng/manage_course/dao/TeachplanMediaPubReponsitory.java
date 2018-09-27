package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanMediaPub;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachplanMediaPubReponsitory extends JpaRepository<TeachplanMediaPub,String> {

    //根据课程id删除数据
    int deleteByCourseId(String courseId);
}
