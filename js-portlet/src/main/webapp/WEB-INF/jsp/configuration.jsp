<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="portlet" uri="http://java.sun.com/portlet_2_0" %>
<%@taglib prefix="liferay-portlet" uri="http://liferay.com/tld/portlet" %>
<%@ page import="com.liferay.portal.kernel.util.Constants" %>

<portlet:defineObjects />

<form action="<liferay-portlet:actionURL portletConfiguration="true" />" method="post" name="<portlet:namespace />fm">
    <input name="<portlet:namespace /><%=Constants.CMD%>" type="hidden" value="<%=Constants.UPDATE%>" />
    <div>
        <label>UI root path (we find index.html there)</label>
        <input type="text" name="<portlet:namespace/>root" value="<%=request.getAttribute("root")%>" />
    </div>
    <div>
        <input type="button" value="Save" onClick="submitForm(document.<portlet:namespace />fm);" />
    </div>
    <div>
        <liferay-portlet:renderURL portletConfiguration="true" var="clearCacheURL">
            <liferay-portlet:param name="clearCache" value="true" />
        </liferay-portlet:renderURL>
        <a href="${clearCacheURL}">Clear cache</a>
    </div>
</form>

