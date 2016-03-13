package com.hannikkala.poc.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * User: bleed
 * Date: 13/03/16
 * Time: 22:57
 */
@Component
public class AppContextServiceImpl implements ApplicationContextAware {
    private static ApplicationContext _appCtx;

    @Override
    public void setApplicationContext(ApplicationContext ctx) {
        _appCtx = ctx;
    }

    public static ApplicationContext getAppContext() {
        return _appCtx;
    }
}
