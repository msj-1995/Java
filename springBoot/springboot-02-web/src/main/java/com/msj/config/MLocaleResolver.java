package com.msj.config;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class MLocaleResolver implements LocaleResolver {
    //解析请求
    @Override
    public Locale resolveLocale(HttpServletRequest httpServletRequest) {
        //获取请求中的语言参数
        String language = httpServletRequest.getParameter("lang");
        //如果没有对应的参数就走默认的
        Locale locale = Locale.getDefault();
        //如果language不为空(即携带了国际化参数），则使用我们的配置
        if(!StringUtils.isEmpty(language)){
            //参数language中的值zh_CN等有下划线_分割，前面是国家，后面是地区，分割得到国家和地区
            String[] split = language.split("_");
            //国家，地区  split[0]是国家,split[1]是地区
            locale = new Locale(split[0],split[1]);
        }
        return locale;
    }

    @Override
    public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Locale locale) {

    }
}
