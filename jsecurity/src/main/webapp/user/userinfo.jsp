<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
</head>
<body>
	<div>
	<fieldset>
		<legend>Normal Info:</legend>
		AuthType: <%=request.getAuthType() %><br/>
		Principal: <%= request.getUserPrincipal()%><br/>
		PrincipalName: <%= request.getUserPrincipal().getName()%><br/>
		RemoteUser: <%= request.getRemoteUser()%><br/>
		isRole(Tomcat): <%= request.isUserInRole("tomcat")%><br/>
		isRole(Admin): <%= request.isUserInRole("admin")%><br/>
	</fieldset>
	</div>
</body>
</html>