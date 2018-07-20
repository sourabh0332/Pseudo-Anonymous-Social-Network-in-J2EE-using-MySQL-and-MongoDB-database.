<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Login @ GeekNetwork</title>
</head>
<body>
<% 
	session = request.getSession(false);
	String userID = (String)session.getAttribute("userID");
	if(userID != null){
		response.sendRedirect("home.jsp");
	}
%>
	<form action="login" method="post">
		email : <input type="text" name="email" />
		<br/>
		password : <input type="password" name="password" />
		<br/>
		<button type="submit">Login</button>
	</form>
	<a href="register.jsp"><button>Register</button></a>
</body>
</html>