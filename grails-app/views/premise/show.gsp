
<%@ page import="citu.Premise" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'premise.label', default: 'Premise')}" />
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
                            <td valign="top" class="name"><g:message code="premise.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.addressLine1.label" default="Address Line1" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "addressLine1")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.addressLine2.label" default="Address Line2" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "addressLine2")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.bathrooms.label" default="Bathrooms" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "bathrooms")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.bedrooms.label" default="Bedrooms" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "bedrooms")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.core.label" default="Core" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "core")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.flatNo.label" default="Flat No" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "flatNo")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.occupied.label" default="Occupied" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${premiseInstance?.occupied}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.postCode.label" default="Post Code" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "postCode")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.premiseType.label" default="Premise Type" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "premiseType")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.setTobBoxes.label" default="Set Tob Boxes" /></td>
                            
                            <td valign="top" style="text-align: left;" class="value">
                                <ul>
                                <g:each in="${premiseInstance.setTobBoxes}" var="s">
                                    <li><g:link controller="setTopBox" action="show" id="${s.id}">${s?.encodeAsHTML()}</g:link></li>
                                </g:each>
                                </ul>
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.squareArea.label" default="Square Area" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: premiseInstance, field: "squareArea")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.user.label" default="User" /></td>
                            
                            <td valign="top" class="value"><g:link controller="user" action="show" id="${premiseInstance?.user?.id}">${premiseInstance?.user?.encodeAsHTML()}</g:link></td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${premiseInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
