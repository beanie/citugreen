<%@ page import="citu.ContentItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'contentItem.label', default: 'ContentItem')}" />
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
            <g:hasErrors bean="${contentItemInstance}">
            <div class="errors">
                <g:renderErrors bean="${contentItemInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:uploadForm action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="contentTitle"><g:message code="contentItem.contentTitle.label" default="Content Title" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: contentItemInstance, field: 'contentTitle', 'errors')}">
                                    <g:textField name="contentTitle" value="${contentItemInstance?.contentTitle}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="messageType"><g:message code="contentItem.messageType.label" default="Message Type" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: contentItemInstance, field: 'messageType', 'errors')}">
                                    <g:select name="messageType" from="${['usageTip', 'Local Amenities','Events', 'Energy Saving Devices','Facilities','Managing Agent','Introduction','Useful Contacts','Concierge Caretaker','Apt Services','Safety Security','Greenhouse Facilities']}" value="${contentItemInstance?.messageType}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="contentBody"><g:message code="contentItem.contentBody.label" default="Content Body" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: contentItemInstance, field: 'contentBody', 'errors')}">
                                    <g:textArea name="contentBody" cols="40" rows="5" value="${contentItemInstance?.contentBody}" />
                                </td>
                            </tr>
                            
                            <tr class="prop">
							    <td valign="top" class="name">
							      	<label for="image1">Image:</label>
							    </td>
							    <td valign="top">
							      	<input type="file" id="tmpImage1" name="tmpImage1"/>
							    </td>
							</tr>
							
							<tr class="prop">
							    <td valign="top" class="name">
							      	<label for="image2">Image:</label>
							    </td>
							    <td valign="top">
							      	<input type="file" id="tmpImage2" name="tmpImage2"/>
							    </td>
							</tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="published"><g:message code="contentItem.published.label" default="Published" /></label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: contentItemInstance, field: 'published', 'errors')}">
                                    <g:checkBox name="published" value="${contentItemInstance?.published}" />
                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" /></span>
                </div>
            </g:uploadForm>
        </div>
    </body>
</html>
