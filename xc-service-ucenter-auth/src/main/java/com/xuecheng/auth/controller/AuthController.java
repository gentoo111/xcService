package com.xuecheng.auth.controller;

import com.xuecheng.api.auth.AuthControllerApi;
import com.xuecheng.auth.service.AuthService;
import com.xuecheng.framework.domain.ucenter.ext.AuthToken;
import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import com.xuecheng.framework.domain.ucenter.request.LoginRequest;
import com.xuecheng.framework.domain.ucenter.response.JwtResult;
import com.xuecheng.framework.domain.ucenter.response.LoginResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.framework.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/9/24 下午8:49
 * @Version 1.0
 */
@RestController
public class AuthController extends BaseController implements AuthControllerApi {

    @Value("${auth.clientId}")
    String clientId;
    @Value("${auth.clientSecret}")
    String clientSecret;
    @Value("${auth.cookieDomain}")
    String cookieDomain;
    @Value("${auth.cookieMaxAge}")
    int cookieMaxAge;
    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;
    @Autowired
    private AuthService authService;

    @Override
    public LoginResult login(LoginRequest loginRequest) {
        //申请令牌
        AuthToken authToken=authService.login(loginRequest.getUsername(), loginRequest.getPassword(), clientId, clientSecret);
        String access_token = authToken.getAccess_token();

        //将令牌存储到cookie
        saveTokenToCookie(access_token);
        return new LoginResult(CommonCode.SUCCESS,access_token);
    }

    @Override
    public JwtResult userjwt() {
        //从cookie中取出token
        String token = getTokenFromCookie();
        //根据token区查询redis中的jwt
        UserTokenStore userTokenStore=authService.getUserToken(token);
        return new JwtResult(CommonCode.SUCCESS,userTokenStore.getJwt_token());
    }

    @Override
    public ResponseResult logout() {
        //从cookie中取出token
        String tokenFromCookie = getTokenFromCookie();

        //清除cookie
        clearToken();

        //清除redis
        authService.deleteTokenFromRedis(tokenFromCookie);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //清除cookie
    private void clearToken(){
        CookieUtil.addCookie(response,cookieDomain,"/","uid","",0,false);
    }

    //存储令牌到cookie
    private void saveTokenToCookie(String token){
        CookieUtil.addCookie(response,cookieDomain,"/","uid",token,cookieMaxAge,false);
    }

    //取出cookie中名称为uid的值
    private String getTokenFromCookie(){
        Map<String, String> stringMap = CookieUtil.readCookie(request, "uid");
        return stringMap.get("uid");
    }
}
