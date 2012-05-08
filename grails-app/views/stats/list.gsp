
<%@ page import="citu.Stats" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'stats.label', default: 'Stats')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'stats.id.label', default: 'Id')}" />
                        
                            <g:sortableColumn property="dateNow" title="${message(code: 'stats.dateNow.label', default: 'Date Now')}" />
                        
                            <g:sortableColumn property="logCode" title="${message(code: 'stats.logCode.label', default: 'Log Code')}" />
                        
                            <g:sortableColumn property="logMessage" title="${message(code: 'stats.logMessage.label', default: 'Log Message')}" />
                        
                            <g:sortableColumn property="messageType" title="${message(code: 'stats.messageType.label', default: 'Message Type')}" />
                        
                            <th><g:message code="stats.premise.label" default="Premise" /></th>
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${statsInstanceList}" status="i" var="statsInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${statsInstance.id}">${fieldValue(bean: statsInstance, field: "id")}</g:link></td>
                        
                            <td><g:formatDate date="${statsInstance.dateNow}" /></td>
                        
                            <td>${fieldValue(bean: statsInstance, field: "logCode")}</td>
                        
                            <td>${fieldValue(bean: statsInstance, field: "logMessage")}</td>
                        
                            <td>${fieldValue(bean: statsInstance, field: "messageType")}</td>
                        
                            <td>${fieldValue(bean: statsInstance, field: "premise")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${statsInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
