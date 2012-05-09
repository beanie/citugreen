

<%@ page import="citu.Premise" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'premise.label', default: 'Premise')}" />
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
            <g:hasErrors bean="${premiseInstance}">
            <div class="errors">
                <g:renderErrors bean="${premiseInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="addressLine1"><g:message code="premise.addressLine1.label" default="Address Line1" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'addressLine1', 'errors')}">
                                    <g:textField name="addressLine1" value="${premiseInstance?.addressLine1}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="addressLine2"><g:message code="premise.addressLine2.label" default="Address Line2" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'addressLine2', 'errors')}">
                                    <g:textField name="addressLine2" value="${premiseInstance?.addressLine2}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="bathrooms"><g:message code="premise.bathrooms.label" default="Bathrooms" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'bathrooms', 'errors')}">
                                    <g:textField name="bathrooms" value="${fieldValue(bean: premiseInstance, field: 'bathrooms')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="bedrooms"><g:message code="premise.bedrooms.label" default="Bedrooms" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'bedrooms', 'errors')}">
                                    <g:textField name="bedrooms" value="${fieldValue(bean: premiseInstance, field: 'bedrooms')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="core"><g:message code="premise.core.label" default="Core" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'core', 'errors')}">
                                    <g:textField name="core" value="${premiseInstance?.core}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="flatNo"><g:message code="premise.flatNo.label" default="Flat No" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'flatNo', 'errors')}">
                                    <g:textField name="flatNo" value="${premiseInstance?.flatNo}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="occupied"><g:message code="premise.occupied.label" default="Occupied" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'occupied', 'errors')}">
                                    <g:checkBox name="occupied" value="${premiseInstance?.occupied}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="postCode"><g:message code="premise.postCode.label" default="Post Code" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'postCode', 'errors')}">
                                    <g:textField name="postCode" value="${premiseInstance?.postCode}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="premiseType"><g:message code="premise.premiseType.label" default="Premise Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'premiseType', 'errors')}">
                                    <g:textField name="premiseType" value="${premiseInstance?.premiseType}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="squareArea"><g:message code="premise.squareArea.label" default="Square Area" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'squareArea', 'errors')}">
                                    <g:textField name="squareArea" value="${fieldValue(bean: premiseInstance, field: 'squareArea')}" />
                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="user"><g:message code="premise.user.label" default="User" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: premiseInstance, field: 'user', 'errors')}">
                                    <g:select name="user.id" from="${citu.User.list()}" optionKey="id" value="${premiseInstance?.user?.id}"  />
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
