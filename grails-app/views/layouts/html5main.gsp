<!DOCTYPE html>
<html>
<head>
<title><g:layoutTitle default="Citu" /></title>
<link rel="stylesheet"
	href="${resource(dir:'css',file:'HTML5main.css')}" />
<link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
<g:layoutHead />
<g:javascript library="application" />
</head>
<body>
	<div id="spinner" class="spinner" style="display: none;">
		<img src="${resource(dir:'images',file:'spinner.gif')}"	alt="${message(code:'spinner.alt',default:'Loading...')}" />
	</div>
	<div id="grailsLogo">
		<a href="${createLink(uri: '/')}"><img src="${resource(dir:'images',file:'CituGreen.jpg')}" alt="CituGreen"	border="0" /></a>
	</div>

	<g:layoutBody />
</body>
</html>