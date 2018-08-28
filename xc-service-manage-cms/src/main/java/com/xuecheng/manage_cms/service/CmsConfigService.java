package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.response.CmsConfigResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CmsConfigService {
    @Autowired
    private CmsConfigRepository cmsConfigRepository;

    public CmsConfigResult getConfigById(String id) {
        CmsConfig cmsConfig = cmsConfigRepository.findOne(id);
        if (cmsConfig == null) {
            ExceptionCast.cast(CommonCode.INVLIDATE);//非法参数
        }
        return new CmsConfigResult(CommonCode.SUCCESS,cmsConfig);
    }
}