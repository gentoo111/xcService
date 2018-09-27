package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseBaseReponsitory extends JpaRepository<CourseBase,String> {
}
