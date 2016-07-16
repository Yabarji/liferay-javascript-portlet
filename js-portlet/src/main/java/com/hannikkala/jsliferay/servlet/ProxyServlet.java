package com.hannikkala.jsliferay.servlet;

import com.hannikkala.jsliferay.servlet.config.Config;
import com.hannikkala.jsliferay.delegate.config.RequestConfig;
import com.hannikkala.jsliferay.servlet.service.RequestConfigServiceImpl;
import org.eclipse.jetty.client.api.Response;
import org.eclipse.jetty.http.HttpField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Locale;

public class ProxyServlet extends org.eclipse.jetty.proxy.ProxyServlet.Transparent {

    @Autowired
    private RequestConfigServiceImpl requestConfigService;

    public ProxyServlet() {
        super("http://localhost", "/");
    }

    @Override
    protected URI rewriteURI(HttpServletRequest request) {
        request.setAttribute("org.apache.catalina.ASYNC_SUPPORTED", true);
        RequestConfig configuration = requestConfigService.findConfiguration(request.getPathInfo());
        String path = rewriteUrl(request, "/delegate/rest");
        return URI.create(configuration.getLocation() + path).normalize();
    }

    @Override
    protected void onResponseHeaders(HttpServletRequest request, HttpServletResponse response, Response proxyResponse) {
        for (HttpField field : proxyResponse.getHeaders())
        {
            String headerName = field.getName();
            String newHeaderValue = filterResponseHeader(request, headerName, field.getValue());
            if (newHeaderValue == null || newHeaderValue.trim().length() == 0) {
                continue;
            }

            response.addHeader(headerName, newHeaderValue);
        }
    }

    protected String rewriteUrl(HttpServletRequest request, String proxyContextPath) {
        String requestURI = request.getRequestURI();

        requestURI = requestURI.replaceFirst(proxyContextPath, "");

        if(requestURI.substring(1).contains("/")) {
            int index = requestURI.indexOf('/', 1);
            requestURI = requestURI.substring(index);
        }

        return requestURI;
    }

    @Override
    public void init() throws ServletException {
        super.init();
        WebApplicationContext parent = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(parent);
        context.register(Config.class);
        context.refresh();
        context.getAutowireCapableBeanFactory().autowireBean(this);
    }
}
