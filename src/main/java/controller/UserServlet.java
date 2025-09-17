package controller;


import java.io.IOException;
import java.util.List;

import dao.UserDAO;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet({"/admin/user/list", "/admin/user/add", "/admin/user/edit", "/admin/user/delete"})
public class UserServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final UserService service = new UserServiceImpl();

    /* ============================
       Service layer inlined
       ============================ */
    public static interface UserService {
        User login(String username, String password);
        List<User> findAll();
        User findById(int id);
        User findByEmail(String email);
        void save(User user);
        void delete(int id);
    }

    public static class UserServiceImpl implements UserService {
        private final UserDAO dao = new UserDAO();

        @Override
        public User login(String username, String password) {
            return dao.login(username, password);
        }

        @Override
        public List<User> findAll() {
            return dao.findAll();
        }

        @Override
        public User findById(int id) {
            return dao.find(id);
        }

        @Override
        public User findByEmail(String email) {
            return dao.findByEmail(email);
        }

        @Override
        public void save(User user) {
            if (user.getUser_id() > 0) {
                dao.update(user);
            } else {
                dao.save(user);
            }
        }

        @Override
        public void delete(int id) {
            User u = dao.find(id);
            if (u != null) dao.delete(u);
        }
    }

    /* ============================
       Helpers
       ============================ */
    private boolean isAdmin(User u) {
        return u != null && u.getRole_id() == 3;
    }

    private Integer parseIntOrNull(String s) {
        try {
            return (s == null || s.isEmpty()) ? null : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /* ============================
       GET
       ============================ */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        User loginUser = (User) req.getSession().getAttribute("user");

        // Chưa đăng nhập -> về trang login (tùy route dự án)
        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        if (uri.endsWith("list")) {
            List<User> list = service.findAll();
            req.setAttribute("users", list);
            req.getRequestDispatcher("/views/admin/Users.jsp").forward(req, resp);

        } else if (uri.endsWith("add")) {
            if (!isAdmin(loginUser)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền");
                return;
            }
            req.getRequestDispatcher("/views/admin/Users.jsp").forward(req, resp);

        } else if (uri.endsWith("edit")) {
            if (!isAdmin(loginUser)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền");
                return;
            }
            Integer id = parseIntOrNull(req.getParameter("id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai tham số id");
                return;
            }
            User u = service.findById(id);
            if (u != null) {
                req.setAttribute("userObj", u);
                req.getRequestDispatcher("/views/admin/Users.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }

        } else if (uri.endsWith("delete")) {
            if (!isAdmin(loginUser)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền");
                return;
            }
            Integer id = parseIntOrNull(req.getParameter("id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai tham số id");
                return;
            }
            User u = service.findById(id);
            if (u != null) {
                service.delete(id);
                resp.sendRedirect(req.getContextPath() + "/admin/user/list");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /* ============================
       POST
       ============================ */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        User loginUser = (User) req.getSession().getAttribute("user");

        if (loginUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (!isAdmin(loginUser)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thay đổi dữ liệu");
            return;
        }

        String username = req.getParameter("username");
        String fullName = req.getParameter("fullname");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String image = req.getParameter("image");
        Integer roleId = parseIntOrNull(req.getParameter("role_id"));

        if (roleId == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai role_id");
            return;
        }

        if (uri.endsWith("add")) {
            User u = new User();
            u.setUsername(username);
            u.setPassword(password);
            u.setRole_id(roleId);
            u.setFullname(fullName);
            u.setPhone(phone);
            u.setImage(image);
            service.save(u);

        } else if (uri.endsWith("edit")) {
            Integer id = parseIntOrNull(req.getParameter("user_id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai user_id");
                return;
            }
            User u = service.findById(id);
            if (u != null) {
                u.setUsername(username);
                u.setPassword(password);
                u.setRole_id(roleId);
                u.setFullname(fullName);
                u.setPhone(phone);
                u.setImage(image);
                service.save(u);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/admin/user/list");
    }
}
