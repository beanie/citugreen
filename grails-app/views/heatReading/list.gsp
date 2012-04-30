
<%@ page import="citu.HeatReading" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'heatReading.label', default: 'HeatReading')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'heatReading.id.label', default: 'Id')}" />
                        
                            <th><g:message code="heatReading.premise.label" default="Premise" /></th>
                        
                            <g:sortableColumn property="fileDate" title="${message(code: 'heatReading.fileDate.label', default: 'File Date')}" />
                        
                            <g:sortableColumn property="readingValueHeat" title="${message(code: 'heatReading.readingValueHeat.label', default: 'Reading Value Heat')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'heatReading.dateCreated.label', default: 'Date Created')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${heatReadingInstanceList}" status="i" var="heatReadingInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${heatReadingInstance.id}">${fieldValue(bean: heatReadingInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: heatReadingInstance, field: "premise")}</td>
                        
                            <td><g:formatDate date="${heatReadingInstance.fileDate}" /></td>
                        
                            <td>${fieldValue(bean: heatReadingInstance, field: "readingValueHeat")}</td>
                        
                            <td><g:formatDate date="${heatReadingInstance.dateCreated}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${heatReadingInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
