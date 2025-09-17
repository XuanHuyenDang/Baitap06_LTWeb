package filter;

import entity.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class AuthFilter implements Filter {

    // Các đường dẫn công khai (không yêu cầu đăng nhập)
    private static final Set<String> PUBLIC_PATHS = Set.of(
            "/", "/login", "/login.jsp", "/logout", "/register",
            "/video/list", "/video/search"
    );

    // Thư mục static (css/js/img/…)
    private static final String[] STATIC_PREFIXES = {
            "/assets/", "/static/", "/css/", "/js/", "/images/", "/img/", "/vendor/", "/webjars/"
    };

    private boolean isPublic(HttpServletRequest req) {
        String ctx = req.getContextPath();
        String path = req.getRequestURI().substring(ctx.length()); // path không kèm context
        if (path.isEmpty()) path = "/";

        if (PUBLIC_PATHS.contains(path)) return true;
        for (String pre : STATIC_PREFIXES) {
            if (path.startsWith(pre)) return true;
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req  = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String ctx  = req.getContextPath();
        String path = req.getRequestURI();                  // có kèm context
        String noCtxPath = path.substring(ctx.length());    // bỏ context cho dễ so khớp

        // Bỏ qua các route public/static
        if (isPublic(req)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User u = (session != null) ? (User) session.getAttribute("user") : null;

        // Nếu chưa đăng nhập mà truy cập khu vực cần quyền -> chuyển về login
        if (u == null && (
                noCtxPath.startsWith("/admin")
             || noCtxPath.startsWith("/manager")
             || noCtxPath.startsWith("/user"))) {
            res.sendRedirect(ctx + "/login.jsp");
            return;
        }

        // Đã đăng nhập -> kiểm tra role theo entity.User (role_id)
        if (u != null) {
            int role = u.getRole_id();  // <<< dùng getRole_id() đúng theo entity

            // Chỉ Admin (role_id = 3) mới vào /admin/**
            if (noCtxPath.startsWith("/admin") && role != 3) {
                res.sendRedirect(ctx + "/login.jsp");
                return;
            }

            // Chỉ Manager (role_id = 2) mới vào /manager/**
            // (Nếu muốn Admin cũng vào được, sửa điều kiện thành: role != 2 && role != 3)
            if (noCtxPath.startsWith("/manager") && role != 2) {
                res.sendRedirect(ctx + "/login.jsp");
                return;
            }

            // Trang quản lý người dùng /user/** -> chỉ Admin (3)
            if (noCtxPath.startsWith("/user") && role != 3) {
                res.sendRedirect(ctx + "/login.jsp");
                return;
            }
        }

        chain.doFilter(request, response);
    }
}
