package com.xuecheng.framework.domain.cms.response;

import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: szz
 * @Date: 2018/9/12 下午2:20
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class CmsPostPageResult extends ResponseResult {

    private String pageUrl;

    public CmsPostPageResult(ResultCode resultCode, String pageUrl) {
        super(resultCode);
        this.pageUrl = pageUrl;
    }
}
