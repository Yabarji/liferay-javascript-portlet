package com.hannikkala.jsliferay.portlet.controller;

import com.hannikkala.jsliferay.service.AppContextServiceImpl;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.portlet.ConfigurationAction;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portlet.PortletPreferencesFactoryUtil;
import org.springframework.cache.CacheManager;
import org.springframework.context.ApplicationContext;

import javax.portlet.*;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 09/03/16
 * Time: 20:50
 */
public class ConfigurationController implements ConfigurationAction {

    private final static Log _log = LogFactoryUtil.getLog(ConfigurationController.class);

    @Override
    public void processAction(PortletConfig portletConfig, ActionRequest actionRequest, ActionResponse actionResponse) throws Exception {
        String portletResource = ParamUtil.getString(actionRequest, "portletResource");
        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(actionRequest, portletResource);
        prefs.setValue("root", actionRequest.getParameter("root"));
        prefs.setValue("developerMode", actionRequest.getParameter("developerMode"));

        prefs.store();

        SessionMessages.add(actionRequest, portletConfig.getPortletName() + ".doConfigure");
    }

    @Override
    public String render(PortletConfig portletConfig, RenderRequest renderRequest, RenderResponse renderResponse) throws Exception {
        if(renderRequest.getParameter("clearCache") != null) {
            ApplicationContext appContext = AppContextServiceImpl.getAppContext();
            CacheManager cacheManager = appContext.getBean(CacheManager.class);
            cacheManager.getCache("default").clear();
        }

        String portletResource = ParamUtil.getString(renderRequest, "portletResource");
        PortletPreferences prefs = PortletPreferencesFactoryUtil.getPortletSetup(renderRequest, portletResource);
        renderRequest.setAttribute("root", prefs.getValue("root", ""));
        renderRequest.setAttribute("developerMode", prefs.getValue("developerMode", "false"));
        return "/WEB-INF/jsp/configuration.jsp";
    }
}
