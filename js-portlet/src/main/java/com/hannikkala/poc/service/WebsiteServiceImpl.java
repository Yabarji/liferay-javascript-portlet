package com.hannikkala.poc.service;

import com.hannikkala.poc.util.CacheIdUtil;
import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.ResponseException;
import com.jaunt.UserAgent;
import com.jaunt.util.HandlerForBinary;
import com.jaunt.util.HandlerForText;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.w3c.tidy.Tidy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * User: bleed
 * Date: 10/03/16
 * Time: 10:55
 */
@Service
@Scope("prototype")
public class WebsiteServiceImpl {

    private HandlerForText handlerForText = new HandlerForText();
    private HandlerForBinary handlerForBinary = new HandlerForBinary();

    @Autowired
    private CacheManager cacheManager;
    private Cache cache;

    @Cacheable(value = "default", key = "#contextRoot + #htmlFile")
    public String fetchWebsite(String baseUrl, String htmlFile, String contextRoot) throws Exception {
        cache = cacheManager.getCache("default");
        UserAgent userAgent = new UserAgent();
        userAgent.setHandler("text/css", handlerForText);
        userAgent.setHandler("text/javascript", handlerForText);
        userAgent.setHandler("application/javascript", handlerForText);
        userAgent.setHandler("application/x-javascript", handlerForText);
        userAgent.setHandler("image/gif", handlerForBinary);
        userAgent.setHandler("image/jpeg", handlerForBinary);
        userAgent.setHandler("image/png", handlerForBinary);

        userAgent.visit(baseUrl + htmlFile);

        Elements cssLinks = userAgent.doc.findEach("<link>");
        for(Element css : cssLinks) {
            String cssHref = css.getAt("href");
            String cacheId = CacheIdUtil.createCacheId(baseUrl, cssHref, contextRoot);
            fetchText(userAgent, cssHref, cacheId);
            css.setAttribute("href", cacheId);
        }
        Elements jsLinks = userAgent.doc.findEach("<script>");
        for(Element js : jsLinks) {
            if(!js.hasAttribute("src")) {
                continue;
            }
            String jsSrc = js.getAt("src");
            String cacheId = CacheIdUtil.createCacheId(baseUrl, jsSrc, contextRoot);
            fetchText(userAgent, jsSrc, cacheId);
            js.setAttribute("src", cacheId);
        }
        Elements imgTags = userAgent.doc.findEach("<img>");
        for(Element img : imgTags) {
            String imgSrc = img.getAt("src");
            String cacheId = CacheIdUtil.createCacheId(baseUrl, imgSrc, contextRoot);
            fetchBinary(userAgent, imgSrc, cacheId);
            img.setAttribute("src", cacheId);
        }

        Tidy tidy = new Tidy();
        tidy.setShowWarnings(false);
        tidy.setMakeClean(true);
        tidy.setQuiet(true);
        tidy.setTidyMark(false);
        tidy.setEscapeCdata(false);

        ByteArrayInputStream site = new ByteArrayInputStream(userAgent.doc.innerHTML().getBytes());
        ByteArrayOutputStream output = new ByteArrayOutputStream();

        tidy.parse(site, output);

        String out = output.toString().replaceAll("[//]*<!\\[CDATA\\[", "").replaceAll("[//]*]]>", "");

        return out;
    }

    public String fetchText(UserAgent ua, String file, String cacheId) throws ResponseException {
        ua.visit(file);
        String content = handlerForText.getContent();
        cache.put(cacheId, content);
        return content;
    }

    public byte[] fetchBinary(UserAgent ua, String file, String cacheId) throws ResponseException {
        ua.visit(file);
        byte[] content = handlerForBinary.getContent();
        cache.put(cacheId, content);
        return content;
    }
}
