package com.xuecheng.manage_cms;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TeatRestTemplate {

    @Autowired
    private RestTemplate restTemplate;


    @Test
    public void testRequestCmsConfig(){
        String dataUrl = "http://localhost:31001/cms/config/getmodel/5b816cd2f26bad052b9672bf";
        ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
        Map body = forEntity.getBody();
        System.out.println(body);
    }
}
