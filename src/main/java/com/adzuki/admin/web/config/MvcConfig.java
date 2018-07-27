package com.adzuki.admin.web.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import com.adzuki.admin.common.filter.SecurityFilter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.github.pagehelper.PageHelper;

/**
 */
@Configuration
public class MvcConfig extends WebMvcConfigurerAdapter {


    @Value("${i18n.resource.cache-seconds}")
    private int i18nResourceCacheSeconds;

    /***
     * filter 注册
     */
    @Bean
    public FilterRegistrationBean loginFilterRegistration() {
        SecurityFilter.loginUrls = new String[]{"/", "/index", "/home", "/logout", "/modifyPassword", "/haveFault", "/typeCode/*", "/home/*"};
        SecurityFilter.anonUrls = new String[]{"/checkstartup.html", "/resources/**", "/login", "/error", "/favicon.ico", "/forget", "/forgetPassword"};
        FilterRegistrationBean registration = new FilterRegistrationBean(new SecurityFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    /***
     * 使用Fastjson做为转换器
     */
    @Bean
    public HttpMessageConverters fastJsonHttpMessageConverters() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
        fastConverter.setFastJsonConfig(fastJsonConfig);
        HttpMessageConverter<?> converter = fastConverter;
        return new HttpMessageConverters(converter);
    }

    /**
     * 方便在Controller外获取到当前HttpServletRequest对象
     *
     * @return
     */
    @Bean
    public RequestContextListener requestContextListener() {
        return new RequestContextListener();
    }

    /***
     * mybatis pagehelper插件支持
     */
    @Bean
    public PageHelper pageHelper() {
        PageHelper pageHelper = new PageHelper();
        Properties p = new Properties();
        p.setProperty("offsetAsPageNum", "true");
        p.setProperty("rowBoundsWithCount", "true");
        p.setProperty("reasonable", "true");
        pageHelper.setProperties(p);
        return pageHelper;
    }

    /**
     * 多国语言消息源
     *
     * @return
     */
    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("message");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(i18nResourceCacheSeconds);
        return messageSource;
    }

    /**
     * Locale解析器
     *
     * @return
     */
    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver localeResolver = new CookieLocaleResolver();
        localeResolver.setCookieMaxAge(3600 * 24 * 30); //设置cookie有效期30天
        localeResolver.setCookieName("locale");
        return localeResolver;
    }

    /**
     * 配置拦截器
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LocaleChangeInterceptor());
        super.addInterceptors(registry);
    }
}
