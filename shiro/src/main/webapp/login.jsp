<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="javax.security.auth.login.LoginContext"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login Page</title>
</head>
<body>
	<h1>Login to My Web Application</h1>
	<p>If you have been issued a username and password, key them in here now!</p>
	<form method="post" action="login">
		<fieldset>
	        <legend>Login </legend>	
			<p><label for="username">Username</label> <input type="text" size="15" maxlength="25" name="username"></p>
			<p><label for="password">Password</label> <input type="password" size="15" maxlength="25" name="password"></p>
			<p><label for="rememberMe">Remember Me</label> <input type="checkbox" size="15" maxlength="25" name="rememberMe"></p>
			<p><input value="Login" type="submit">&nbsp;&nbsp;&nbsp;&nbsp;<input value="Clear" type="reset"></p>
			<p>${error}</p>
		</fieldset>
	</form>
</body>
</html>