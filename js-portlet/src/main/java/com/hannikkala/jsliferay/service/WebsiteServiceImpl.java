package com.hannikkala.jsliferay.service;

import com.hannikkala.jsliferay.util.CacheIdUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 10/03/16
 * Time: 10:55
 */
@Service
@Scope("prototype")
public class WebsiteServiceImpl {

    @Autowired
    private CacheManager cacheManager;
    private Cache cache;

    private static final Log log = LogFactoryUtil.getLog(WebsiteServiceImpl.class);

    public String fetchWebsite(String baseUrl, String htmlFile, String contextRoot) throws IOException {
        cache = cacheManager.getCache("default");
        Document document = Jsoup.connect(baseUrl + htmlFile).get();
        Elements cssElems = document.select("link[rel='stylesheet'], link[type='text/css']");
        for(Element css : cssElems) {
            String cssHref = relativeToAbsolute(baseUrl, css.attr("href"));
            String cacheId = CacheIdUtil.createCacheId(baseUrl, cssHref, contextRoot);
            fetchText(cssHref, cacheId);
            css.attr("href", cacheId);
        }

        Elements jsElems = document.select("script[src!='']");
        for(Element js : jsElems) {
            String jsSrc = relativeToAbsolute(baseUrl, js.attr("src"));
            String cacheId = CacheIdUtil.createCacheId(baseUrl, jsSrc, contextRoot);
            fetchText(jsSrc, cacheId);
            js.attr("src", cacheId);
        }

        Elements scriptElems = document.select("script");
        for(Element script : scriptElems) {
            if("".equals(script.attr("type"))) {
                script.attr("type", "text/javascript");
            }
        }

        Elements imgElems = document.select("img[src!='']");
        for(Element img : imgElems) {
            String imgSrc = relativeToAbsolute(baseUrl, img.attr("src"));
            String cacheId = CacheIdUtil.createCacheId(baseUrl, imgSrc, contextRoot);
            fetchBinary(imgSrc, cacheId);
            img.attr("src", cacheId);
        }

        String content = document.outerHtml();
        String cacheId = CacheIdUtil.createCacheId(baseUrl, htmlFile, contextRoot);
        cache.put(cacheId, content);

        return content;
    }

    public String fetchText(String file, String cacheId) {
        Connection.Response response = null;
        try {
            response = Jsoup.connect(file).ignoreContentType(true).execute();
        } catch (IOException e) {
            log.warn("Error fetching text source.", e);
            return "";
        }
        String content = response.body();
        cache.put(cacheId, content);
        return content;
    }

    public byte[] fetchBinary(String file, String cacheId) {
        Connection.Response binaryResponse = null;
        try {
            binaryResponse = Jsoup.connect(file).ignoreContentType(true).execute();
        } catch (IOException e) {
            log.warn("Error fetching binary source.", e);
            return new byte[0];
        }
        byte[] content = binaryResponse.bodyAsBytes();
        cache.put(cacheId, content);
        return content;
    }

    private String relativeToAbsolute(String baseUrl, String relativeUrl) {
        try {
            URL base = new URL(baseUrl);
            URL absoluteUrl = new URL(base, relativeUrl);
            return absoluteUrl.toExternalForm().replace("../", "");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
