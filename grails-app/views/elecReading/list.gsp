
<%@ page import="citu.ElecReading" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'elecReading.label', default: 'ElecReading')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'elecReading.id.label', default: 'Id')}" />
                        
                            <th><g:message code="elecReading.premise.label" default="Premise" /></th>
                        
                            <g:sortableColumn property="fileDate" title="${message(code: 'elecReading.fileDate.label', default: 'File Date')}" />
                        
                            <g:sortableColumn property="dateCreated" title="${message(code: 'elecReading.dateCreated.label', default: 'Date Created')}" />
                        
                            <g:sortableColumn property="readingValueElec" title="${message(code: 'elecReading.readingValueElec.label', default: 'Reading Value Elec')}" />
                        
                            <g:sortableColumn property="realReadingElec" title="${message(code: 'elecReading.realReadingElec.label', default: 'Real Reading Elec')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${elecReadingInstanceList}" status="i" var="elecReadingInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${elecReadingInstance.id}">${fieldValue(bean: elecReadingInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: elecReadingInstance, field: "premise")}</td>
                        
                            <td><g:formatDate date="${elecReadingInstance.fileDate}" /></td>
                        
                            <td><g:formatDate date="${elecReadingInstance.dateCreated}" /></td>
                        
                            <td>${fieldValue(bean: elecReadingInstance, field: "readingValueElec")}</td>
                        
                            <td>${fieldValue(bean: elecReadingInstance, field: "realReadingElec")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${elecReadingInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
