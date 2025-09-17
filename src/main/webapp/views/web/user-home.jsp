<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
<title>User/Admin Home</title>
<!-- CSS ở trên -->
<style>
body {
	font-family: Arial, sans-serif;
	margin: 20px;
	background: #f4f6f9;
}

h2 {
	color: #2c3e50;
	text-align: center;
}

a {
	text-decoration: none;
	margin: 0 10px;
	color: #2980b9;
	font-weight: bold;
}

a:hover {
	color: #d35400;
}

table {
	width: 80%;
	margin: 20px auto;
	border-collapse: collapse;
	background: #fff;
	box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
}

th, td {
	border: 1px solid #ddd;
	padding: 10px;
	text-align: center;
}

th {
	background: #3498db;
	color: white;
}

tr:nth-child(even) {
	background: #f9f9f9;
}

.btn {
	padding: 6px 12px;
	border-radius: 5px;
	font-size: 14px;
	margin: 0 3px;
}

.btn-edit {
	background: #27ae60;
	color: white;
}

.btn-delete {
	background: #e74c3c;
	color: white;
}

.btn-add {
	background: #3498db;
	color: white;
}

.btn-logout {
	background: #7f8c8d;
	color: white;
}
</style>

</head>
<body>
	<h2>Trang Home (User)</h2>
	<div style="text-align: center;">
		 <a href="${pageContext.request.contextPath}/category/list"
			class="btn btn-add">Quản lý Category</a> <a
			href="${pageContext.request.contextPath}/logout"
			class="btn btn-logout">Thoát</a>
	</div>

	<table>
		<tr>
			<th>ID</th>
			<th>Tên</th>
			<th>Người tạo</th>
		</tr>
		<c:forEach var="c" items="${categories}">
			<tr>
				<td>${c.cate_id}</td>
				<td>${c.cate_name}</td>
				<td>${c.user.username}</td>
			</tr>
		</c:forEach>
	</table>
</body>
</html>
