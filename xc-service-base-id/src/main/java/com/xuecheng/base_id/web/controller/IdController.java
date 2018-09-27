package com.xuecheng.base_id.web.controller;

import com.xuecheng.base_id.service.IdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mrt on 2018/3/27.
 */
@RestController
public class IdController {
    @Autowired
    private IdService idService;

    @PostMapping("/generateid/{datacenterId}")
    public Long generateId(@PathVariable("datacenterId") String datacenterId){
        return idService.generateId(datacenterId);
    }
}
