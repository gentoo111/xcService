package com.xuecheng.govern.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.sun.org.apache.regexp.internal.RE;
import com.xuecheng.framework.domain.ucenter.ext.UserTokenStore;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.CookieUtil;
import com.xuecheng.govern.service.AuthService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/9/26 上午9:56
 * @Version 1.0
 */
@Component
public class LoginFilter extends ZuulFilter {

    @Autowired
    private AuthService authService;
    /**
     * to classify a filter by type. Standard types in Zuul are "pre" for pre-routing filtering,
     * "route" for routing to an origin, "post" for post-routing filters, "error" for error handling.
     * We also support a "static" type for static responses see  StaticResponseFilter.
     * Any filterType made be created or added and run by calling FilterProcessor.runFilters(type)
     *
     * @return A String representing that type
     */
    @Override
    public String filterType() {
        //过滤器的类型 pre route post error
        return "pre";
    }

    /**
     * filterOrder() must also be defined for a filter. Filters may have the same  filterOrder if precedence is not
     * important for a filter. filterOrders do not need to be sequential.
     *
     * @return the int order of a filter
     */
    @Override
    public int filterOrder() {
        //过滤器的顺序
        return 0;
    }

    /**
     * a "true" return from this method means that the run() method should be invoked
     *
     * @return true if the run() method should be invoked. false will not invoke the run() method
     */
    @Override
    public boolean shouldFilter() {
        //true代码过滤器会被执行
        return true;
    }

    /**
     * if shouldFilter() is true, this method will be invoked. this method is the core method of a ZuulFilter
     *
     * @return Some arbitrary artifact may be returned. Current implementation ignores it.
     */
    @Override
    public Object run() {
        RequestContext requestContext = RequestContext.getCurrentContext();

        HttpServletRequest request = requestContext.getRequest();
        //从cookie中取出token
        String tokenFormCookie = getTokenFormCookie(request);
        if (StringUtils.isEmpty(tokenFormCookie)) {
            access_denied();
        }
        //查询redis
        UserTokenStore userToken = authService.getUserToken(tokenFormCookie);
        if (userToken==null) {
            access_denied();
        }

        return null;
    }

    //取出 cookie中名称为uid的值
    private String getTokenFormCookie(HttpServletRequest request){
        Map<String, String> stringMap = CookieUtil.readCookie(request, "uid");
        return stringMap.get("uid");

    }

    //拒绝访问方法
    private void access_denied(){
        RequestContext requestContext = RequestContext.getCurrentContext();
        //拒绝访问
        requestContext.setSendZuulResponse(false);
        //设置响应的内容
        ResponseResult responseResult = new ResponseResult(CommonCode.UNAUTHENTICATED);
        String responseResultString = JSON.toJSONString(responseResult);
        requestContext.setResponseBody(responseResultString);
        requestContext.getResponse().setContentType("application/json;charset=utf-8");
    }
}
