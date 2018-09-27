package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.CmsPostPageResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.*;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.course.response.CoursePublishResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.*;
import com.xuecheng.manage_course.client.CmsPageClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午1:44
 * @Version 1.0
 */
@Service
public class CourseService {

    @Autowired
    private CourseBaseMapper courseBaseMapper;

    @Autowired
    private CourseBaseReponsitory courseBaseReponsitory;

    @Autowired
    private TeachplanMapper teachplanMapper;

    @Autowired
    private TeachplanReponsitory teachplanReponsitory;

    @Autowired
    private CourseMarketRepository courseMarketRepository;

    @Autowired
    private CoursePicRepository coursePicRepository;

    @Autowired
    private CoursePubRepository coursePubRepository;

    @Autowired
    private TeachplanMediaReponsitory teachplanMediaReponsitory;

    @Autowired
    private TeachplanMediaPubReponsitory teachplanMediaPubReponsitory;


    public QueryResponseResult<CourseInfo> findCourseList(int page, int size, CourseListRequest courseListRequest) {
        if (page < 0) {
            page = 1;
        }
        if (size < 1) {
            size = 10;
        }
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseListPage = courseBaseMapper.findCourseListPage(courseListRequest);
        QueryResult<CourseInfo> queryResult = new QueryResult<>();
        queryResult.setTotal(courseListPage.getTotal());
        queryResult.setList(courseListPage.getResult());
        return new QueryResponseResult<CourseInfo>(CommonCode.SUCCESS, queryResult);
    }

    public AddCourseResult addCourseBase(CourseBase courseBase) {
        //检验非空字段
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }

        //设置课程状态为制作中
        courseBase.setStatus(CourseStatus.MAKING.getName());

