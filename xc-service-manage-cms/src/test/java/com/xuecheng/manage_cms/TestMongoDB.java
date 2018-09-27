package com.xuecheng.manage_cms;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestMongoDB {
    @Autowired
    private CmsPageRepository cmsPageRepository;

    /*@Test
    public void testFindList() {
        CmsPage cmsPage=new CmsPage();
        cmsPage.setDataUrl("11");
        cmsPage.setPageName("首页");
        cmsPage.setPageId("1");
        cmsPageRepository.save(cmsPage);
        //生成返回值对象ctrl+alt+v
        CmsPage one = cmsPageRepository.findOne("1");
        System.out.println(one);
        PageRequest pageRequest = new PageRequest(1, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(pageRequest);
        System.out.println(all.getContent());
    }

    @Test
    public void testPageAndExample(){
        CmsPage cmsPage=new CmsPage();

        Pageable pageable = new PageRequest(0, 10);
        ExampleMatcher exampleMatcher=ExampleMatcher.matching()
                .withMatcher("pageAliase",ExampleMatcher.GenericPropertyMatchers.contains());
        cmsPage.setPageAliase("页面");
        Example<? extends CmsPage> example=Example.of(cmsPage,exampleMatcher);
        Page<? extends CmsPage> all = cmsPageRepository.findAll(example, pageable);
        List<? extends CmsPage> content = all.getContent();
        //  public long getTotalElements() {
        //		return total;
        //	}
        long totalElements = all.getTotalElements();
        System.out.println("totalElements:"+totalElements);
        System.out.println(content);

    }*/

    //测试GridFS存取文件
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Test
    public void testGridTemplateStore()throws Exception{

        //输入流
      //  FileInputStream fileInputStream = new FileInputStream(new File("/Users/szz/IdeaProjects/xcEduService/test-freemarker/d:/index_banner.html"));
        FileInputStream fileInputStream = new FileInputStream(new File("/Users/szz/IdeaProjects/xcEduService/test-freemarker/src/main/resources/templates/index_banner.ftl"));
        GridFSFile gridFSFile = gridFsTemplate.store(fileInputStream, "轮播图文件01");
        //文件id
        String fileId = gridFSFile.getId().toString();
        //5b84d0a23712ea0625f959e0
        //5b84eca83712ea07e4d71338  //.ftl的文件id
        System.out.println(fileId);

    }

    @Test
    public void getFile()throws  Exception{
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5b84d0a23712ea0625f959e0")));
        FileOutputStream fileOutputStream = new FileOutputStream(new File("/Users/szz/IdeaProjects/xcEduService/test-freemarker/d:/banner.html"));
        IOUtils.copy(gridFSDBFile.getInputStream(), fileOutputStream);
    }
}
