package com.xuecheng.learning.client;

import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.client.XcServiceList;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: szz
 * @Date: 2018/9/23 下午7:56
 * @Version 1.0
 */
@FeignClient(value = XcServiceList.XC_SERVICE_SEARCH)
public interface SearchClient extends EsCourseControllerApi {
}
