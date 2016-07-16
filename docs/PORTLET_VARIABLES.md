# Portlet variables

First of all portlet has to fetch the index page. Modern JavaScript frameworks come with some kind of container to run, for example NodeJS. The portlet fetches and processes the index page as [Thymeleaf](http://www.thymeleaf.org) template. Thymeleaf is non-intrusive because it's designed in a way that the HTML page can be developed standalone. Browsers don't care about attributes or any syntax it uses. Elegant, I'd say :)

###### Variables that will be processed for index.html

Variable            | Description
--------------------|----------------------------------------------------------------
ajaxURL             | Relative URL to REST API proxy. Usually /rest/api
standalone          | Is application standalone? Will be set to *false*
authenticatedUser   | Assigns authenticated user email. For example test@liferay.com
portletId           | Assigns full portlet ID and instance ID. Example: **p_p_id_jsportlet_WAR_jsportlet_INSTANCE_MVFuHik6CyK0_**
portletAppContextPath | This variable can be used as prefix to images and any other resources.
portletFriendlyUrl | This variable tells you the URL where portlet sits on. Very useful for [Path URL history](PATH_HISTORY.md) applications. Example: **/web/guest/home** (since 2.0)
locale              | Locale Liferay uses. Useful for I18N. Example: **en_US** (since 2.0)

###### Example variables

```javascript
<script type="text/javascript" th:inline="javascript">
var ajaxURL = /*[[${ajaxURL}]]*/ "http://localhost:8081/";
var isStandalone = /*[[${standalone}]]*/ true;
var authenticatedUser = /*[[${authenticatedUser}]]*/ "anonymous";
var portletId = /*[[${portletId}]]*/ 'pocAngularClient';
var portletAppContextPath = /*[[${portletAppContextPath}]]*/ '';
var portletFriendlyUrl = /*[[${portletFriendlyUrl}]]*/ '';
var locale = /*[[${locale}]]*/ '';
// Do here whatever you need to use them.
</script>
```
