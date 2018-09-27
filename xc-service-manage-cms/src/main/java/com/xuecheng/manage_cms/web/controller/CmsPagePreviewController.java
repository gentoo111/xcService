package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.CmsPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;

/**
 * @Author: szz
 * @Date: 2018/9/11 上午11:45
 * @Version 1.0
 * 页面预览
 */
@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    private CmsPageService cmsPageService;

    @GetMapping("/cms/preview/{pageId}")
    public void preview(@PathVariable("pageId") String pageId) {
        //使用service根据页面得到静态化内容
        String content = cmsPageService.getHtmlPageId(pageId);
        //使用response将静态化内容响应到浏览器
        try {
            response.setHeader("content-type","text/html;charset=utf-8");
            response.getWriter().write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
