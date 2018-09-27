package com.xuecheng.ucenter.controller;

import com.xuecheng.api.ucenter.UcenterControllerApi;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: szz
 * @Date: 2018/9/25 下午4:08
 * @Version 1.0
 */

@RestController
public class UcenterController implements UcenterControllerApi {

    @Autowired
    private UserService userService;

    @Override
    public XcUserExt getUserext(@RequestParam("username") String username){
        return userService.getUserExt(username);
    }
}
