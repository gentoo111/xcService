package com.xuecheng.auth.client;

import com.xuecheng.api.ucenter.UcenterControllerApi;
import com.xuecheng.framework.client.XcServiceList;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * @Author: szz
 * @Date: 2018/9/25 下午4:29
 * @Version 1.0
 */
@FeignClient(value = XcServiceList.XC_SERVICE_UCENTER)
public interface UserClient extends UcenterControllerApi {
}
