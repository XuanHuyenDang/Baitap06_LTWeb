<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<title>Login</title>
<style>
body {
	font-family: Arial, sans-serif;
	background: #f8f9fa;
	display: flex;
	align-items: center;
	justify-content: center;
	height: 100vh;
}

.login-box {
	background: white;
	padding: 25px;
	border-radius: 8px;
	box-shadow: 0 2px 8px rgba(0, 0, 0, 0.2);
	width: 320px;
}

h2 {
	text-align: center;
	margin-bottom: 20px;
	color: #2c3e50;
}

label {
	display: block;
	margin-bottom: 6px;
	font-weight: bold;
}

input[type="text"], input[type="password"] {
	width: 95%;
	padding: 8px;
	margin-bottom: 15px;
	border: 1px solid #ccc;
	border-radius: 5px;
}

input[type="submit"] {
	width: 100%;
	padding: 10px;
	background: #3498db;
	border: none;
	border-radius: 5px;
	color: white;
	font-weight: bold;
	cursor: pointer;
}

input[type="submit"]:hover {
	background: #2980b9;
}

.error {
	color: red;
	text-align: center;
	margin-top: 10px;
}
</style>
</head>
<body>

	<div class="login-box">
		<h2>Login</h2>
		<form action="login" method="post">
			<label for="username">Username:</label> <input type="text"
				id="username" name="username" required /> <label for="password">Password:</label>
			<input type="password" id="password" name="password" required /> <input
				type="submit" value="Login" />
		</form>

		<p class="error">${error}</p>
	</div>

</body>
</html>
