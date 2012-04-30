
<%@ page import="citu.WaterReading" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'waterReading.label', default: 'WaterReading')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'waterReading.id.label', default: 'Id')}" />
                        
                            <th><g:message code="waterReading.premise.label" default="Premise" /></th>
                        
                            <g:sortableColumn property="fileDate" title="${message(code: 'waterReading.fileDate.label', default: 'File Date')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'waterReading.dateCreated.label', default: 'Date Created')}" />
                        
                            <g:sortableColumn property="readingValueCold" title="${message(code: 'waterReading.readingValueCold.label', default: 'Reading Value Cold')}" />
                        
                            <g:sortableColumn property="readingValueGrey" title="${message(code: 'waterReading.readingValueGrey.label', default: 'Reading Value Grey')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${waterReadingInstanceList}" status="i" var="waterReadingInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${waterReadingInstance.id}">${fieldValue(bean: waterReadingInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: waterReadingInstance, field: "premise")}</td>
                        
                            <td><g:formatDate date="${waterReadingInstance.fileDate}" /></td>
                        
                            <td><g:formatDate date="${waterReadingInstance.dateCreated}" /></td>
                        
                            <td>${fieldValue(bean: waterReadingInstance, field: "readingValueCold")}</td>
                        
                            <td>${fieldValue(bean: waterReadingInstance, field: "readingValueGrey")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${waterReadingInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
