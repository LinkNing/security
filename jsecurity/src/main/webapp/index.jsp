﻿<%@page import="java.util.*"%>
<%@page import="org.apache.catalina.realm.GenericPrincipal"%>
<%@page import="javax.security.auth.spi.LoginModule"%>
<%@page import="javax.security.auth.login.LoginContext"%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
	<div>
		<h1>Welcome</h1>
		<fieldset>
			<a href="login.html">Login</a> &nbsp;&nbsp;
			<a href="logout.jsp">Logout</a>
		</fieldset>
		<fieldset>
			<legend>admin pages</legend>
			<p><a href="admin/welcome.jsp">welcome</a></p>
		</fieldset>
		<fieldset>
			<legend>user pages</legend>
			<p><a href="user/welcome.jsp">welcome</a></p>
			<p><a href="user/userinfo.jsp">userinfo.jsp</a></p>
			<p><a href="user/memoryUser.jsp">memoryUser.jsp</a></p>
			<p><a href="user/principal.jsp">principal.jsp</a></p>
		</fieldset>
	</div>
</body>
</html>