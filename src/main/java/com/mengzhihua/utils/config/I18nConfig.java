package com.mengzhihua.utils.config;


import java.util.List;
import java.util.Locale;

import jakarta.servlet.http.HttpServletRequest;

import com.mengzhihua.utils.common.i18n.LocaleUtil;
import com.mengzhihua.utils.common.lang.StringUtil;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class I18nConfig implements WebMvcConfigurer {

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource source = new ResourceBundleMessageSource();
        source.setBasename("i18n/messages");
        source.setDefaultEncoding("UTF-8");
        source.setFallbackToSystemLocale(false);
        source.setDefaultLocale(Locale.ENGLISH);
        return source;
    }

    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver resolver = new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale(HttpServletRequest request) {
                String lang = request.getParameter("lang");
                if (StringUtil.isNotBlank(lang)) {
                    return LocaleUtil.parse(lang);
                }
                String cookie = request.getHeader("X-Locale");
                if (StringUtil.isNotBlank(cookie)) {
                    return LocaleUtil.parse(cookie);
                }
                return super.resolveLocale(request);
            }
        };
        resolver.setDefaultLocale(Locale.ENGLISH);
        resolver.setSupportedLocales(LocaleUtil.SUPPORTED);
        return resolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
        interceptor.setParamName("lang");
        registry.addInterceptor(interceptor);
    }
}
