package com.xuecheng.base_id.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by mrt on 2018/3/27.
 */
@Component
@PropertySource("classpath:config/idgenerator.properties")
@ConfigurationProperties
@Data
public class IdGeneratorSetting {
    @Value("${xuecheng.generateId.workerId}")
    int workerId;

    Map<String,Integer> dataCenterId;



}
