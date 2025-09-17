<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width,initial-scale=1" />
    <title>Quản lý User</title>

    <!-- Bootstrap 5.3.8 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">

    <style>
        body { background:#f8f9fa; }
        .page-header { margin: 24px 0; }
        .table-actions a { margin-right: .25rem; }
        .avatar {
            width:48px;height:48px;object-fit:cover;border-radius:50%;
            border:1px solid rgba(0,0,0,.08);
        }
    </style>
</head>
<body>
<div class="container py-4">

    <div class="d-flex align-items-center justify-content-between page-header">
        <h1 class="h3 m-0">Quản lý User</h1>
        <c:if test="${not empty sessionScope.user and sessionScope.user.role_id == 3}">
            <!-- Nút cuộn tới form thêm -->
            <a href="#userFormCard" class="btn btn-primary">
                <i class="bi bi-plus-circle"></i> Thêm mới
            </a>
        </c:if>
    </div>

    <!-- Chỉ Admin (role_id = 3) mới thấy form thêm/sửa -->
    <c:if test="${not empty sessionScope.user and sessionScope.user.role_id == 3}">
        <div id="userFormCard" class="card shadow-sm mb-4">
            <div class="card-header">
                <strong>${userObj != null ? "Sửa người dùng" : "Thêm người dùng"}</strong>
            </div>
            <div class="card-body">
                <form method="post"
                      action="${pageContext.request.contextPath}/admin/user/${userObj != null ? "edit" : "add"}"
                      class="row g-3">

                    <c:if test="${userObj != null}">
                        <input type="hidden" name="user_id" value="${userObj.user_id}" />
                    </c:if>

                    <div class="col-md-6">
                        <label class="form-label">Username</label>
                        <input type="text" name="username" class="form-control"
                               value="${userObj != null ? userObj.username : ''}" required />
                    </div>

                    <div class="col-md-6">
                        <label class="form-label">Password</label>
                        <input type="password" name="password" class="form-control"
                               value="${userObj != null ? userObj.password : ''}" required />
                    </div>

                    <div class="col-md-4">
                        <label class="form-label">Role</label>
                        <select name="role_id" class="form-select">
                            <option value="1"
                                ${userObj != null and userObj.role_id == 1 ? "selected" : ""}>User</option>
                            <option value="2"
                                ${userObj != null and userObj.role_id == 2 ? "selected" : ""}>Manager</option>
                            <option value="3"
                                ${userObj != null and userObj.role_id == 3 ? "selected" : ""}>Admin</option>
                        </select>
                    </div>

                    <div class="col-md-8">
                        <label class="form-label">Fullname</label>
                        <input type="text" name="fullname" class="form-control"
                               value="${userObj != null ? userObj.fullname : ''}" />
                    </div>

                    <div class="col-md-6">
                        <label class="form-label">Phone</label>
                        <input type="text" name="phone" class="form-control"
                               value="${userObj != null ? userObj.phone : ''}" />
                    </div>

                    <div class="col-md-6">
                        <label class="form-label">Image (URL)</label>
                        <input type="text" name="image" class="form-control"
                               value="${userObj != null ? userObj.image : ''}" />
                    </div>

                    <div class="col-12">
                        <button type="submit" class="btn btn-success">
                            ${userObj != null ? "Cập nhật" : "Lưu mới"}
                        </button>
                        <a href="${pageContext.request.contextPath}/admin/user/list" class="btn btn-outline-secondary">
                            Hủy
                        </a>
                    </div>
                </form>
            </div>
        </div>
    </c:if>

    <div class="card shadow-sm">
        <div class="card-header">
            <strong>Danh sách Users</strong>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-striped table-hover align-middle mb-0">
                    <thead class="table-light">
                        <tr>
                            <th style="width:72px">ID</th>
                            <th>Username</th>
                            <th>Role</th>
                            <th>Fullname</th>
                            <th>Phone</th>
                            <th>Image</th>
                            <c:if test="${not empty sessionScope.user and sessionScope.user.role_id == 3}">
                                <th style="width:160px">Action</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${users}">
                            <tr>
                                <td><span class="badge text-bg-secondary">${u.user_id}</span></td>
                                <td class="fw-medium">${u.username}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${u.role_id == 3}">
                                            <span class="badge text-bg-danger">Admin</span>
                                        </c:when>
                                        <c:when test="${u.role_id == 2}">
                                            <span class="badge text-bg-warning">Manager</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge text-bg-info">User</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>${u.fullname}</td>
                                <td>${u.phone}</td>
                                <td>
                                    <c:if test="${not empty u.image}">
                                        <img src="${u.image}" alt="avatar" class="avatar" />
                                    </c:if>
                                </td>
                                <c:if test="${not empty sessionScope.user and sessionScope.user.role_id == 3}">
                                    <td class="table-actions">
                                        <a class="btn btn-sm btn-primary"
                                           href="${pageContext.request.contextPath}/admin/user/edit?id=${u.user_id}">
                                            Sửa
                                        </a>
                                        <a class="btn btn-sm btn-outline-danger"
                                           href="${pageContext.request.contextPath}/admin/user/delete?id=${u.user_id}"
                                           onclick="return confirm('Xóa user này?')">
                                            Xóa
                                        </a>
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

</div>

<!-- Bootstrap 5.3.8 Bundle (gồm Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/js/bootstrap.bundle.min.js"></script>
<!-- (Tuỳ chọn) Bootstrap Icons -->
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css">
</body>
</html>
