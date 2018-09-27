package com.xuecheng.manage_course;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @Author: szz
 * @Date: 2018/9/13 上午10:25
 * @Version 1.0
 */
public class TestStream implements Serializable {

    /*
        java.util.stream.Stream<T>是Java 8新加入的最常用的流接口。（这并不是一个函数式接口。）
        获取一个流非常简单，有以下几种常用的方式：
            - 所有的Collection集合都可以通过stream默认方法获取流；
                default Stream<E> stream​()
            - Stream接口的静态方法of可以获取数组对应的流。
                static <T> Stream<T> of​(T... values)
                参数是一个可变参数,那么我们就可以传递一个数组
     */

    public static void main(String[] args) {

        Stream<String> stream = Stream.of("张三", "李四", "王五", "赵六", "田七");

        Stream<String> stream2 = stream.filter((String s) -> {
            return s.startsWith("a");
        });
        stream2.forEach(s-> System.out.println(s));

    }
}
