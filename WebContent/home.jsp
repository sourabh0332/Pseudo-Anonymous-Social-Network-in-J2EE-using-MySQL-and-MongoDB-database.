<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%
String email = null, userName = null, sessionID = null;
String firstName = null, lastName = null, college = null, msgCount = null;
	String userID;// = null;
	session = request.getSession(false);
	userID = (String)session.getAttribute("userID");
	if(userID == null){
		response.sendRedirect("login.jsp");
	}
	else{
		userID = (String)session.getAttribute("userID");
		sessionID = null;
		firstName = (String)session.getAttribute("firstName");
		lastName = (String)session.getAttribute("lastName");
		college = (String)session.getAttribute("college");
		msgCount = (String)session.getAttribute("msgCount");
	}
%>
<title>Geek@work</title>
<script src="http://code.jquery.com/jquery-latest.min.js"></script>
<script>
//$(document).ready(function(){
//	$('#checkMessageBtn')
//});
$(document).on("click", "#checkMessageBtn", function(){
	$.get("checkNewMessage", function(result){
		$("#allMessages").text(result);
	});
});
</script>
</head>
<body>
Hello <%=firstName %><br/>
College : <%=college %><br/>
Message Count : <%=msgCount %><br/>
<br/>
<form action="logout" method="post">
<button  type="submit">Logout</button>
</form>
<br/>
<br/>

Messaging here : <br/>
<form action="sendMesage" method="post">
Message : <input type="text" name="message"/><br/>
Reciever UserID : <input type="text" name="recieverID" /><br/>
<button id="sendMessageBtn" type="submit">Send</button>
</form>

<textarea id="allMessages" rows="20" cols="60" readonly></textarea>


<!-- <form action="checkNewMessage" method="post"> -->
<button id="checkMessageBtn">Check New Message</button>
<!-- </form> -->



</body>
</html>