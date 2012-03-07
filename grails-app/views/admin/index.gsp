<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<meta name="layout" content="main" />
<title>Login</title>
<script type="text/javascript"
	src="/js/yui/2.7.0/utilities/utilities.js"></script>
<gui:resources components="['tabView']" />
</head>
<body id="page1">
	<div class="nav">
		<span class="menuButton">Welcome back ${session.user.firstName}</span>
	</div>
	<br />
	<div class="body">
		<ul id="tabs">
			<li id="tab1"><a href="ztab1.htm">Flats</a></li>
			<li id="tab2"><a href="ztab2.htm">Tab 2</a></li>
			<li id="tab3"><a href="ztab3.htm">Tab 3</a></li>
			<li id="tab4"><a href="ztab4.htm">Tab 4</a></li>
		</ul>

	</div>
</body>
</html>
