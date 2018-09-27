package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcMenu;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by mrt on 2018/7/18.
 */
@Mapper
public interface XcMenuMapper {
    //查询某个用户的权限
    List<XcMenu> selectPermissionByUserId(String userId);
}
