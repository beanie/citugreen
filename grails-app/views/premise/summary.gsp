<%@ page import="citu.Premise" %>
<%@ page import="java.text.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/xml; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'premise.label', default: 'Premise')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        <script type="text/javascript" src="http://www.google.com/jsapi"></script>
        <gui:resources components="['tabView']"/>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></span>
        </div>
        <div class="body">
            <h1>Summary for: ${simpleView.flatNo} ${simpleView.addressLine1}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
            <div class="dialog">
                <table>
                    <tbody>
                    
                       <tr class="prop">
                       
                            <td valign="top" class="name"><g:message code="premise.user.label" default="Name" /></td>
                            
                            <td valign="top" class="value">${simpleView.user.firstName} ${simpleView.user.lastName}</td>
                            
                        </tr>
                        
                        <tr class="prop">
                       
                            <td valign="top" class="name"><g:message code="premise.user.label" default="Username" /></td>
                            
                            <td valign="top" class="value">${simpleView.user.userName}</td>
                            
                        </tr>
                    
                        <tr class="prop">
                            
                            <td valign="top" class="name"><g:message code="premise.flatNo.label" default="Address:" /></td>
                            
                            <td valign="top" class="value">${simpleView.flatNo} ${simpleView.addressLine1}<br />${simpleView.addressLine2}
                            </td>
                            
                        </tr>
                    
                        <tr class="prop">
                            <td valign="top" class="name"><g:message code="premise.postCode.label" default="Post Code" /></td>
                            
                            <td valign="top" class="value">${simpleView.postCode}</td>
                            
                        </tr>
                                                
                        <tr class="prop">
                            
                            <td valign="top" class="yui-skin-sam" colspan="2" align="center">
															
								<gui:tabView>
								    <gui:tab label="Electricity Readings" active="true">
								        kjkj
								    </gui:tab>
								    <gui:tab label="Heat Readings">
								        iuo
								    </gui:tab>
								    <gui:tab label="Water Readings">
								        hjk
								    </gui:tab>
								</gui:tabView>
								
							</td>
                            
                        </tr>
                    
                    </tbody>
                </table>
            </div>
        </div>
    </body>
</html>
