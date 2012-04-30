
<%@ page import="citu.WaterReading" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'waterReading.label', default: 'WaterReading')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
            <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.show.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: waterReadingInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.premise.label" default="Premise" /></td>
                            
                            <td valign="top" class="value"><g:link controller="premise" action="show" id="${waterReadingInstance?.premise?.id}">${waterReadingInstance?.premise?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.fileDate.label" default="File Date" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${waterReadingInstance?.fileDate}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.dateCreated.label" default="Date Created" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${waterReadingInstance?.dateCreated}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.readingValueCold.label" default="Reading Value Cold" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: waterReadingInstance, field: "readingValueCold")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.readingValueGrey.label" default="Reading Value Grey" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: waterReadingInstance, field: "readingValueGrey")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.readingValueHot.label" default="Reading Value Hot" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: waterReadingInstance, field: "readingValueHot")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.realValueCold.label" default="Real Value Cold" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: waterReadingInstance, field: "realValueCold")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.realValueGrey.label" default="Real Value Grey" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: waterReadingInstance, field: "realValueGrey")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="waterReading.realValueHot.label" default="Real Value Hot" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: waterReadingInstance, field: "realValueHot")}</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${waterReadingInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
