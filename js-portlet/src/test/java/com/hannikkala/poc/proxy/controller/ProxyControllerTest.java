package com.hannikkala.poc.proxy.controller;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * User: bleed
 * Date: 11/03/16
 * Time: 12:47
 */
public class ProxyControllerTest {

    @Test
    public void testGetPortletId() throws Exception {
        String portletId = ProxyController.getPortletId("/poc-js-portlet/p/pocangularportlet_WAR_pocjsportlet_INSTANCE_MVFuHik6CyK0/styles/vendor-c2769e81fe.css");
        Assert.assertEquals("pocangularportlet_WAR_pocjsportlet_INSTANCE_MVFuHik6CyK0", portletId);
    }

}