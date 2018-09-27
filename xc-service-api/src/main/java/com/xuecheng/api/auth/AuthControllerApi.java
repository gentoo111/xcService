package com.xuecheng.api.auth;

import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @Author: szz
 * @Date: 2018/9/24 下午8:46
 * @Version 1.0
 */
@Api(value = "用户认证",description = "用户认证接口")
public interface AuthControllerApi {

    @ApiOperation("登录")
    @PostMapping("/userlogin")
    public LoginResult login(LoginRequest loginRequest);

    @ApiOperation("查询用户的jwt令牌")
    @GetMapping("/userjwt")
    public JwtResult userjwt();

    @ApiOperation(("用户退出"))
    @PostMapping("userlogout")
    public ResponseResult logout();

}
