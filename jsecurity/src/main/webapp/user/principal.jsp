<%@page import="java.util.*"%>
<%@page import="org.apache.catalina.realm.GenericPrincipal"%>
<%@page import="javax.security.auth.spi.LoginModule"%>
<%@page import="javax.security.auth.login.LoginContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
	<%
	   GenericPrincipal principal = (GenericPrincipal)request.getUserPrincipal();
	   request.setAttribute("principal", principal);
	%>
	<div>
	<fieldset>
		<legend>Normal Info:</legend>
		<p><label>Name:</label><span>${principal.name}</span></p>
		<p><label>Password:</label><span>${principal.password}</span></p>
		<p><label>Roles:</label><span><c:forEach items="${principal.roles}" var="role">${role};</c:forEach></span></p>
	</fieldset>
	</div>
	
</body>
</html>