package com.hannikkala.poc.portlet.controller;

import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.model.User;
import com.liferay.portal.theme.PortletDisplay;
import com.liferay.portal.theme.ThemeDisplay;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

/**
 * User: bleed
 * Date: 25/02/16
 * Time: 18:55
 */
@Controller
@RequestMapping("view")
public class PortletController {

    @RenderMapping
    public String index(RenderRequest request, RenderResponse response, Model model) {
        User user = (User) request.getAttribute(WebKeys.USER);
        String userScreenName = user != null ? user.getScreenName() : "anonymous";

        model.addAttribute("ajaxURL", "/delegate/rest/");
        model.addAttribute("standalone", false);
        model.addAttribute("authenticatedUser", userScreenName);
        model.addAttribute("portletId", getPortletId(request));
        model.addAttribute("portletAppContextPath", request.getContextPath() + "/");

        return "index";
    }

    private String getPortletId(PortletRequest request) {
        ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
        PortletDisplay portletDisplay = themeDisplay.getPortletDisplay();
        return portletDisplay.getId();
    }
}
