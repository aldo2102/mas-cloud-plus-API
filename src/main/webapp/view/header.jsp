<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="jsp">
<head>
	<title>MAS-Cloud+</title>
	<meta charset="UTF-8">
	<meta name="description" content="Cloud 83 - hosting template ">
	<meta name="keywords" content="cloud, hosting, creative, html">
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<!-- Favicon -->
	<link href="img/favicon.ico" rel="shortcut icon"/>

	<!-- Google Font -->
	<link href="https://fonts.googleapis.com/css?family=Poppins:400,400i,500,500i,600,600i,700,700i" rel="stylesheet">

	<!-- Stylesheets -->
	<link rel="stylesheet" href="css/bootstrap.min.css"/>
	<link rel="stylesheet" href="css/font-awesome.min.css"/>
	<link rel="stylesheet" href="css/magnific-popup.css"/>
	<link rel="stylesheet" href="css/owl.carousel.min.css"/>
	<link rel="stylesheet" href="css/style.css"/>
	<link rel="stylesheet" href="css/animate.css"/>


	<!--[if lt IE 9]>
	  <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
	  <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
	<![endif]-->

</head>
<body>
	<!-- Page Preloder -->
	<div id="preloder">
		<div class="loader"></div>
	</div>

	<!-- Header section -->
	<header class="header-section">
		<div class="container">
			<a href="index.jsp" class="logoTop">
				MAS-Cloud+
			</a>
			<!-- Switch button -->
			<div class="nav-switch">
				<div class="ns-bar"></div>
			</div>
			<div class="header-right">
				<ul class="main-menu">
					<li class="active"><a href="?">Home</a></li>
					<li><a href="#">About us</a></li>
					<li><a href="#">Project</a></li>
					<li><a href="#">Contact</a></li>
				</ul>
				<div class="header-btns">
				<% HttpSession session1 = request.getSession();%>
			
				<%if (session1.getAttribute("use") == null){ %>
					<a href="?page=6" class="site-btn sb-c2">Login</a>
					<a href="?page=2" class="site-btn sb-c3">Register</a>
				<%} else { %>
				    <p class="site-btn sb-c2">Hello <%=session1.getAttribute("use") %></p>
					<a href="?page=5" class="site-btn sb-c3">Request a VM</a>
					<a href="LoginServelet" class="site-btn sb-c3">Logout</a>
				<%} %>	
				</div>
			</div>
		</div>
	</header>
	<!-- Header section end -->