        CourseBase save = courseBaseReponsitory.save(courseBase);
        if (save != null && StringUtils.isNotEmpty(save.getId())) {
            return new AddCourseResult(CommonCode.SUCCESS, save.getId());
        } else {
            return new AddCourseResult(CommonCode.FAIL, null);
        }
    }

    public static void main(String[] args) {
        System.out.println(CourseStatus.MAKING.getName());
    }

    public TeachplanNode findTeachplanList(String courseId) {
        return teachplanMapper.findList(courseId);
    }

    public CourseBase getCoursebaseById(String id) {
        return courseBaseReponsitory.findOne(id);
    }

    public AddCourseResult updateCoursebase(CourseBase courseBase) {
        CourseBase save = courseBaseReponsitory.save(courseBase);
        if (save != null && StringUtils.isNotEmpty(save.getId())) {
            return new AddCourseResult(CommonCode.SUCCESS, save.getId());
        } else {
            return new AddCourseResult(CommonCode.FAIL, null);
        }
    }

    /**
     * 添加课程计划
     * 业务流程
     * 1.添加一级节点
     * 1)确定根节点
     * 页面传入的parentId为空 处理g根节点
     * 找到一级节点所属的课程的根节点
     * 如果该课程为新课程,那么根节点为空,自动创建课程根节点
     * 2)确定父节点的层级  grade  根据父节点来确定
     * 2.创建二级节点
     *
     * @param teachplan
     * @return
     */
    public ResponseResult addTeachplan(Teachplan teachplan) {
        if (teachplan == null) {
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }
        //得到课程id
        String courseid = teachplan.getCourseid();

        //设置parantId
        String parentid = teachplan.getParentid();
        if (StringUtils.isEmpty(parentid)) {
            //如果传入parentId为空说明要添加一级节点
            //先找到根节点,根据课程id和parentId查询teachplan
            parentid = getTeachplanRoot(courseid);
        }
        if (StringUtils.isEmpty(parentid)) {
            ExceptionCast.cast(CourseCode.COURSE_ADDTEACHPLAN_PARENTIDISNULL);
        }

        //根据parentId查询节点
        Teachplan parentNode = teachplanReponsitory.findOne(parentid);
        //得到父节点的层级grade,确定子节点的层级grade
        String parentGrade = parentNode.getGrade();
        String grade=null;
        switch (parentGrade) {
            case "1":{
                grade="2";
                break;
            }
            case "2":{
                grade = "3";
                break;
            }
            default:{
                grade = "1";
                break;
            }
        }
        teachplan.setParentid(parentid);

        //设置grade层级
        teachplan.setGrade(grade);
        //设置状态,默认为0
        teachplan.setStatus("0");
        teachplan.setCourseid(courseid);

        teachplanReponsitory.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //返回根节点的id
    //如果根节点找不到,创建根节点,
    public String getTeachplanRoot(String courseId) {
        //根据课程id和parentid(0)查询teachplan
        List<Teachplan> teachplanList = teachplanReponsitory.findByCourseidAndParentid(courseId, "0");
        if (teachplanList==null||teachplanList.size()==0) {
            CourseBase one = courseBaseReponsitory.findOne(courseId);
            if (one==null) {
                return null;
            }
            //创建根节点,向teachplan表添加一条新记录
            Teachplan teachplan = new Teachplan();
            teachplan.setPname(one.getName());//根节点名称就是课程名称
            teachplan.setParentid("0");
            teachplan.setGrade("1");
            teachplan.setCourseid(courseId);
            teachplan.setStatus("0");
            Teachplan save = teachplanReponsitory.save(teachplan);
            return save.getId();
        }
        return teachplanList.get(0).getId();
    }

    public CourseMarket getCourseMarketById(String courseId) {
        return courseMarketRepository.findOne(courseId);
    }

    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        return courseMarketRepository.save(courseMarket);
    }


    //保存课程图片到course_pic
    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
        CoursePic one = coursePicRepository.findOne(courseId);

        if(one!=null){//保存图片
            one.setPic(pic);
            coursePicRepository.save(one);
        }else{
            CoursePic coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
            CoursePic save = coursePicRepository.save(coursePic);
        }
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //根据课程id查询课程图片
    public CoursePic findCoursepicList(String courseId) {
        CoursePic coursePic = coursePicRepository.findOne(courseId);
        return coursePic;
    }
    //删除课程图片
    @Transactional
    public ResponseResult deleteCoursePic(String courseId) {
        //删除课程图片信息，如果成功返回值大于0
        long result = coursePicRepository.deleteByCourseid(courseId);
        if(result>0){
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            return new ResponseResult(CommonCode.FAIL);
        }

    }

    //根据课程id查询课程全部信息,此信息用于静态化
    public CourseView getCourseView(String id) {
        CourseView courseView = new CourseView();
        CourseBase courseBase = courseBaseReponsitory.findOne(id);
        if (courseBase == null) {
            return courseView;
        }
        //图片信息
        CoursePic coursePic = coursePicRepository.findOne(id);
        //营销计划
        CourseMarket courseMarket = courseMarketRepository.findOne(id);
        //课程计划
        TeachplanNode teachplanNode = teachplanMapper.findList(id);

        courseView.setCourseMarket(courseMarket);
        courseView.setCoursePic(coursePic);
        courseView.setTeachplanNode(teachplanNode);
        courseView.setCourseBase(courseBase);
        return courseView;
    }

    /**
     * 课程管理服务中提供的课程预览
     * 业务流程
     * 1.前端请求课程预览
     * 2.请求cms服务添加页面
     * 3.得到cms服务返回添加成功的页面id
     * 4.此方法根据页面id拼接成http://www.xuecheng.com/cms/preview/5b986fe86a79ae04a2375530
     * 5.将此页面预览的地址给前端返回
     * 6.前端在浏览器打开此页面预览地址
     */
    @Autowired
    private CmsPageClient cmsPageClient;

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

    public CoursePublishResult preview(String id) {
        CourseBase one = courseBaseReponsitory.findOne(id);
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id+".html");//页面详情页的名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageAliase(one.getName());//页面别名就是课程名称

        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageCreateTime(new Date());
        CmsPageResult cmsPageResult = cmsPageClient.save(cmsPage);

        if (!cmsPageResult.isSuccess()) {
            //抛出异常
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
        //获取新页面的id
        CmsPage cmsPage1 = cmsPageResult.getCmsPage();
        String pageId = cmsPage1.getPageId();
        //构建一个页面预览的url
        //http://www.xuecheng.com/cms/preview/5b986fe86a79ae04a2375530
        String url = previewUrl + pageId;


        return new CoursePublishResult(CommonCode.SUCCESS, url);
    }

    /**
     * 页面发布
     * @param id
     * @return
     */
    @Transactional
    public CoursePublishResult publish(String id) {

        //准备页面信息
        CourseBase courseBase = courseBaseReponsitory.findOne(id);
        CmsPage cmsPage=new CmsPage();
        cmsPage.setSiteId(publish_siteId);
        cmsPage.setPageName(id+".html");//页面详情页的名称
        cmsPage.setPagePhysicalPath(publish_page_physicalpath);
        cmsPage.setPageWebPath(publish_page_webpath);
        cmsPage.setPageAliase(courseBase.getName());//页面别名就是课程名称

        cmsPage.setDataUrl(publish_dataUrlPre+id);
        cmsPage.setTemplateId(publish_templateId);
        cmsPage.setPageCreateTime(new Date());

        //调用cms的一键发布页面接口
        CmsPostPageResult cmsPostPageResult = cmsPageClient.postPageQuick(cmsPage);
        if (!cmsPostPageResult.isSuccess()) {
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_VIEWERROR);
        }

        String pageUrl = cmsPostPageResult.getPageUrl();
        //更新课程状态为已发布,状态保存在mongodb的sys_dict数据字典中
        courseBase.setStatus("202002");
        CourseBase save = courseBaseReponsitory.save(courseBase);

        //将课程信息写到course_pub待索引表中,和更新课程状态属于一个事务
        CoursePub coursePub=saveCoursePub(id);
        if (coursePub==null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }

        //保存teachplan_media_pub信息
        saveTeachpanMedia(id);

        return new CoursePublishResult(CommonCode.SUCCESS,pageUrl);
    }

    //保存teachplan_media_pub信息
    private void saveTeachpanMedia(String courseId) {
        //将课程计划的媒资信息存储到teachplan_media_pub,然后使用logstash进行采集
        //一个课程id对应的课程计划是多个,所以先删后加
        teachplanMediaPubReponsitory.deleteByCourseId(courseId);
        List<TeachplanMedia> teachplanMedias = teachplanMediaReponsitory.findByCourseId(courseId);
        for (TeachplanMedia teachplanMedia : teachplanMedias) {
            TeachplanMediaPub teachplanMediaPub=new TeachplanMediaPub();
            BeanUtils.copyProperties(teachplanMedia,teachplanMediaPub);
            teachplanMediaPub.setTimestamp(new Date());
            teachplanMediaPubReponsitory.save(teachplanMediaPub);
        }
    }

    //向course_pub表中保存课程信息,以便后续为es搜索服务提供数据
    private CoursePub saveCoursePub(String courseId) {
        //课程修改时,先判断表中有没有数据,所以需要去数据库查一下
        CoursePub one = coursePubRepository.findOne(courseId);
        //不管更新还是添加,都要创建对象
        CoursePub coursePub = createCoursePub(courseId);

        //如果已存在就更新,否则就添加
        if (one == null) {
            CoursePub save = coursePubRepository.save(coursePub);
            return save;
        } else {
            //将创建的新coursePub对象的值拷贝到one中
            BeanUtils.copyProperties(coursePub,one);
            //设置主键
            one.setId(courseId);
            //设置时间戳
            one.setTimestamp(new Date());
            CoursePub save = coursePubRepository.save(one);
            return save;
        }
    }

    //创建course_pub对象
    private CoursePub createCoursePub(String courseId) {
        CoursePub coursePub=new CoursePub();

        //查询课程信息的几张表,组成一个coursePub对象
        CourseBase courseBase = courseBaseReponsitory.findOne(courseId);
        BeanUtils.copyProperties(courseBase,coursePub);
        //营销信息
        CourseMarket courseMarket = courseMarketRepository.findOne(courseId);
        if (courseMarket != null) {
            BeanUtils.copyProperties(courseMarket,coursePub);
        }
        //课程图片信息
        CoursePic coursePic = coursePicRepository.findOne(courseId);
        if (coursePic != null) {
            BeanUtils.copyProperties(coursePic,coursePub);
        }

        //课程计划信息
        TeachplanNode teachplanNode = teachplanMapper.findList(courseId);
        //将课程计划转换成json存储到课程发布中
        String teachplanNodeStr = JSON.toJSONString(teachplanNode);
        coursePub.setTeachplan(teachplanNodeStr);

        //课程发布时间
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.now();
        String pubTime = dateTime.format(dateTimeFormatter);
        coursePub.setPubTime(pubTime);

        //时间戳,logstash会根据这个值判断是否进行索引
        coursePub.setTimestamp(new Date());
        return coursePub;
    }


    //保存课程计划相关联的视频
    public ResponseResult savemedia(TeachplanMedia teachplanMedia) {

        if (teachplanMedia==null||StringUtils.isEmpty(teachplanMedia.getTeachplanId())) {
            ExceptionCast.cast(CommonCode.INVLIDATE);
        }

        //课程计划
        String teachplanId = teachplanMedia.getTeachplanId();

        //只有grade为3级时才能选择视频
        Teachplan teachplan = teachplanReponsitory.findOne(teachplanId);
        if (!teachplan.getGrade().equals("3")) {
            ExceptionCast.cast(CourseCode.COURSE_MEDIS_GRADEERROR);
        }

        /*TeachplanMedia teachplanMedia_save = teachplanMediaReponsitory.findOne(teachplanId);
        if (teachplanMedia_save==null) {
            teachplanMedia_save=new TeachplanMedia();
        }
        BeanUtils.copyProperties(teachplanMedia,teachplanMedia_save);
        teachplanMediaReponsitory.save(teachplanMedia_save);*/
        teachplanMediaReponsitory.save(teachplanMedia);

        return new ResponseResult(CommonCode.SUCCESS);
    }
}