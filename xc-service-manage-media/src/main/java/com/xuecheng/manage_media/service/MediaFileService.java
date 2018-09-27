package com.xuecheng.manage_media.service;

import com.sun.org.apache.regexp.internal.RE;
import com.xuecheng.framework.domain.media.MediaFile;
import com.xuecheng.framework.domain.media.request.QueryMediaFileRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResultCode;
import com.xuecheng.manage_media.dao.MediaFileRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: szz
 * @Date: 2018/9/21 下午3:39
 * @Version 1.0
 */
@Service
public class MediaFileService {


    @Autowired
    private MediaFileRepository mediaFileRepository;

    public QueryResponseResult<MediaFile> findList(int page, int size, QueryMediaFileRequest queryMediaFileRequest) {

        if (page < 1) {
            page=1;
        }
        if (size < 1) {
            size = 15;
        }
        page=page-1;
        Pageable pageable = new PageRequest(page, size);

        //查询条件对象
        MediaFile mediaFile=new MediaFile();
        if (StringUtils.isNotEmpty(queryMediaFileRequest.getTag())) {
            mediaFile.setTag(queryMediaFileRequest.getTag());
        }

        if (StringUtils.isNotEmpty(queryMediaFileRequest.getFileOriginalName())) {
            mediaFile.setFileOriginalName(queryMediaFileRequest.getFileOriginalName());
        }

        if (StringUtils.isNotEmpty(queryMediaFileRequest.getProcessStatus())) {
            mediaFile.setProcessStatus(queryMediaFileRequest.getProcessStatus());
        }

        ExampleMatcher exampleMatcher=ExampleMatcher.matching()
                .withMatcher("fileOriginalName",ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("tag",ExampleMatcher.GenericPropertyMatchers.contains());
        Example<MediaFile> example=Example.of(mediaFile,exampleMatcher);
        Page<MediaFile> all = mediaFileRepository.findAll(example, pageable);
        long totalElements = all.getTotalElements();
        List<MediaFile> mediaFiles = all.getContent();

        QueryResult<MediaFile> queryResult=new QueryResult<>();
        queryResult.setTotal(totalElements);
        queryResult.setList(mediaFiles);
        QueryResponseResult<MediaFile> queryResponseResult = new QueryResponseResult<>(CommonCode.SUCCESS,queryResult);
        return queryResponseResult;
    }
}
