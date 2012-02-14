
<%@ page import="citu.ContentItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'contentItem.label', default: 'ContentItem')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.list.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="list">
                <table>
                    <thead>
                        <tr>
                        
                            <g:sortableColumn property="id" title="${message(code: 'contentItem.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="contentBody" title="${message(code: 'contentItem.contentBody.label', default: 'Content Body')}" />
                        
                            <g:sortableColumn property="contentTitle" title="${message(code: 'contentItem.contentTitle.label', default: 'Content Title')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'contentItem.dateCreated.label', default: 'Date Created')}" />
                        
                            <g:sortableColumn property="published" title="${message(code: 'contentItem.published.label', default: 'Published')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${contentItemInstanceList}" status="i" var="contentItemInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${contentItemInstance.id}">${fieldValue(bean: contentItemInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: contentItemInstance, field: "contentBody")}</td>
                        
                            <td>${fieldValue(bean: contentItemInstance, field: "contentTitle")}</td>
                        
                            <td><g:formatDate date="${contentItemInstance.dateCreated}" /></td>
                        
                            <td><g:formatBoolean boolean="${contentItemInstance.published}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${contentItemInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
