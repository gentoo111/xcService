package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.system.SysDictionary;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午6:29
 * @Version 1.0
 */
public interface SysDictionaryRepository extends MongoRepository<SysDictionary,String> {
    SysDictionary findBydType(String type);
}
