<!DOCTYPE html>
<html>
<head>
<title><g:layoutTitle default="Grails" /></title>
<link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />
<link rel="shortcut icon"
	href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
<g:layoutHead />
<g:javascript library="application" />
<style type="text/css">
<!--
#tabs {
	border-bottom: .5em solid #009900;
	margin: 0;
	padding: 0;
}

#tabs li { 
	display:inline; 
	border-top: .1em solid #009900;
	border-left: .1em solid #009900;
	border-right: .1em solid #009900;
}
#tabs li a {
	text-decoration: none;
	padding: 0.25em 1em;
	color: #000;
}

#tabs li a:hover {
	padding: 0.25em 1em;
	background-color: #14FF14
}

#page1 #tabs li#tab1 a, #page2 #tabs li#tab2 a, #page3 #tabs li#tab3 a, .page4 li#tab4 a {
	padding: 0.25em 1em;
	background-color: #009900;
	color: #fff;
}
-->
</style>

</head>
<body id="${pageProperty(name:'body.id')}"> 
<div id="spinner" class="spinner" style="display: none;"><img
	src="${resource(dir:'images',file:'spinner.gif')}"
	alt="${message(code:'spinner.alt',default:'Loading...')}" /></div>
<div id="grailsLogo"><a href="${createLink(uri: '/')}"><img
	src="${resource(dir:'images',file:'CituGreen.jpg')}" alt="CituGreen"
	border="0" /></a></div>
<g:layoutBody />
</body>
</html>