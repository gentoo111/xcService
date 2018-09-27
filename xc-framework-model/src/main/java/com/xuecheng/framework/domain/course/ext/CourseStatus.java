package com.xuecheng.framework.domain.course.ext;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午9:06
 * @Version 1.0
 */
public enum CourseStatus {
    MAKING("202001"),PUBLISH("202002"),OFFLINE("202003");

    private final String name;

    private CourseStatus(String name) {
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
