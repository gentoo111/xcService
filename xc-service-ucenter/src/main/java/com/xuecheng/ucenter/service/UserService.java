package com.xuecheng.ucenter.service;

import com.xuecheng.framework.domain.ucenter.XcCompanyUser;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.XcUser;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import com.xuecheng.ucenter.dao.XcCompanyUserRepository;
import com.xuecheng.ucenter.dao.XcMenuMapper;
import com.xuecheng.ucenter.dao.XcUserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: szz
 * @Date: 2018/9/25 下午4:15
 * @Version 1.0
 */
@Service
public class UserService {

    @Autowired
    private XcUserRepository xcUserRepository;

    @Autowired
    private XcCompanyUserRepository xcCompanyUserRepository;

    @Autowired
    private XcMenuMapper xcMenuMapper;

    //根据账号查询用户信息(包括用户基本信息、用户所属企业，及用户的权限)
    public XcUserExt getUserExt(String username){

        //返回xcUserExt对象
        XcUserExt xcUserExt = new XcUserExt();
        //根据账号查询
        XcUser xcUser = xcUserRepository.findByUsername(username);
        if(xcUser == null){
            return null;
        }
        //将xcUser属性值拷贝到xcUserExt
        BeanUtils.copyProperties(xcUser,xcUserExt);
        //用户id
        String userId = xcUser.getId();
        //查询用户所属企业
        XcCompanyUser xcCompanyUser = xcCompanyUserRepository.findByUserId(userId);
        if(xcCompanyUser!=null){
            //将企业id设置到返回对象中
            xcUserExt.setCompanyId(xcCompanyUser.getCompanyId());
        }

        //查询用户的权限
        List<XcMenu> xcMenus = xcMenuMapper.selectPermissionByUserId(userId);
        xcUserExt.setPermissions(xcMenus);

        return xcUserExt;

    }
}
