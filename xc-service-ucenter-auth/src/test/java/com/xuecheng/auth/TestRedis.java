package com.xuecheng.auth;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @Author: szz
 * @Date: 2018/9/24 下午8:01
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestRedis {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void testRedis(){
        String key = "token:464c3dbc-9ba4-4b40-9217-d6b0a6a9918b";
        String jwt = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHQiOiIxIiwicm9sZXMiOiJyMDEscjAyIiwibmFtZSI6Iml0Y2FzdCIsImlkIjoiMTIzIn0.HnjWjEX4Wk2NL1zcAMsxbvuwiPpyM4UOIPLBk6N3l1VTtOxLcpdV3ht4I668ljmGyQDezb6MVEVx4LqjME7YhNjsm1yrry-TWsn3IO274e5YBkRAPn02NQ9A-oy0CTe37IymuVSILpIcD85FC0adM9kg_cSICM9uLcfYJdXd4pvUM3W-ifRrRP-AE-kBl-f2oAdyJDWqGyXEMC1tM-S8YLdb383kCgFrX5XxU2bJGl2nSMOXs7KpRCPycfYgDj3UOu3UjEwqJOnIDJYCL9VmcG-lL5L_6XjXl5MsnoJ2Dtog3muOfj4RuzXGtLTViDOJElCGpkkNuQxKwHHPXZaFAA";

        stringRedisTemplate.boundValueOps(key).set(jwt,60,TimeUnit.SECONDS);

        //校验
        Long expire = stringRedisTemplate.getExpire(key);

        String s = stringRedisTemplate.boundValueOps(key).get();
        System.out.println(s);


    }
}
