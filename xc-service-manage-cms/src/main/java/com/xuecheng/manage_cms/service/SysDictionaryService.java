package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午6:28
 * @Version 1.0
 */
@Service
public class SysDictionaryService {
    @Autowired
    private SysDictionaryRepository sysDictionaryRepository;

    public SysDictionary getByType(String type) {
        return sysDictionaryRepository.findBydType(type);
    }

    public void add(SysDictionary sysDictionary) {
        sysDictionaryRepository.insert(sysDictionary);
    }

    public void update(SysDictionary sysDictionary) {
        sysDictionaryRepository.save(sysDictionary);
    }

    public void delete(String id) {
        sysDictionaryRepository.delete(id);
    }
}
