package com.hannikkala.poc.util;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * User: bleed
 * Date: 10/03/16
 * Time: 11:28
 */
public class CacheIdUtil {

    private static final Log _log = LogFactoryUtil.getLog(CacheIdUtil.class);

    public static String createCacheId(String baseUrl, String file, String contextRoot) {
        if(!baseUrl.endsWith("/") && !file.startsWith("/")) {
            baseUrl += "/";
        }
        URL url = null;
        try {
            url = new URL(new URL(baseUrl), file);
            if(file.startsWith("http") && contextRoot.startsWith("http")) {
                URL contextUrl = new URL(contextRoot);
                contextRoot = contextUrl.getProtocol() + "://" + contextUrl.getHost();
            }
        } catch (MalformedURLException e) {
            _log.error("URL: " + baseUrl + file + " is malformed.", e);
        }
        return contextRoot + url.getPath();
    }
}
