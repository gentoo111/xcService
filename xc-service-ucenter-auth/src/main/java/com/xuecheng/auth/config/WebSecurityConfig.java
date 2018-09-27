package com.xuecheng.auth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by mrt on 2018/5/18.
 */
@Configuration
@EnableWebSecurity
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/userlogin","/userlogout","/userjwt","/v2/api-docs", "/swagger-resources/configuration/ui",
                "/swagger-resources","/swagger-resources/configuration/security",
                "/swagger-ui.html","/css/**", "/js/**","/images/**", "/webjars/**", "**/favicon.ico", "/index");

    }
}
