package com.xuecheng.manage_course;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/9/9 下午9:20
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRibbon {

    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testRobbin(){
        //String url = "http://localhost:31001/cms/page/get/5b8027c76d9b3206907fd104";
        String url = "http://xc-service-manage-cms/cms/page/get/5b8027c76d9b3206907fd104";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(url, Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);

    }
}
