package com.mengzhihua.utils.common.spring;


import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Static access to Spring beans. Prefer constructor injection in application code.
 */
@Component
public class SpringContextHolder implements ApplicationContextAware {

    private static volatile ApplicationContext context;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        context = applicationContext;
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            throw new IllegalStateException("Spring ApplicationContext is not initialized yet");
        }
        return context;
    }

    public static <T> T getBean(Class<T> type) {
        return getContext().getBean(type);
    }

    public static <T> T getBean(String name, Class<T> type) {
        return getContext().getBean(name, type);
    }

    public static String getProperty(String key) {
        return getContext().getEnvironment().getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return getContext().getEnvironment().getProperty(key, defaultValue);
    }
}
