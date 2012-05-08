

<%@ page import="citu.Stats" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'stats.label', default: 'Stats')}" />
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
            <g:hasErrors bean="${statsInstance}">
            <div class="errors">
                <g:renderErrors bean="${statsInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateNow"><g:message code="stats.dateNow.label" default="Date Now" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: statsInstance, field: 'dateNow', 'errors')}">
                                    <g:datePicker name="dateNow" precision="day" value="${statsInstance?.dateNow}"  />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="logCode"><g:message code="stats.logCode.label" default="Log Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: statsInstance, field: 'logCode', 'errors')}">
                                    <g:textField name="logCode" value="${statsInstance?.logCode}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="logMessage"><g:message code="stats.logMessage.label" default="Log Message" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: statsInstance, field: 'logMessage', 'errors')}">
                                    <g:textField name="logMessage" value="${statsInstance?.logMessage}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="messageType"><g:message code="stats.messageType.label" default="Message Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: statsInstance, field: 'messageType', 'errors')}">
                                    <g:textField name="messageType" value="${statsInstance?.messageType}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="premise"><g:message code="stats.premise.label" default="Premise" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: statsInstance, field: 'premise', 'errors')}">
                                    <g:select name="premise.id" from="${citu.Premise.list()}" optionKey="id" value="${statsInstance?.premise?.id}"  />
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
