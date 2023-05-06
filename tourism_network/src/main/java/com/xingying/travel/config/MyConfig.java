package com.xingying.travel.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: tz
 * @Date: 2019/3/17 21:22
 */
@Configuration
public class MyConfig implements WebMvcConfigurer {

    @Autowired
    private InterceptorConfig interceptorConfig;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }


    // 这个方法用来注册拦截器，我们自己写好的拦截器需要通过这里添加注册才能生效
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //registry.addInterceptor(interceptorConfig).addPathPatterns()
        registry.addInterceptor(interceptorConfig).addPathPatterns("/admin/**").excludePathPatterns("/admin/login").excludePathPatterns("/admin/adminlogin");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/dist/view");

        registry.addViewController("/admin").setViewName("forward:/admin/adminlogin");


        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
    }
}
