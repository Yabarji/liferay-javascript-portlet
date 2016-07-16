# Application development

The main idea is to create portlet that only has to be configured to use with any JavaScript application and REST API. 

#### Common issues with Liferay & single page applications 

There are a few things needed to make JavaScript application on Liferay portal. Here's a list of some common issues:

- JavaScript application must stay inside portlet boundaries, not taking over the whole page
- In portal, pages usually have other URL than `/`. Then also JavaScript application must need to know that rather dynamically. This applies to [path URL history](PATH_HISTORY.md)
- In development environments portal and JavaScript run on different ports

## Using Thymeleaf template

You might need [variables](PORTLET_VARIABLES.md) from the portlet. For that purpose there are few steps to take:

- Add XML namespaces to `<html>`-element

```html
<html xmlns="http://www.w3.org/1999/xhtml" xmlns:th="http://www.thymeleaf.org">
```

- Add `<script>`-tag with `th:inline="javascript"`-attribute. It makes [Thymeleaf](http://www.thymeleaf.org) to kick in and replace variable values.

```html
<script type="application/javascript" th:inline="javascript">
    // AJAX base URL with default value.
    var ajaxURL = /*[[${ajaxURL}]]*/ "http://localhost:8081/";
    // Use variable however you need.
 </script>
```

> Note! You can find all variables can be found [here](PORTLET_VARIABLES.md). 

See framework specific instructions:

* [Angular1](ANGULAR1.md) 
* Others under construction