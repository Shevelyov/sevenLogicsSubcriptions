<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<body>
	<script src='resources/js/loginModal.js'></script>
	<script src='resources/js/homePage.js'></script>
	<%@ include file="navigation.jsp" %>
	<div id="content">
		<img alt="img" src="resources/images/family-organizer-today.png" id="image">
		<div id="textContent">
			<h2>FAMILY, FUN, LOVE, SHARE</h2>
			<h2>
				Welcome to the family members<br> This page allows to add the
				family members<br> Share images and location<br> Share
				Love and ExpensesS<br>
			</h2>
			<h3>Must Have application for a happy and organized family</h3>
		</div>
			
		<a href="#" class="btn btn-primary btn-lg loginBtn" role="button" data-toggle="modal" data-target="#login-modal">Login</a>
	
		<%@ include file="loginModal.jsp" %>
	</div>
</body>
</html>
