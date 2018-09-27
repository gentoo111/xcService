package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.TeachplanMediaPub;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import net.bytebuddy.implementation.bytecode.StackSize;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * @Author: szz
 * @Date: 2018/9/15 下午6:38
 * @Version 1.0
 */
@Service
public class EsCourseService {

    private static  final Logger LOGGER = LoggerFactory.getLogger(EsCourseService.class);

    //索引库名称
    @Value("${xuecheng.elasticsearch.course.index}")
    private String es_index;
    //索引类型
    @Value("${xuecheng.elasticsearch.course.type}")
    private String es_type;
    //源文档字段明细
    @Value("${xuecheng.elasticsearch.course.source_field}")
    private String source_field;

    //课程计划媒资索引库名称
    @Value("${xuecheng.elasticsearch.media.index}")
    private String media_index;
    //课程计划媒资索引类型
    @Value("${xuecheng.elasticsearch.media.type}")
    private String media_type;
    //课程计划媒资源文档字段明细
    @Value("${xuecheng.elasticsearch.media.source_field}")
    private String media_source_field;

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    /**
     * 搜索课程
     * @param page
     * @param size
     * @param courseSearchParam
     * @return
     */
    public QueryResponseResult<CoursePub> list(int page, int size, CourseSearchParam courseSearchParam) {
        if (courseSearchParam == null) {
            courseSearchParam = new CourseSearchParam();
        }
        //定义搜索请求对象
        SearchRequest searchRequest = new SearchRequest(es_index);
        //指定索引类型
        searchRequest.types(es_type);
        //创建searchSourceBuilder对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置包括哪些源字段
        String[] source_fields = source_field.split(",");
        searchSourceBuilder.fetchSource(source_fields,new String[]{});

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<font class='eslight'>");
        highlightBuilder.postTags("</font>");
        //设置高亮字段
        highlightBuilder.fields().add(new HighlightBuilder.Field("name"));
        searchSourceBuilder.highlighter(highlightBuilder);

        //创建布尔查询,完成综合查询
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //根据关键字搜索,name,description,teachplan
        if (StringUtils.isNotEmpty(courseSearchParam.getKeyword())) {
            String keyword = courseSearchParam.getKeyword();
            MultiMatchQueryBuilder multiMatchQueryBuilder = QueryBuilders.multiMatchQuery(keyword,"name","teachplan","description");
            //设置匹配的比例
            multiMatchQueryBuilder.minimumShouldMatch("70%");
            //设置boost,将name字段的权重加10倍
            multiMatchQueryBuilder.field("name", 10);

            //将multiMatchQueryBuilder加入到布尔查询中
            boolQueryBuilder.must(multiMatchQueryBuilder);
        }

        //过滤查询:mt大分类  st小分类  grade课程等级
        if (StringUtils.isNotEmpty(courseSearchParam.getMt())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("mt",courseSearchParam.getMt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getSt())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("st",courseSearchParam.getSt()));
        }
        if (StringUtils.isNotEmpty(courseSearchParam.getGrade())) {
            boolQueryBuilder.filter(QueryBuilders.termsQuery("grade",courseSearchParam.getGrade()));
        }

        //分页查询
        if (page<1) {
            page=1;
        }
        if (size < 1) {
            size=12;
        }
        int start=(page-1)*size;
        searchSourceBuilder.from(start);
        searchSourceBuilder.size(size);

        //将boolQueryBuilder设置到searchSourceBuilder中
        searchSourceBuilder.query(boolQueryBuilder);
        //将searchSourceBuilder设置到searchRequest中
        searchRequest.source(searchSourceBuilder);

