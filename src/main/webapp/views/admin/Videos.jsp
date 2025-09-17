<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<html>
<head><title>Quản lý Video</title></head>
<body>

    <!-- Chỉ Admin (role_id = 3) mới thấy form thêm/sửa -->
    <c:if test="${not empty sessionScope.user and sessionScope.user.role_id == 3}">
        <h2>${video != null ? "Edit Video" : "Add Video"}</h2>
        <form method="post" action="${pageContext.request.contextPath}/video/${video != null ? "edit" : "add"}">
            <c:if test="${video != null}">
                <input type="hidden" name="video_id" value="${video.videoId}" />
            </c:if>

            <label>Title:</label>
            <input type="text" name="title" value="${video != null ? video.title : ''}" required /><br/>

            <label>Description:</label>
            <textarea name="description">${video != null ? video.description : ''}</textarea><br/>

            <label>URL:</label>
            <input type="text" name="url" value="${video != null ? video.url : ''}" /><br/>

            <label>Poster:</label>
            <input type="text" name="poster" value="${video != null ? video.poster : ''}" /><br/>

            <label>Views:</label>
            <input type="number" name="views" value="${video != null ? video.views : 0}" /><br/>

            <label>Active:</label>
            <input type="checkbox" name="active" value="1"
                   ${video != null and video.active ? "checked" : ""}/> Active<br/>

            <input type="submit" value="Save" />
        </form>
        <hr/>
    </c:if>

    <h2>Danh sách Videos</h2>
    <table border="1">
        <tr>
            <th>ID</th><th>Title</th><th>Views</th><th>Active</th>
            <c:if test="${not empty sessionScope.user and sessionScope.user.role_id == 3}">
                <th>Action</th>
            </c:if>
        </tr>

        <c:forEach var="v" items="${videos}">
            <tr>
                <td>${v.videoId}</td>
                <td>${v.title}</td>
                <td>${v.views}</td>
                <td><c:out value="${v.active ? 'Yes' : 'No'}"/></td>
                <c:if test="${not empty sessionScope.user and sessionScope.user.role_id == 3}">
                    <td>
                        <a href="${pageContext.request.contextPath}/video/edit?id=${v.videoId}">Edit</a>
                        <a href="${pageContext.request.contextPath}/video/delete?id=${v.videoId}"
                           onclick="return confirm('Xóa video này?')">Delete</a>
                    </td>
                </c:if>
            </tr>
        </c:forEach>
    </table>
</body>
</html>
