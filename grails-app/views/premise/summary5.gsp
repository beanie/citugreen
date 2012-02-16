<%@ page import="citugreen.Premise" %>
<%@ page import="java.text.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/xml; charset=UTF-8" />
        <meta name="layout" content="html5main" />
        <g:set var="entityName" value="${message(code: 'premise.label', default: 'Premise')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
        <script type="text/javascript" src="http://www.google.com/jsapi"></script>
    </head>
    <body>
<section>
            <h1>Summary for: ${fieldValue(bean: premiseInstance, field: "flatNo")} ${fieldValue(bean: premiseInstance, field: "addressLine1")}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
</section>

<section>
<table>
<tr>

			<td valign="top" style="text-align: left;" class="value" colspan="5">
			Electric Usage ${fieldValue(bean: premiseInstance, field: "totalElecUsage")}kWh
			<td>
			Electric Costs &pound;${fieldValue(bean: premiseInstance, field: "totalElecCost")}
			<td>
			Heating Usage ${fieldValue(bean: premiseInstance, field: "totalHeatUsage")}kWh
			<td>
			Heating Cost &pound;${fieldValue(bean: premiseInstance, field: "totalHeatCost")}
			<td>
			Cold Water ${fieldValue(bean: premiseInstance, field: "totalColdWater")}L
			<td>
			Hot Water ${fieldValue(bean: premiseInstance, field: "totalHotWater")}L
			<td>
			Grey Water ${fieldValue(bean: premiseInstance, field: "totalGreyWater")}L
	</tr>		
</table>
</section>

<section>
			<%
			def elecGraphData = []
			for (i in premiseInstance.elecReadings) {
				def dataObj = [[DateFormat.getDateInstance(DateFormat.SHORT).format(i.fileDate), i.elecReading]]
				elecGraphData += dataObj
			}
			def waterGraphData = []
			for (i in premiseInstance.waterReadings) {
				def dataObj = [[DateFormat.getDateInstance(DateFormat.SHORT).format(i.fileDate), i.coldWater, i.hotWater, i.greyWater]]
				waterGraphData += dataObj
			}
			def heatGraphData = []
			for (i in premiseInstance.heatReadings) {
				def dataObj = [[DateFormat.getDateInstance(DateFormat.SHORT).format(i.dateCreated), i.heatReading]]
				heatGraphData += dataObj
			}
			%>	

			<table>		
			<tr>
			<td>
			<gvisualization:imageBarChart elementId="elecGraph" title="My Daily Electricity Usage" width="${300}" height="${200}" columns="[['string', 'Usage'], ['number', 'Electricity (kWh)']]" data="${elecGraphData}" />
			<div id="elecGraph"></div>

			<td>

			<gvisualization:imageBarChart elementId="heatGraph" title="My Daily Heat Usage" width="${300}" height="${200}" columns="[['string', 'Usage'], ['number', 'Electricity (kWh)']]" data="${heatGraphData}" />
			<div id="heatGraph"></div>

			<td>

			<gvisualization:imageBarChart elementId="waterGraph" title="My Daily Water Usage" width="${300}" height="${200}" columns="[['string', 'Usage'], ['number', 'Cold Water (m3)'], ['number', 'Hot Water (m3)'], ['number', 'Grey Water (m3)']]" data="${waterGraphData}" />
			<div id="waterGraph"></div>
			</tr>

			</table>	
</section>


	</body>
</html>