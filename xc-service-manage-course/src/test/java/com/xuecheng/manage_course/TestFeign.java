package com.xuecheng.manage_course;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.manage_course.client.CmsPageClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;

/**
 * @Author: szz
 * @Date: 2018/9/9 下午9:20
 * @Version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TestFeign {

    @Autowired
    private CmsPageClient cmsPageClient;

    @Test
    public void testFeign(){
        CmsPageResult byId = cmsPageClient.findById("5b8027c76d9b3206907fd104");
        System.out.println(byId.getCmsPage());
    }


    @Value("${course-publish.dataUrlPre}")
    private String publish_dataUrlPre;
    @Value("${course-publish.pagePhysicalPath}")
    private String publish_page_physicalpath;
    @Value("${course-publish.pageWebPath}")
    private String publish_page_webpath;
    @Value("${course-publish.siteId}")
    private String publish_siteId;
    @Value("${course-publish.templateId}")
    private String publish_templateId;
    @Value("${course-publish.previewUrl}")
    private String previewUrl;
    //单元测试一键发布页面的接口
    @Test
    public void testPostPageQuick(){
        //准备页面信息

        //课程id

        String id = "5b98a29b6a79ae09d480325f";
        //请求cms添加课程页面
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id+".html");//页面详情页的名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageAliase("bootstrap课程详情");//页面别名就是课程名称

        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageCreateTime(new Date());
        //远程调用cms进行一键发布页面
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        System.out.println(cmsPostPageResult);
    }
}
