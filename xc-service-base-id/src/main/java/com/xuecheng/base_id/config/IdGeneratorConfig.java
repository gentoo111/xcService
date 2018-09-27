package com.xuecheng.base_id.config;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.utils.Snowflake;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.Set;

/**
 * Created by mrt on 2018/3/27.
 */

@Configuration
@EnableConfigurationProperties(IdGeneratorSetting.class)
public class IdGeneratorConfig {

    @Autowired
    IdGeneratorSetting idGeneratorSetting;

    @Bean
    public ImmutableMap<String, Snowflake> snowflakeFactory(){
        ImmutableMap.Builder<String, Snowflake> builder = ImmutableMap.builder();
        int workerId = idGeneratorSetting.getWorkerId();
        Map<String, Integer> dataCenterIdMap = idGeneratorSetting.getDataCenterId();
        Set<String> dataCenterIds = dataCenterIdMap.keySet();
        for(String dataCenterId:dataCenterIds){
            Integer dataCenterIdNum = dataCenterIdMap.get(dataCenterId);
            Snowflake snowflake = new Snowflake(dataCenterIdNum, workerId);
            builder.put(dataCenterId,snowflake);
        }

        ImmutableMap<String, Snowflake> snowflakeFactory = builder.build();
       return snowflakeFactory;
    }

}
