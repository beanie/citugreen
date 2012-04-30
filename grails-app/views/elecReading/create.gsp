

<%@ page import="citu.ElecReading" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'elecReading.label', default: 'ElecReading')}" />
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
            <g:hasErrors bean="${elecReadingInstance}">
            <div class="errors">
                <g:renderErrors bean="${elecReadingInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="premise"><g:message code="elecReading.premise.label" default="Premise" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: elecReadingInstance, field: 'premise', 'errors')}">
                                    <g:select name="premise.id" from="${citu.Premise.list()}" optionKey="id" value="${elecReadingInstance?.premise?.id}" noSelection="['null': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="fileDate"><g:message code="elecReading.fileDate.label" default="File Date" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: elecReadingInstance, field: 'fileDate', 'errors')}">
                                    <g:datePicker name="fileDate" precision="day" value="${elecReadingInstance?.fileDate}" default="none" noSelection="['': '']" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="readingValueElec"><g:message code="elecReading.readingValueElec.label" default="Reading Value Elec" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: elecReadingInstance, field: 'readingValueElec', 'errors')}">
                                    <g:textField name="readingValueElec" value="${fieldValue(bean: elecReadingInstance, field: 'readingValueElec')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="realReadingElec"><g:message code="elecReading.realReadingElec.label" default="Real Reading Elec" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: elecReadingInstance, field: 'realReadingElec', 'errors')}">
                                    <g:textField name="realReadingElec" value="${fieldValue(bean: elecReadingInstance, field: 'realReadingElec')}" />
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
