package com.hannikkala.jsliferay.delegate.service;

import com.hannikkala.jsliferay.RootConfig;
import com.hannikkala.jsliferay.TestConfig;
import com.hannikkala.jsliferay.delegate.config.RequestConfig;
import com.hannikkala.jsliferay.servlet.service.RequestConfigServiceImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 11/03/16
 * Time: 14:22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RootConfig.class, TestConfig.class})
@TestPropertySource(properties = {"jsportlet.rest.resource: classpath:test.yml"})
public class RequestConfigServiceImplTest {

    @Autowired
    private RequestConfigServiceImpl requestConfigService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testFindConfiguration() throws Exception {
        RequestConfig configuration = requestConfigService.findConfiguration("/rest/api/todo");
        assertNotNull("Configuration for todo REST must not be null.", configuration);
    }

    @Test
    public void testFindConfigurationSubpath() throws Exception {
        RequestConfig configuration = requestConfigService.findConfiguration("/rest/api/todo/123/user");
        assertNotNull("Configuration for todo REST must not be null.", configuration);
    }

    @Test
    public void testFindConfigurationForTestAPI() throws Exception {
        RequestConfig configuration = requestConfigService.findConfiguration("/rest/api/test");
        assertNotNull("Configuration for test REST must not be null.", configuration);
    }

    @Test(expected = RuntimeException.class)
    public void testFindConfigurationMissing() throws Exception {
        requestConfigService.findConfiguration("/doesnotexist");
    }
}