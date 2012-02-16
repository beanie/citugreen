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
								
								<%
								
								def elecGraphData = []
								for (i in simpleView.electricity.elecReadings) {
									def dataObj = [[DateFormat.getDateInstance(DateFormat.SHORT).format(i.dateTime), i.readingValue]]
									elecGraphData += dataObj
								}
								def waterGraphData = []
								for (i in simpleView.water.waterReadings) {
									def dataObj = [[DateFormat.getDateInstance(DateFormat.SHORT).format(i.dateTime), i.readingValueCold, i.readingValueHot, i.readingValueGrey]]
									waterGraphData += dataObj
								}
								/*
								def heatGraphData = []
								for (i in premiseInstance.heatReadings) {
									def dataObj = [[DateFormat.getDateInstance(DateFormat.SHORT).format(i.dateCreated), i.readingValueHeat]]
									heatGraphData += dataObj
								}*/
								 %>								
								<gui:tabView>
								    <gui:tab label="Electricity Readings" active="true">
								        <gvisualization:barCoreChart elementId="elecGraph" title="My Electricity Usage" width="${450}" height="${300}" columns="[['string', 'Usage'], ['number', 'Electricity (kWh)']]" data="${elecGraphData}" />
										<div id="elecGraph"></div>
								    </gui:tab>
								    <gui:tab label="Heat Readings">
								        <gvisualization:barCoreChart elementId="heatGraph" title="My Heat Usage" width="${450}" height="${300}" columns="[['string', 'Usage'], ['number', 'Electricity (kWh)']]" data="${heatGraphData}" />
										<div id="heatGraph"></div>
								    </gui:tab>
								    <gui:tab label="Water Readings">
								        <gvisualization:barCoreChart elementId="waterGraph" title="My Water Usage" width="${450}" height="${300}" columns="[['string', 'Usage'], ['number', 'Cold Water (m3)'], ['number', 'Hot Water (m3)'], ['number', 'Grey Water (m3)']]" data="${waterGraphData}" />
										<div id="waterGraph"></div>
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
