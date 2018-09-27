package com.xuecheng.manage_course.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by szz on 2018/3/30 15:07.
 * Email szhz186@gmail.com
 * http://localhost:31200/druid/index.html
 */
@Configuration
public class DruidConfig {
    private static final Logger log = LoggerFactory.getLogger("adminLogger");

    @Bean
    public ServletRegistrationBean druidServlet() {
        log.info("初始化 Druid Servlet Configuration ");
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(),
                "/druid/*");
        // IP白名单,这种配置方式只能在服务器上监控
        ///servletRegistrationBean.addInitParameter("allow", "127.0.0.1");
        // IP黑名单(共同存在时，deny优先于allow)
//		servletRegistrationBean.addInitParameter("deny", "192.168.27.26");
        // 控制台管理用户,通过这样的方式可以进行远程监控
        servletRegistrationBean.addInitParameter("loginUsername", "admin");
        servletRegistrationBean.addInitParameter("loginPassword", "admin");
//		// 是否能够重置数据 禁用HTML页面上的“Reset All”功能
//		servletRegistrationBean.addInitParameter("resetEnable", "false");
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

}
