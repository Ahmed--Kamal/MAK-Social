
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1256">
<title>Home</title>
</head>
<body>
<div align="center" style="color:#C80000"><h3>Welcome: ${it.name}</h3></div>
<div><a href="/social/showFriendRequests/"><b>Show Friend Requests</b></a></div>
<div><a href="/social/sendAMessage/"><b>Send a message</b></a></div>
<div align="right"><a href="/social/LogOut/"><b>Log Out</b></a><br></div>
<form action="/social/sendFriendRequest" method="post">
	<div align="left">
	Send friend request to: <input type="text" name="recieverMail" placeholder="Enter reciever mail" />
	<input type="submit" value="Send my request">
	</div>
</form>
</body>
</html>