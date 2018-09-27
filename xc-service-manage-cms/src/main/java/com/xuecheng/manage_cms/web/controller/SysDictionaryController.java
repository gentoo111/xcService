package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午6:28
 * @Version 1.0
 */
@RestController
public class SysDictionaryController implements SysDictionaryControllerApi {


    @Autowired
    private SysDictionaryService sysDictionaryService;

    @Override
    public SysDictionary getByType(@PathVariable String type) {
        return sysDictionaryService.getByType(type);
    }

    @Override
    public void add(@RequestBody SysDictionary sysDictionary){
        sysDictionaryService.add(sysDictionary);
    }

    @Override
    public void update(@RequestBody SysDictionary sysDictionary) {
        sysDictionaryService.update(sysDictionary);
    }

    @Override
    public void add(@PathVariable String id) {
        sysDictionaryService.delete(id);
    }
}
