<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
    <title>Categories</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background: #f8f9fa;
            padding: 20px;
        }
        h2 {
            color: #2c3e50;
        }
        form {
            background: white;
            padding: 20px;
            border-radius: 8px;
            width: 350px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }
        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        input[type="text"] {
            width: 95%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
        }
        .buttons {
            display: flex;
            gap: 10px;
        }
        input[type="submit"] {
            background: #27ae60;
            color: white;
            padding: 8px 14px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"]:hover {
            background: #219150;
        }
        .cancel-btn {
            display: inline-block;
            padding: 8px 14px;
            background: #e74c3c;
            color: white;
            border-radius: 5px;
            text-decoration: none;
        }
        .cancel-btn:hover {
            background: #c0392b;
        }
        table {
            border-collapse: collapse;
            width: 600px;
            background: white;
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 2px 6px rgba(0,0,0,0.1);
        }
        table th, table td {
            padding: 12px 15px;
            border-bottom: 1px solid #ddd;
            text-align: left;
        }
        table th {
            background: #2c3e50;
            color: white;
        }
        table tr:hover {
            background: #f1f1f1;
        }
        .action-links a {
            margin-right: 10px;
            text-decoration: none;
            color: #3498db;
        }
        .action-links a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>

    <!-- Form add/edit -->
    <h2>${category != null ? "Edit Category" : "Add Category"}</h2>
    <form method="post"
          action="${pageContext.request.contextPath}/category/${category != null ? "edit" : "add"}">
        <c:if test="${category != null}">
            <input type="hidden" name="cate_id" value="${category.cate_id}" />
        </c:if>

        <label for="cate_name">Tên Category:</label>
        <input type="text" id="cate_name" name="cate_name"
               value="${category != null ? category.cate_name : ''}" required />

        <div class="buttons">
            <input type="submit" value="Save" />
            <a href="${pageContext.request.contextPath}/category/list" class="cancel-btn">Cancel</a>
        </div>
    </form>

    <!-- Danh sách category -->
    <h2>Category List</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên Category</th>
                <th>User</th>
                <th>Hành động</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="c" items="${categories}">
                <tr>
                    <td>${c.cate_id}</td>
                    <td>${c.cate_name}</td>
                    <td>${c.user.username}</td>
                    <td class="action-links">
                        <a href="${pageContext.request.contextPath}/category/edit?id=${c.cate_id}">Edit</a>
                        <a href="${pageContext.request.contextPath}/category/delete?id=${c.cate_id}"
                           onclick="return confirm('Bạn có chắc muốn xóa?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

</body>
</html>
