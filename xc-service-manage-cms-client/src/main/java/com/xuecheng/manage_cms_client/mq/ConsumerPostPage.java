package com.xuecheng.manage_cms_client.mq;

import com.alibaba.fastjson.JSON;
import com.mongodb.gridfs.GridFSDBFile;
import com.rabbitmq.client.Channel;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.manage_cms_client.dao.CmsPageRepository;
import com.xuecheng.manage_cms_client.dao.CmsSiteRepository;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Map;

@Component
public class ConsumerPostPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsSiteRepository cmsSiteRepository;

    @Autowired
    private GridFsTemplate gridFsTemplate;

    //接收页面发布的消息,从配置文件中注入队列名称
    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postPage(String msg, Message message, Channel channel) {
        Map msgMap = null;
        try {
            //解析消息
            msgMap = JSON.parseObject(msg, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("接收到的postpage消息解析错误:{}", e.getMessage());
        }
        //从消息中得到页面id
        String pageId = (String) msgMap.get("pageId");
        if (StringUtils.isEmpty(pageId)) {
            LOGGER.error("消息中不包含页面id");
            return;
        }
        //根据id查询数据库得到页面信息
        CmsPage cmsPage = cmsPageRepository.findOne(pageId);
        String siteId = cmsPage.getSiteId();

        //得到站点信息
        CmsSite cmsSite = cmsSiteRepository.findOne(siteId);

        //从页面信息中得到静态文件id
        String htmlFileId = cmsPage.getHtmlFileId();
        if (StringUtils.isEmpty(htmlFileId)) {
            LOGGER.error("消息中不包含htmlFileId");
            return;
        }

        //查询GridFS
        GridFSDBFile gridFSDBFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
        //文件输入流
        InputStream inputStream = gridFSDBFile.getInputStream();

        //得到文件输出路径:站点物理路径+页面物理路径+页面名字
        String filePath = cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath() + cmsPage.getPageName();

        //文件输出流
        FileOutputStream outputStream = null;
        try {
            File fileDir = new File(cmsSite.getSitePhysicalPath() + cmsPage.getPagePhysicalPath());
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            File file = new File(filePath);
            outputStream = new FileOutputStream(file);
            IOUtils.copy(inputStream, outputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LOGGER.error("page html filepath is not found:{}", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("输出 page html is error", e.getMessage());
        }

    }
}