        //开始搜索
        SearchResponse searchResponse =null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER.error("search error:{}",e.getMessage());
            return new QueryResponseResult<>(CommonCode.FAIL,new QueryResult<>());
        }

        //处理结果集
        SearchHits hits = searchResponse.getHits();
        //总记录数
        long totalHits = hits.getTotalHits();
        //得到记录
        SearchHit[] searchHits = hits.getHits();
        //返回数据列表
        List<CoursePub> datalist = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            CoursePub coursePub=new CoursePub();
            //课程名称
            String name=null;
            //替换高亮结果集
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField nameHighlightField = highlightFields.get("name");
            if (nameHighlightField != null) {
                Text[] fragments = nameHighlightField.getFragments();
                StringBuffer sb = new StringBuffer();
                for (Text fragment : fragments) {
                    sb.append(fragment.string());
                }
                name = sb.toString();
            }

            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //取出id,名称,图片,课程等级,价格,大分类,小分类
            //如果高亮字段中没有name,那就去普通字段中取
            if (StringUtils.isEmpty(name)) {
                name= (String) sourceAsMap.get("name");
            }

            String id= (String) sourceAsMap.get("id");
            String pic= (String) sourceAsMap.get("pic");
            String grade= (String) sourceAsMap.get("grade");
            String mt= (String) sourceAsMap.get("mt");
            String st= (String) sourceAsMap.get("st");
            try {
                Double price= (Double) sourceAsMap.get("price");
                if (price!=null) {
                    coursePub.setPrice(Float.parseFloat(price.toString()));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            coursePub.setId(id);
            coursePub.setName(name);
            coursePub.setPic(pic);
            coursePub.setGrade(grade);
            coursePub.setMt(mt);
            coursePub.setSt(st);
            datalist.add(coursePub);
        }

        QueryResult<CoursePub> queryResult=new QueryResult<>();
        queryResult.setList(datalist);
        queryResult.setTotal(totalHits);

        return new QueryResponseResult(CommonCode.SUCCESS,queryResult);
    }


    public Map<String, CoursePub> getall(String id) {
        //定义搜索请求对象
        SearchRequest searchRequest = new SearchRequest(es_index);
        //指定索引类型
        searchRequest.types(es_type);
        //创建searchSourceBuilder对象
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.termsQuery("id",id));

        searchRequest.source(searchSourceBuilder);
        //开始搜索
        SearchResponse searchResponse =null;
        try {
            searchResponse = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits searchHits = searchResponse.getHits();
        SearchHit[] hits = searchHits.getHits();
        CoursePub coursePub=new CoursePub();

        for (SearchHit hit : hits) {
            //取出搜索的文档内容
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            //课程的id
            String courseId = (String) sourceAsMap.get("id");
            String name = (String) sourceAsMap.get("name");
            String grade = (String) sourceAsMap.get("grade");
            String charge = (String) sourceAsMap.get("charge");
            String pic = (String) sourceAsMap.get("pic");
            String description = (String) sourceAsMap.get("description");
            String teachplan = (String) sourceAsMap.get("teachplan");
            coursePub.setId(courseId);
            coursePub.setName(name);
            coursePub.setPic(pic);
            coursePub.setGrade(grade);
            coursePub.setTeachplan(teachplan);
            coursePub.setDescription(description);
        }
        //要返回的对象
        Map<String, CoursePub> coursePubMap = new HashMap<>();
        //将搜索的课程信息放入对象
        coursePubMap.put(id,coursePub);

        return coursePubMap;
    }

    //根据课程计划id查询对应的视频信息
    public List<TeachplanMediaPub> getall(String[] teachplanIds) {
        if (teachplanIds==null||teachplanIds.length<1) {
            return new ArrayList<TeachplanMediaPub>();
        }
        //定义搜索请求对象
        SearchRequest searchRequest = new SearchRequest(media_index);
        //指定索引类型
        searchRequest.types(media_type);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //设置查询条件
        searchSourceBuilder.query(QueryBuilders.termsQuery("teachplan_id",teachplanIds));
        searchRequest.source(searchSourceBuilder);

        //执行搜索
        SearchResponse search =null;
        try {
            search = restHighLevelClient.search(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHits searchHits = search.getHits();
        SearchHit[] hits = searchHits.getHits();
        List<TeachplanMediaPub> teachplanMediaPubList = new ArrayList<>();
        for (SearchHit hit : hits) {
            TeachplanMediaPub teachplanMediaPub=new TeachplanMediaPub();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            String media_url = (String) sourceAsMap.get("media_url");
            String courseid = (String) sourceAsMap.get("courseid");
            String teachplan_id = (String) sourceAsMap.get("teachplan_id");
            String media_id = (String) sourceAsMap.get("media_id");
            String media_fileoriginalname = (String) sourceAsMap.get("media_fileoriginalname");

            teachplanMediaPub.setMediaId(media_id);
            teachplanMediaPub.setTeachplanId(teachplan_id);
            teachplanMediaPub.setMediaFileOriginalName(media_fileoriginalname);
            teachplanMediaPub.setCourseId(courseid);
            teachplanMediaPub.setMediaUrl(media_url);
            teachplanMediaPubList.add(teachplanMediaPub);
        }

        return teachplanMediaPubList;
    }
}
