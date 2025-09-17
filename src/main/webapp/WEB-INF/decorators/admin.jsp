<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><sitemesh:write property="title" default="Admin Area"/></title>
    <link rel="stylesheet"
          href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"/>
</head>
<body class="bg-light">
    <%@ include file="/commons/header.jsp" %>
    <div class="container mt-4">
        <h1 class="text-center text-danger">Admin Panel</h1>
        <hr/>
        <sitemesh:write property="body"/>
    </div>
    <%@ include file="/commons/footer.jsp" %>
</body>
</html>