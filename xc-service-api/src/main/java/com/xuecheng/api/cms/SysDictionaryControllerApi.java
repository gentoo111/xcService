package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: szz
 * @Date: 2018/9/6 下午6:18
 * @Version 1.0
 */

@Api(value = "数据字典接口",description = "提供数据字典接口的管理,查询功能")
public interface SysDictionaryControllerApi {

    final String API_PRE = "/sys/dictionary";

    @ApiOperation(value = "数据字典查询接口")
    @GetMapping(API_PRE + "/get/{type}")
    SysDictionary getByType(@PathVariable("type") String type);

    @ApiOperation("新增数据字典")
    @PostMapping(API_PRE+"/add")
    void add(@RequestBody SysDictionary sysDictionary);

    @ApiOperation("修改数据字典")
    @PutMapping(API_PRE+"/update")
    void update(@RequestBody SysDictionary sysDictionary);

    @ApiOperation("删除数据字典")
    @DeleteMapping(API_PRE+"/delete/{id}")
    void add(String id);
}
