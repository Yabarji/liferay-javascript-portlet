package com.hannikkala.poc.util;

import junit.framework.Assert;

import static org.junit.Assert.*;

/**
 * User: bleed
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
}