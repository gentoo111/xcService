package com.xuecheng.manage_media.dao;

import com.xuecheng.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: szz
 * @Date: 2018/9/19 上午11:42
 * @Version 1.0
 */
public interface MediaFileRepository extends MongoRepository<MediaFile,String> {

}
