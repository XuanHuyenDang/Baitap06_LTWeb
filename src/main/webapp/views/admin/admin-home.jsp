<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<html>
<head>
<title>Admin Home</title>
<style>
body {
    font-family: Arial, sans-serif;
    margin: 20px;
}
h2 { text-align: center; }
a { text-decoration: none; margin: 0 10px; }
table {
    width: 80%;
    margin: 20px auto;
    border-collapse: collapse;
}
th, td {
    border: 1px solid #000;
    padding: 8px;
    text-align: center;
}
.btn {
    padding: 4px 8px;
    border: 1px solid #000;
    margin: 0 3px;
}
</style>
</head>
<body>
    <h2>Trang Home (Admin)</h2>
    <div style="text-align: center;">
        <a href="${pageContext.request.contextPath}/category/list" class="btn">
            Quản lý Category
        </a>
        <a href="${pageContext.request.contextPath}/video/list" class="btn">
            Quản lý Video
        </a>
        <a href="${pageContext.request.contextPath}/admin/user/list" class="btn">
            Quản lý User
        </a>
        <a href="${pageContext.request.contextPath}/logout" class="btn">
            Thoát
        </a>
    </div>

    <table>
        <tr>
            <th>ID</th>
            <th>Tên</th>
            <th>Người tạo</th>
        </tr>
        <c:forEach var="c" items="${categories}">
            <tr>
                <!-- đổi sang cate_id, cate_name theo entity -->
                <td>${c.cate_id}</td>
                <td>${c.cate_name}</td>
                <td>
                    <c:choose>
                        <c:when test="${not empty c.user}">
                            ${c.user.username}
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
