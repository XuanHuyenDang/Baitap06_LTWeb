<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<nav>
    <ul style="list-style:none; display:flex; gap:15px; padding:10px; margin:0; border-bottom:1px solid #ccc;">
        <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
        <li><a href="${pageContext.request.contextPath}/category/list">Categories</a></li>
        <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
        <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
    </ul>
</nav>