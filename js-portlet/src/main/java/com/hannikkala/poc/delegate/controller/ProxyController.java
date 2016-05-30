package com.hannikkala.poc.delegate.controller;

import com.hannikkala.poc.delegate.config.RequestConfig;
import com.hannikkala.poc.delegate.service.RequestConfigServiceImpl;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * @author Tommi Hännikkälä tommi@hannikkala.com
 * Date: 02/03/16
 * Time: 21:56
 */
@Controller
@RequestMapping("/rest/api/**")
public class ProxyController {

    @Autowired
    private RequestConfigServiceImpl requestConfigService;

    @RequestMapping(method = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.HEAD, RequestMethod.OPTIONS,
        RequestMethod.PATCH, RequestMethod.POST, RequestMethod.PUT, RequestMethod.TRACE})
    public void proxyRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
        RequestConfig configuration = requestConfigService.findConfiguration(request.getPathInfo());
        doProxyRequest(request, response, configuration.getLocation(), "/delegate/rest");
    }

    public static void doProxyRequest(HttpServletRequest request, HttpServletResponse response, String baseURL, String proxyContextPath) throws IOException {
        HttpRequest httpRequest = ProxyUtil.createHttpRequest(request, proxyContextPath);
        ProxyUtil.copyRequestHeaders(request, httpRequest);
        HttpClient client = HttpClientBuilder.create().build();

        URL url = new URL(baseURL);
        HttpHost httpHost = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        HttpResponse httpResponse = client.execute(httpHost, httpRequest);

        // Set response status
        response.setStatus(httpResponse.getStatusLine().getStatusCode());

        // Copy headers from proxy response
        ProxyUtil.copyResponseHeaders(httpResponse, request, response);

        // Write content from proxy response
        if(httpResponse.getEntity() != null) {
            httpResponse.getEntity().writeTo(response.getOutputStream());
        }

        response.flushBuffer();
    }

    private static class ProxyUtil {

        public static String rewriteUrl(HttpServletRequest request, String proxyContextPath) {
            String requestURI = request.getRequestURI();

            requestURI = requestURI.replaceFirst(proxyContextPath, "");
            return requestURI;
        }

        public static HttpRequest createHttpRequest(HttpServletRequest request, String proxyContextPath) throws IOException {
            String requestURI = rewriteUrl(request, proxyContextPath);
            if(request.getContentLength() > 0) {
                HttpEntityEnclosingRequest proxyReq = new BasicHttpEntityEnclosingRequest(request.getMethod(), requestURI);
                proxyReq.setEntity(new InputStreamEntity(request.getInputStream(), request.getContentLength()));
                return proxyReq;
            } else {
                return new BasicHttpRequest(request.getMethod(), requestURI);
            }
        }

        public static void copyRequestHeaders(HttpServletRequest servletRequest, HttpRequest proxyRequest) {
            // Get an Enumeration of all of the header names sent by the client
            Enumeration enumerationOfHeaderNames = servletRequest.getHeaderNames();
            while (enumerationOfHeaderNames.hasMoreElements()) {
                String headerName = (String) enumerationOfHeaderNames.nextElement();
                copyRequestHeader(servletRequest, proxyRequest, headerName);
            }
        }

        /**
         * Copy a request header from the servlet client to the proxy request.
         * This is easily overwritten to filter out certain headers if desired.
         */
        protected static void copyRequestHeader(HttpServletRequest servletRequest, HttpRequest proxyRequest,
                                         String headerName) {
            //Instead the content-length is effectively set via InputStreamEntity
            if (headerName.equalsIgnoreCase(HttpHeaders.CONTENT_LENGTH))
                return;

            Enumeration headers = servletRequest.getHeaders(headerName);
            while (headers.hasMoreElements()) {//sometimes more than one value
                String headerValue = (String) headers.nextElement();
                proxyRequest.addHeader(headerName, headerValue);
            }
        }

        /** Copy proxied response headers back to the servlet client. */
        protected static void copyResponseHeaders(HttpResponse proxyResponse, HttpServletRequest servletRequest,
                                           HttpServletResponse servletResponse) {
            for (Header header : proxyResponse.getAllHeaders()) {
                copyResponseHeader(servletRequest, servletResponse, header);
            }
        }

        /** Copy a proxied response header back to the servlet client.
         * This is easily overwritten to filter out certain headers if desired.
         */
        protected static void copyResponseHeader(HttpServletRequest servletRequest,
                                          HttpServletResponse servletResponse, Header header) {
            // We don't want to pass cookies from REST
            if("Set-Cookie".equalsIgnoreCase(header.getName())) {
                return;
            }
            String headerName = header.getName();
            String headerValue = header.getValue();
            servletResponse.addHeader(headerName, headerValue);
        }
    }
}
