package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@Api(value="cms配置管理的接口",description="cms配置管理的接口，提供轮播图信息的配置、精品课程的配置及查询操作")
public interface CmsConfigControllerApi {
    final String API_PRE = "/cms/config";
    //根据配置信息id查询配置信息
    @ApiOperation(value = "根据配置信息id查询配置信息")
    @GetMapping(API_PRE+"/getmodel/{id}")
    public CmsConfigResult getmodel(@PathVariable("id") String id);

}
