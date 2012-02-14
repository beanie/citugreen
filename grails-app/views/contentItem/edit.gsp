

<%@ page import="citu.ContentItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'contentItem.label', default: 'ContentItem')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${contentItemInstance}">
            <div class="errors">
                <g:renderErrors bean="${contentItemInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${contentItemInstance?.id}" />
                <g:hiddenField name="version" value="${contentItemInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="contentBody"><g:message code="contentItem.contentBody.label" default="Content Body" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: contentItemInstance, field: 'contentBody', 'errors')}">
                                    <g:textArea name="contentBody" cols="40" rows="5" value="${contentItemInstance?.contentBody}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="contentTitle"><g:message code="contentItem.contentTitle.label" default="Content Title" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: contentItemInstance, field: 'contentTitle', 'errors')}">
                                    <g:textField name="contentTitle" value="${contentItemInstance?.contentTitle}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="published"><g:message code="contentItem.published.label" default="Published" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: contentItemInstance, field: 'published', 'errors')}">
                                    <g:checkBox name="published" value="${contentItemInstance?.published}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="save" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
