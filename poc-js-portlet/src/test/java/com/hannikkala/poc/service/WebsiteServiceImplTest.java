package com.hannikkala.poc.service;

import com.hannikkala.poc.RootConfig;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.mockserver.MockServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.ResourceUtils;

import java.io.IOException;

import static org.mockserver.model.HttpRequest.*;
import static org.mockserver.model.HttpResponse.*;

/**
 * User: bleed
 * Date: 10/03/16
 * Time: 17:25
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class})
public class WebsiteServiceImplTest {

    @Autowired
    WebsiteServiceImpl websiteService;

    @Autowired
    CacheManager cacheManager;

    private ClientAndServer mockServer;

    @Before
    public void setUp() throws Exception {
        mockServer = ClientAndServer.startClientAndServer(3555);
    }

    @Test
    public void testFetchWebsite() throws Exception {
        mockServer.when(request("/index.html"))
                .respond(response(getFileContents("website/index.html"))
                        .withHeader(new Header(HttpHeaders.Names.CONTENT_TYPE, MediaType.TEXT_HTML_VALUE)));
        mockServer.when(request("/app/index.css"))
                .respond(response(getFileContents("website/index.css"))
                        .withHeader(new Header(HttpHeaders.Names.CONTENT_TYPE, "text/css")));
        mockServer.when(request("/app/index.module.js"))
                .respond(response(getFileContents("website/index.module.js"))
                        .withHeader(new Header(HttpHeaders.Names.CONTENT_TYPE, "application/javascript")));
        mockServer.when(request("/bower_components/angular-toastr/dist/angular-toastr.css"))
                .respond(response(getFileContents("website/angular-toastr.css"))
                        .withHeader(new Header(HttpHeaders.Names.CONTENT_TYPE, "text/css")));
        mockServer.when(request("/angular.png"))
                .respond(response(getFileContents("website/angular.png"))
                        .withHeader(new Header(HttpHeaders.Names.CONTENT_TYPE, "image/png")));

        String s = websiteService.fetchWebsite("http://localhost:3555", "/index.html", "/mycontext/123");
        Cache cache = cacheManager.getCache("default");
        Assert.assertNotNull(cache.get("/mycontext/123/index.html"));
        Assert.assertNotNull(cache.get("/mycontext/123/app/index.css"));
        Assert.assertNotNull(cache.get("/mycontext/123/app/index.module.js"));
        Assert.assertNotNull(cache.get("/mycontext/123/bower_components/angular-toastr/dist/angular-toastr.css"));
        Assert.assertNotNull(cache.get("/mycontext/123/angular.png"));
    }

    private String getFileContents(String file) throws IOException {
        Resource res = new ClassPathResource(file);
        return FileUtils.readFileToString(res.getFile());
    }
}