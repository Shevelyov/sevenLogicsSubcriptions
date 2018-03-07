<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<div id="page">
	<nav class="navbar">
		<div class="container-fluid">
			<nav class="navbar navbar-default">
				<div class="container-fluid">
					<div class="navbar-header">
						<a class="navbar-brand" href="#"><img alt="img"
							src="resources/images/logo.png" id="logoimage"> </a>
						<div id="logo">
							<h1>SevenLogics</h1>
						</div>
					</div>
					<form class="navbar-form navbar-right">
						<ul class="nav navbar-nav navbar-right">
							<li><a href="index.html">Home</a></li>
							<li><a href="recipes.html">Recipes</a></li>
							<li><a href="calendar.html" id="calendarPage" style="display:none">Calendar</a></li>
							<li><a href="journal.html">Family Journal</a></li>
							<li><a href="expenses.html">Expenses</a></li>
							<li class="dropdown" id="userInfo" style="display:none">
					          <a href="#" class="dropdown-toggle" data-toggle="dropdown"><span id="loggedInUser"></span><span class="glyphicon glyphicon-user pull-right"></span></a>
					          <ul class="dropdown-menu">
					            <li><a href="#" id="logoutBtn">Sign Out <span class="glyphicon glyphicon-log-out pull-right"></span></a></li>
					          </ul>
					        </li>
						</ul>
					</form>
				</div>
			</nav>
		</div>
	</nav>
</div>