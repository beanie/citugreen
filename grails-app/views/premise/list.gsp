
<%@ page import="citu.Premise" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'premise.label', default: 'Premise')}" />
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
                        
                            <g:sortableColumn property="id" title="${message(code: 'premise.id.label', default: 'Id')}" />
                            
                            <g:sortableColumn property="flatNo" title="${message(code: 'premise.flatNo.label', default: 'flatNo')}" />
                        
                            <g:sortableColumn property="addressLine1" title="${message(code: 'premise.addressLine1.label', default: 'Address Line1')}" />
                        
                            <g:sortableColumn property="addressLine2" title="${message(code: 'premise.addressLine2.label', default: 'Address Line2')}" />
                        
                            <g:sortableColumn property="bathrooms" title="${message(code: 'premise.bathrooms.label', default: 'Bathrooms')}" />
                        
                            <g:sortableColumn property="bedrooms" title="${message(code: 'premise.bedrooms.label', default: 'Bedrooms')}" />
                        
                            <g:sortableColumn property="core" title="${message(code: 'premise.core.label', default: 'Core')}" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${premiseInstanceList}" status="i" var="premiseInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="show" id="${premiseInstance.id}">${fieldValue(bean: premiseInstance, field: "id")}</g:link></td>
                            
                            <td>${fieldValue(bean: premiseInstance, field: "flatNo")}</td>
                        
                            <td>${fieldValue(bean: premiseInstance, field: "addressLine1")}</td>
                        
                            <td>${fieldValue(bean: premiseInstance, field: "addressLine2")}</td>
                        
                            <td>${fieldValue(bean: premiseInstance, field: "bathrooms")}</td>
                        
                            <td>${fieldValue(bean: premiseInstance, field: "bedrooms")}</td>
                        
                            <td>${fieldValue(bean: premiseInstance, field: "core")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${premiseInstanceTotal}" />
            </div>
        </div>
    </body>
</html>
