

<%@ page import="citu.HeatReading" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'heatReading.label', default: 'HeatReading')}" />
        <title><g:message code="default.create.label" args="[entityName]" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="default.create.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${heatReadingInstance}">
            <div class="errors">
                <g:renderErrors bean="${heatReadingInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="premise"><g:message code="heatReading.premise.label" default="Premise" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: heatReadingInstance, field: 'premise', 'errors')}">
                                    <g:select name="premise.id" from="${citu.Premise.list()}" optionKey="id" value="${heatReadingInstance?.premise?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fileDate"><g:message code="heatReading.fileDate.label" default="File Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: heatReadingInstance, field: 'fileDate', 'errors')}">
                                    <g:datePicker name="fileDate" precision="day" value="${heatReadingInstance?.fileDate}" default="none" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="readingValueHeat"><g:message code="heatReading.readingValueHeat.label" default="Reading Value Heat" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: heatReadingInstance, field: 'readingValueHeat', 'errors')}">
                                    <g:textField name="readingValueHeat" value="${fieldValue(bean: heatReadingInstance, field: 'readingValueHeat')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="realReadingHeat"><g:message code="heatReading.realReadingHeat.label" default="Real Reading Heat" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: heatReadingInstance, field: 'realReadingHeat', 'errors')}">
                                    <g:textField name="realReadingHeat" value="${fieldValue(bean: heatReadingInstance, field: 'realReadingHeat')}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
