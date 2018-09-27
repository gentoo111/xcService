package com.xuecheng.manage_media_process.dao;

import com.xuecheng.framework.domain.media.MediaFile;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @Author: szz
 * @Date: 2018/9/21 上午10:29
 * @Version 1.0
 */
public interface MediaFileRepository extends MongoRepository<MediaFile,String> {
}
