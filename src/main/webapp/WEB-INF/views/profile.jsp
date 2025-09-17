<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<html>
<head>
    <title>Profile</title>
</head>
<body>
    <h2>Cập nhật thông tin cá nhân</h2>

    <form action="profile" method="post" enctype="multipart/form-data">
        <input type="hidden" name="user_id" value="${sessionScope.user.user_id}" />

        <p>Username: <b>${sessionScope.user.username}</b></p>

        <p>Fullname: <input type="text" name="fullname" value="${sessionScope.user.fullname}"/></p>
        <p>Phone: <input type="text" name="phone" value="${sessionScope.user.phone}"/></p>
        
        <p>Ảnh đại diện: 
            <input type="file" name="image"/>
            <br/>
            <c:if test="${not empty sessionScope.user.image}">
                <img src="uploads/${sessionScope.user.image}" width="120"/>
            </c:if>
        </p>

        <button type="submit">Cập nhật</button>
    </form>
</body>
</html>