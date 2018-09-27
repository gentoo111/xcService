package com.xuecheng.ucenter.dao;

import com.xuecheng.framework.domain.ucenter.XcUserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by admin on 2018/2/7.
 */
public interface XcUserRoleRepository extends JpaRepository<XcUserRole, String> {
    List<XcUserRole> findXcUserRoleByUserId(String userId);
}
