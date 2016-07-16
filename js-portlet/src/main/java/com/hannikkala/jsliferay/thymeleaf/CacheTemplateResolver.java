package com.hannikkala.jsliferay.thymeleaf;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.resourceresolver.IResourceResolver;
import org.thymeleaf.templateresolver.TemplateResolver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 11/03/16
 * Time: 09:37
 */
public class CacheTemplateResolver extends TemplateResolver implements InitializingBean {

    private final static String PREFIX = "cache:";

    @Autowired
    private CacheManager cacheManager;

    private String cacheName;

    public CacheTemplateResolver() {
        setResourceResolver(new CacheResourceResolver());
        setResolvablePatterns(Sets.newHashSet(PREFIX + "*"));
    }

    @Override
    protected String computeResourceName(TemplateProcessingParameters templateProcessingParameters) {
        String templateName = templateProcessingParameters.getTemplateName();
        return templateName.substring(PREFIX.length());
    }

    public String getCacheName() {
        return cacheName;
    }

    public void setCacheName(String cacheName) {
        this.cacheName = cacheName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(cacheName, "Cache name must be set.");
    }

    private class CacheResourceResolver implements IResourceResolver {
        @Override
        public String getName() {
            return "cacheResourceResolver";
        }

        @Override
        public InputStream getResourceAsStream(TemplateProcessingParameters templateProcessingParameters, String resourceName) {
            Cache cache = cacheManager.getCache(cacheName);
            String template = cache.get(resourceName, String.class);
            return new ByteArrayInputStream(template.getBytes());
        }
    }
}
