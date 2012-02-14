
<%@ page import="citu.ContentItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'contentItem.label', default: 'ContentItem')}" />
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
                            <td valign="top" class="name"><g:message code="contentItem.id.label" default="Id" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: contentItemInstance, field: "id")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="contentItem.contentBody.label" default="Content Body" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: contentItemInstance, field: "contentBody")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="contentItem.contentTitle.label" default="Content Title" /></td>
                            
                            <td valign="top" class="value">${fieldValue(bean: contentItemInstance, field: "contentTitle")}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="contentItem.dateCreated.label" default="Date Created" /></td>
                            
                            <td valign="top" class="value"><g:formatDate date="${contentItemInstance?.dateCreated}" /></td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="contentItem.published.label" default="Published" /></td>
                            
                            <td valign="top" class="value"><g:formatBoolean boolean="${contentItemInstance?.published}" /></td>
                            
                        </tr>
                        <g:if test="${contentItemInstance.image1}">
                        <tr class="prop">
                            <td colspan="2" valign="top" class="name"><img src="${createLinkTo(dir:'contentImages/'+contentItemInstance.id,
                               file:''+contentItemInstance.image1)}"
           alt="${contentItemInstance.image1}"
           title="${contentItemInstance.image1}" /></td>
                        </tr>
                        </g:if>
                        
                        <g:if test="${contentItemInstance.image2}">
                        <tr class="prop">
                            <td colspan="2" valign="top" class="name"><img src="${createLinkTo(dir:'contentImages/'+contentItemInstance.id,
                               file:''+contentItemInstance.image2)}"
           alt="${contentItemInstance.image2}"
           title="${contentItemInstance.image2}" /></td>
                        </tr>
                        </g:if>
                    
                    </tbody>
                </table>
            </div>
            <div class="buttons">
                <g:form>
                    <g:hiddenField name="id" value="${contentItemInstance?.id}" />
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'default.button.edit.label', default: 'Edit')}" /></span>
                    <span class="button"><g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
                </g:form>
            </div>
        </div>
    </body>
</html>
