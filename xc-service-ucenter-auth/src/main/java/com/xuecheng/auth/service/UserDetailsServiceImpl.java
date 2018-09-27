package com.xuecheng.auth.service;

import com.xuecheng.auth.client.UserClient;
import com.xuecheng.framework.domain.ucenter.XcMenu;
import com.xuecheng.framework.domain.ucenter.ext.XcUserExt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        XcUserExt xcUserExt = userClient.getUserext(username);

        //XcUserExt userext = new XcUserExt();
        if(xcUserExt == null){
            return null;
        }
        //取出正确密码，这里暂时使用静态密码
        //String password = "123";
        String password = xcUserExt.getPassword();

        //权限标识串,多个权限之间用逗号分隔
        //String user_permission_string  = "course_find_list,coursepic_find_list";

        List<XcMenu> xcMenus = xcUserExt.getPermissions();
        List<String> list = new ArrayList<>();
        for (XcMenu xcMenu : xcMenus) {
            String code = xcMenu.getCode();
            list.add(code);
        }
        String user_permission_string = StringUtils.join(list.toArray(), ",");

        UserJwt userDetails = new UserJwt(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(user_permission_string));
        //将用户的 相关信息加入userJwt,那么在jwt的令牌中才包括加入的这些信息
        userDetails.setCompanyId(xcUserExt.getCompanyId());
        userDetails.setName(xcUserExt.getName());
        userDetails.setUserpic(xcUserExt.getUserpic());
        userDetails.setUtype(xcUserExt.getUtype());
        userDetails.setId(xcUserExt.getId());

       /* UserDetails userDetails = new org.springframework.security.core.userdetails.User(username,
                password,
                AuthorityUtils.commaSeparatedStringToAuthorityList(""));*/
//                AuthorityUtils.createAuthorityList("course_get_baseinfo","course_get_list"));
        return userDetails;
    }
}
