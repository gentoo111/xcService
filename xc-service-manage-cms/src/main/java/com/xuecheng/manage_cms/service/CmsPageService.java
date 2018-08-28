package com.xuecheng.manage_cms.service;

import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Service
public class CmsPageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CmsPageService.class);

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private CmsTemplateRepository cmsTemplateRepository;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;
        if (size <= 0) {
            size = 20;
        }
        //条件匹配器,默认就是精确匹配
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        //条件值对象
        CmsPage cmsPage = new CmsPage();
        //判断传入的站点id
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //设置页面别名查询条件
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        // cmsPage.setSiteId("5a751fab6abb5044e0d19ea1");//查询该站点下的页面
//        cmsPage.setPageAliase("页面");
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //分頁參數
        Pageable pageable = new PageRequest(page, size);
        //執行分頁查詢
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageable);
        QueryResult queryResult = new QueryResult();
        queryResult.setList(all.getContent());
        queryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, queryResult);
    }

    //新增页面
    public CmsPageResult add(CmsPage cmsPage) {
        //校验页面是否重复,根据页面名称,站点id,页面web访问路径判断此页面是否重复
        CmsPage cmsPage_1 = cmsPageRepository.findBySiteIdAndAndPageNameAndPageWebPath(cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());

        if (cmsPage_1 != null) {
            ExceptionCast.cast(CmsCode.CMS_ADDPAGE_EXISTSNAME);
        }
        //将主键设置为空
        cmsPage.setPageId(null);
        //ctrl+alt+v直接返回本地变量
        CmsPage save = cmsPageRepository.save(cmsPage);
        if (save != null) {
            return new CmsPageResult(CommonCode.SUCCESS, save);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public CmsPageResult findById(String id) {
        CmsPage one = cmsPageRepository.findOne(id);
        if (one != null) {
            return new CmsPageResult(CommonCode.SUCCESS, one);
        }
        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public CmsPageResult update(String id, CmsPage cmsPage) {
        CmsPage one = cmsPageRepository.findOne(id);
        if (one != null) {
            //更新数据
            //更新模板id
            one.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            one.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            one.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            one.setPageName(cmsPage.getPageName());
            //更新访问路径
            one.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            one.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            //更新状态
            one.setPageType(cmsPage.getPageType());
            //保存dateUrl
            one.setDataUrl(cmsPage.getDataUrl());
            CmsPage save = cmsPageRepository.save(one);
            if (save != null) {
                return new CmsPageResult(CommonCode.SUCCESS, save);
            }
        }

        return new CmsPageResult(CommonCode.FAIL, null);
    }

    public ResponseResult delete(String id) {
        cmsPageRepository.delete(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //生成静态化文件
    @SuppressWarnings("all")
    public GenerateHtmlResult generateHtml(String pageId) {
        //查询页面信息
        CmsPage one = cmsPageRepository.findOne(pageId);
        if (one == null) {
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }
        //得到模板id
        String templateId = one.getTemplateId();
        //dedaodataUrl
        String dataUrl = one.getDataUrl();

        Map map = null;
        if (StringUtils.isNotEmpty(dataUrl)) {
            //远程请求dataUrl获取数据
            ResponseEntity<Map> forEntity = restTemplate.getForEntity(dataUrl, Map.class);
            map = forEntity.getBody();
        }
        //根据模板id从GridFS中查询模板内容
        String templateContent = getPageTemplateContent(templateId);

        //配置freemarker,进行页面静态化
        String content = generateFreemarker(map, templateContent);


        //将静态文件的内容存储到GRIDFS中
        String fileId = saveHtml(content);

        //将文件id存储到cmspage
        one.setHtmlFileId(fileId);
        cmsPageRepository.save(one);

        return new GenerateHtmlResult(CommonCode.SUCCESS, content);
    }

    //配置freemarker,进行页面静态化
    private String generateFreemarker(Map map, String templateContent) {

        Configuration configuration = new Configuration(Configuration.getVersion());

        //加载模板
        //使用模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        //加载上边templateContent的内容作为模板
        //参数1：给模板起一个名称，参数2：模板内容
        stringTemplateLoader.putTemplate("template", templateContent);
        //将模板加载器设置到configuration
        configuration.setTemplateLoader(stringTemplateLoader);
        //设置字符编码
        configuration.setDefaultEncoding("utf-8");

        //指定模板文件名称
        Template template = null;
        try {
            template = configuration.getTemplate("template");
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }

        //准备数据
        Map<String, Object> model = new HashMap<>();
        model.put("model", map);


        //参数1：模板，参数2：数据模型
        String content = null;
        try {
            //静态化
            content = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return content;
    }

    //将静态文件的内容存储到GRIDFS中
    private String saveHtml(String content) {
        //将静态文件的内容存储到GRIDFS中
        InputStream htmlInputStream = null;
        try {
            htmlInputStream = IOUtils.toInputStream(content, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        GridFSFile gridFSFile = gridFsTemplate.store(htmlInputStream, "轮播图新静态文件");
        //得到文件id
        return gridFSFile.getId().toString();
    }

    //根据模板id从GridFS中查询模板内容
    private String getPageTemplateContent(String templateId) {
        //根据模板id查询cms_template表,找到templateFileId
        CmsTemplate one = cmsTemplateRepository.findOne(templateId);
        if (one == null) {
            return null;
        }
        String templateFileId = one.getTemplateFileId();
        if (StringUtils.isEmpty(templateFileId)) {
            return null;
        }
        //根据templateFileId从GridFS中查询模板文件内容
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
        //得到模板文件的流对象
        InputStream inputStream = gridFSDBFile.getInputStream();
        String templateContent = null;
        try {
            templateContent = IOUtils.toString(inputStream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        if (StringUtils.isEmpty(templateContent)) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_TEMPLATEISNULL);
        }
        return templateContent;
    }

    public GenerateHtmlResult getHtml(String pageId) {
        CmsPage one = cmsPageRepository.findOne(pageId);
        if (one == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTFOUND);
        }
        //得到页面静态文件id
        String htmlFileId = one.getHtmlFileId();
        String html = null;
        try {
            //从GridFS中查询静态文件内容
            GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
            InputStream inputStream = gridFSDBFile.getInputStream();
            html = IOUtils.toString(inputStream, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("Get htmlFile from GridFS has a error:{}", e.getMessage());
            return new GenerateHtmlResult(CommonCode.FAIL, null);
        }
        return new GenerateHtmlResult(CommonCode.SUCCESS, html);

    }
}
