package com.xuecheng.filesystem.dao;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by mrt on 2018/7/3.
 */
public interface FileSystemRepository extends MongoRepository<FileSystem,String> {
}
