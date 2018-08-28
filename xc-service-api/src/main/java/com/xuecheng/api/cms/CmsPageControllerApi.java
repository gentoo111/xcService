package com.xuecheng.api.cms;

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
import org.springframework.web.bind.annotation.*;

@Api(value = "cms頁面管理的接口",description = "提供頁面的添加刪除修改查詢操作")
public interface CmsPageControllerApi {
    final String API_PRE = "/cms/page";
    //分页查询页面
    @ApiOperation(value = "分頁查詢頁面列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page",value = "頁碼",paramType ="path" ),
            @ApiImplicitParam(name = "size",value = "每頁記錄數",paramType = "path")
    })
    @GetMapping(API_PRE+"/list/{page}/{size}")
    public QueryResponseResult<CmsPage> findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest);

    @ApiOperation(value = "新增页面")
    @PostMapping(API_PRE+"/add")
    public CmsPageResult add(@RequestBody CmsPage cmsPage);


    //根据id查询页面
    @ApiOperation(value = "根据id查询页面")
    @GetMapping(API_PRE + "/get/{id}")
    public CmsPageResult findById(@PathVariable("id") String id);

    //更新页面
    @ApiOperation(value = "更新页面")
    @PutMapping(API_PRE + "/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage);

    @ApiOperation(value = "通过id删除页面")
    @DeleteMapping(API_PRE + "/del/{id}")
    public ResponseResult delete(@PathVariable("id") String id);

    @ApiOperation(value = "页面静态化")
    @PostMapping(API_PRE+"/generateHtml/{pageId}")
    public GenerateHtmlResult generateHtml(@PathVariable("pageId")String pageId);

    @ApiOperation(value = "查询静态化页面内容")
    @GetMapping(API_PRE+"/getHtml/{pageId}")
    public GenerateHtmlResult getHtml(@PathVariable("pageId")String pageId);


}
