package com.hannikkala.poc.proxy.controller;

import com.hannikkala.poc.RootConfig;
import com.hannikkala.poc.TestConfig;
import com.hannikkala.poc.TestUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import io.netty.handler.codec.http.HttpHeaders;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.portlet.MockPortletPreferences;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.ReflectionUtils;

import javax.portlet.PortletPreferences;
import javax.portlet.ReadOnlyException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author Tommi Hännikkälä <tommi@hannikkala.com>
 * Date: 11/03/16
 * Time: 12:47
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, TestConfig.class})
public class ProxyControllerTest {

    private ProxyController proxyController;
    private ClientAndServer mockServer;

    @Autowired
    private CacheManager cacheManager;

    @Before
    public void setUp() throws Exception {
        proxyController = new ProxyController() {
            @Override
            protected PortletPreferences getPortletPreferencesByPortletId(String portletId) throws SystemException, PortalException {
                MockPortletPreferences preferences = new MockPortletPreferences();
                try {
                    preferences.setValue("root", "http://localhost:3555");
                } catch (ReadOnlyException e) {
                    throw new RuntimeException(e);
                }
                return preferences;
            }
        };
        ReflectionTestUtils.setField(proxyController, "cacheManager", cacheManager);
        mockServer = ClientAndServer.startClientAndServer(3555);
    }

    @After
    public void tearDown() throws Exception {
        mockServer.stop();
    }

    @Test
    public void testGetPortletId() throws Exception {
        String portletId = ProxyController.getPortletId("/poc-js-portlet/p/pocangularportlet_WAR_pocjsportlet_INSTANCE_MVFuHik6CyK0/styles/vendor-c2769e81fe.css");
        Assert.assertEquals("pocangularportlet_WAR_pocjsportlet_INSTANCE_MVFuHik6CyK0", portletId);
    }

    @Test
    public void testStaticProxy() throws Exception {
        mockServer.when(request("/path/to/angular.png"))
                .respond(response(TestUtil.getFileContents("website/angular.png"))
                        .withHeader(new Header(HttpHeaders.Names.CONTENT_TYPE, "image/png")));

        MockHttpServletResponse response = new MockHttpServletResponse();
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/js-portlet/p/portletId/path/to/angular.png");
        request.setContextPath("/js-portlet");
        proxyController.staticProxy(request, response);

        Assert.assertEquals("image/png", response.getHeader(HttpHeaders.Names.CONTENT_TYPE));

    }

}