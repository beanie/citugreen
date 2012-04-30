

<%@ page import="citu.WaterReading" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'waterReading.label', default: 'WaterReading')}" />
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
            <g:hasErrors bean="${waterReadingInstance}">
            <div class="errors">
                <g:renderErrors bean="${waterReadingInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form method="post" >
                <g:hiddenField name="id" value="${waterReadingInstance?.id}" />
                <g:hiddenField name="version" value="${waterReadingInstance?.version}" />
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="premise"><g:message code="waterReading.premise.label" default="Premise" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'premise', 'errors')}">
                                    <g:select name="premise.id" from="${citu.Premise.list()}" optionKey="id" value="${waterReadingInstance?.premise?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="fileDate"><g:message code="waterReading.fileDate.label" default="File Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'fileDate', 'errors')}">
                                    <g:datePicker name="fileDate" precision="day" value="${waterReadingInstance?.fileDate}" default="none" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="readingValueCold"><g:message code="waterReading.readingValueCold.label" default="Reading Value Cold" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'readingValueCold', 'errors')}">
                                    <g:textField name="readingValueCold" value="${fieldValue(bean: waterReadingInstance, field: 'readingValueCold')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="readingValueGrey"><g:message code="waterReading.readingValueGrey.label" default="Reading Value Grey" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'readingValueGrey', 'errors')}">
                                    <g:textField name="readingValueGrey" value="${fieldValue(bean: waterReadingInstance, field: 'readingValueGrey')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="readingValueHot"><g:message code="waterReading.readingValueHot.label" default="Reading Value Hot" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'readingValueHot', 'errors')}">
                                    <g:textField name="readingValueHot" value="${fieldValue(bean: waterReadingInstance, field: 'readingValueHot')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="realValueCold"><g:message code="waterReading.realValueCold.label" default="Real Value Cold" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'realValueCold', 'errors')}">
                                    <g:textField name="realValueCold" value="${fieldValue(bean: waterReadingInstance, field: 'realValueCold')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="realValueGrey"><g:message code="waterReading.realValueGrey.label" default="Real Value Grey" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'realValueGrey', 'errors')}">
                                    <g:textField name="realValueGrey" value="${fieldValue(bean: waterReadingInstance, field: 'realValueGrey')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                  <label for="realValueHot"><g:message code="waterReading.realValueHot.label" default="Real Value Hot" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: waterReadingInstance, field: 'realValueHot', 'errors')}">
                                    <g:textField name="realValueHot" value="${fieldValue(bean: waterReadingInstance, field: 'realValueHot')}" />
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
