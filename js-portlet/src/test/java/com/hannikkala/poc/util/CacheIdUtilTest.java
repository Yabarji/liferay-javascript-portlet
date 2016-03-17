package com.hannikkala.poc.util;

import junit.framework.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 10/03/16
 * Time: 11:58
 */
public class CacheIdUtilTest {

    @org.junit.Test
    public void testCreateCacheId() throws Exception {
        String cacheId = CacheIdUtil.createCacheId("http://localhost:3000", "/index.html", "/myservlet/123");
        assertEquals("/myservlet/123/index.html", cacheId);
    }

    @org.junit.Test
    public void testCreateCacheId2() throws Exception {
        String cacheId = CacheIdUtil.createCacheId("http://localhost:3000/context", "../index.html", "/myservlet/123");
        assertEquals("/myservlet/123/index.html", cacheId);
    }

    @Test
    public void testCreateCacheIdAbsoluteURL() {
        String cacheId = CacheIdUtil.createCacheId("http://jdanyow.github.io/aurelia-examples/",
                "http://jdanyow.github.io/aurelia-examples/jspm_packages/npm/font-awesome@4.3.0/css/font-awesome.min.css",
                "http://jdanyow.github.io/aurelia-examples");
        assertEquals("http://jdanyow.github.io/aurelia-examples/jspm_packages/npm/font-awesome@4.3.0/css/font-awesome.min.css", cacheId);
    }
}