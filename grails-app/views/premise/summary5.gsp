<%@ page import="citu.Premise" %>
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
            <h1>Summary for: ${simpleView.flatNo} ${simpleView.addressLine1}</h1>
            <g:if test="${flash.message}">
            <div class="message">${flash.message}</div>
            </g:if>
</section>

<section>
<table>
<tr>

			<td valign="top" style="text-align: left;" class="value" colspan="5">
			Electric Usage ${simpleView.electricity.elecTotalUsage}kWh
			<td>
			Electric Costs &pound;${simpleView.electricity.elecTotalCost}
			<td>
			Heating Usage TODO kWh
			<td>
			Heating Cost &pound TODO
			<td>
			Cold Water ${simpleView.water.coldTotalUsage}L
			<td>
			Hot Water ${simpleView.water.hotTotalUsage}L
			<td>
			Grey Water ${simpleView.water.greyTotalUsage}L
	</tr>		
</table>
</section>

<section>
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

			<table>		
			<tr>
			<td>
			<gvisualization:imageBarChart elementId="elecGraph" title="My Electricity Usage" width="${300}" height="${200}" columns="[['string', 'Usage'], ['number', 'Electricity (kWh)']]" data="${elecGraphData}" />
			<div id="elecGraph"></div>

			<td>

			<gvisualization:imageBarChart elementId="heatGraph" title="My Heat Usage" width="${300}" height="${200}" columns="[['string', 'Usage'], ['number', 'Electricity (kWh)']]" data="${heatGraphData}" />
			<div id="heatGraph"></div>

			<td>

			<gvisualization:imageBarChart elementId="waterGraph" title="My Water Usage" width="${300}" height="${200}" columns="[['string', 'Usage'], ['number', 'Cold Water (m3)'], ['number', 'Hot Water (m3)'], ['number', 'Grey Water (m3)']]" data="${waterGraphData}" />
			<div id="waterGraph"></div>
			</tr>

			</table>	
</section>


	</body>
</html>