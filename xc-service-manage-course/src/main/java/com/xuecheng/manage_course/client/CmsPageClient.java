package com.xuecheng.manage_course.client;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * FeignClient从eureka中找一个可用的服务
 */
@FeignClient(XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsPageClient extends CmsPageControllerApi {


}
//@FeignClient("xc-service-manage-cms")
//public interface CmsPageClient {
//    final String API_PRE = "/cms/page";
//
//    @ApiOperation(value = "根据id查询页面")
//    @GetMapping(API_PRE + "/get/{id}")
//    public CmsPageResult findById(@PathVariable("id") String id);
//
//}
