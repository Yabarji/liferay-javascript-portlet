package com.hannikkala.poc.portlet.controller;

import com.hannikkala.poc.service.WebsiteServiceImpl;
import com.hannikkala.poc.util.CacheIdUtil;
import com.jaunt.NotFound;
import com.jaunt.ResponseException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.io.IOException;

/**
 * User: bleed
 * Date: 25/02/16
 * Time: 18:55
 */
@Controller
@RequestMapping("view")
public class PortletController {

    private static final Log _log = LogFactoryUtil.getLog(PortletController.class);

    @Autowired
    private WebsiteServiceImpl websiteService;

    @Autowired
    private CacheManager cacheManager;

    @RenderMapping
    public String index(RenderRequest request, RenderResponse response, Model model) {
        User user = (User) request.getAttribute(WebKeys.USER);
        String userEmail = user != null ? user.getDisplayEmailAddress() : "anonymous";

        model.addAttribute("ajaxURL", "/delegate/rest/");
        model.addAttribute("standalone", false);
        model.addAttribute("authenticatedUser", userEmail);
        model.addAttribute("portletId", getPortletId(request));
        model.addAttribute("portletAppContextPath", request.getContextPath() + "/");

        PortletPreferences preferences = request.getPreferences();
        String root = preferences.getValue("root", null);
        boolean cdnMode = GetterUtil.getBoolean(preferences.getValue("cdnMode", "false"));

        if(root == null) {
            return "notconfigured";
        }

        String contextRoot = request.getContextPath() + "/p/" + getPortletId(request);
        if(cdnMode) {
            contextRoot = root;
        }

        String cacheId = CacheIdUtil.createCacheId(root, "/", contextRoot);

        Cache cache = cacheManager.getCache("default");
        String cacheSite = cache.get(cacheId, String.class);

        if(cacheSite != null) {
            return "cache:" + cacheId;
        }

        try {
            websiteService.fetchWebsite(root, "/", contextRoot);
        } catch (ResponseException e) {
            _log.error("Page not found.", e);
            return "notfound";
        } catch (NotFound notFound) {
            _log.error("Attribute not found", notFound);
            return "notfound";
        } catch (IOException e) {
            // Ignored
        }

        return "cache:" + cacheId;
    }

    private String getPortletId(PortletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
        return portletDisplay.getId();
    }
}
