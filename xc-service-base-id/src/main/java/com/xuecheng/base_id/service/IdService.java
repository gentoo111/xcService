package com.xuecheng.base_id.service;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.utils.Snowflake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mrt on 2018/3/27.
 */
@Service
public class IdService {
    private static Logger log = LoggerFactory.getLogger(IdService.class);

    @Autowired
    ImmutableMap<String, Snowflake> snowflakeFactory;
    //生成 一个id
    public Long generateId(String datacenterId) {
        try {
            if(snowflakeFactory.get(datacenterId) == null){
                return -1l;
            }
            Snowflake snowflake = snowflakeFactory.get(datacenterId);
            return snowflake.nextId();
        }catch (Exception e){
            log.error("IdService generate Id error{}",e.getMessage(),e);
        }
        return -2l;
    }

}
