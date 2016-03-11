package com.hannikkala.poc.proxy.controller;

import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQueryFactoryUtil;
import com.liferay.portal.kernel.dao.orm.PropertyFactoryUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.PortletPreferences;
import com.liferay.portal.service.LayoutLocalServiceUtil;
import com.liferay.portal.service.PortletPreferencesLocalServiceUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: bleed
 * Date: 11/03/16
 * Time: 12:05
 */
@Controller
@RequestMapping("/**")
public class ProxyController {

    @Autowired
    private CacheManager cacheManager;

    @RequestMapping
    public void staticProxy(HttpServletRequest request, HttpServletResponse response) throws IOException, SystemException, PortalException {
        // /pocjsportlet/p/portletId/path
        String requestURI = request.getRequestURI();
        String portletId = getPortletId(requestURI);
        javax.portlet.PortletPreferences prefs = getPortletPreferencesByPortletId(portletId);
        String proxyContextPath = request.getContextPath() + "/p/" + portletId;

        com.hannikkala.poc.delegate.controller.ProxyController.doProxyRequest(request, response, prefs.getValue("root", ""), proxyContextPath);

    }

    public static String getPortletId(String requestURI) {
        Matcher matcher = Pattern.compile("/p/([^/]+)/").matcher(requestURI);
        matcher.find();
        return matcher.group(1);
    }

    private javax.portlet.PortletPreferences getPortletPreferencesByPortletId(String portletId) throws SystemException, PortalException {
        DynamicQuery dynamicQuery = DynamicQueryFactoryUtil.forClass(PortletPreferences.class)
                .add(PropertyFactoryUtil.forName("portletId").eq(portletId));
        List<PortletPreferences> list = PortletPreferencesLocalServiceUtil.dynamicQuery(dynamicQuery);
        PortletPreferences portletPreferences = list.get(0);
        Layout layout = LayoutLocalServiceUtil.getLayout(portletPreferences.getPlid());
        return PortletPreferencesFactoryUtil.getPortletSetup(layout, portletId, "");
    }

}
