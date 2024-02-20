<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Login Success</title>
</head>
<body>
 <div align="center">
  <h1>You have logged in successfully</h1>
  <p>Welcome, <%= session.getAttribute("name") %></p>
  <p>Your phone number: <%= session.getAttribute("phoneNumber") %></p>
 </div>
</body>
</html>
