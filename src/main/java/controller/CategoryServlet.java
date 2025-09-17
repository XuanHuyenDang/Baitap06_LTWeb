package controller;

import java.io.IOException;
import java.util.List;

import dao.CategoryDAO;
import entity.Category;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet({"/category/list", "/category/add", "/category/edit", "/category/delete"})
public class CategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final CategoryService service = new CategoryServiceImpl();

    /* ============================
       Service layer inlined
       ============================ */
    public static interface CategoryService {
        List<Category> findAll();
        List<Category> findByUser(int userId);
        Category findById(int id);
        void save(Category category);
        void delete(int id);
    }

    public static class CategoryServiceImpl implements CategoryService {
        private final CategoryDAO dao = new CategoryDAO();

        @Override
        public List<Category> findAll() {
            return dao.findAll(); // có sẵn trong AbstractDAO
        }

        @Override
        public List<Category> findByUser(int userId) {
            return dao.findByUser(userId); // đặc thù của CategoryDAO
        }

        @Override
        public Category findById(int id) {
            return dao.find(id); // từ AbstractDAO
        }

        @Override
        public void save(Category category) {
            // Quy ước: cateId > 0 -> update, ngược lại -> save
            if (category.getCate_id() > 0) {
                dao.update(category);
            } else {
                dao.save(category);
            }
        }

        @Override
        public void delete(int id) {
            Category c = dao.find(id);
            if (c != null) {
                dao.delete(c);
            }
        }
    }

    /* ============================
       Helpers
       ============================ */
    private Integer parseIntOrNull(String s) {
        try {
            return (s == null || s.isEmpty()) ? null : Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        HttpSession ss = req.getSession(false);
        return ss != null && ss.getAttribute("user") != null;
    }

    /** User thường (role_id == 2) không được thêm/sửa/xóa */
    private boolean isRestricted(User u) {
        return u != null && u.getRole_id() == 2;
    }

    /* ============================
       GET
       ============================ */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("user");

        if (uri.endsWith("list")) {
            // User thường chỉ thấy category của mình
            List<Category> list = (user != null && user.getRole_id() == 2)
                    ? service.findByUser(user.getUser_id())
                    : service.findAll();
            req.setAttribute("categories", list);
            req.getRequestDispatcher("/views/admin/Categories.jsp").forward(req, resp);
            return;
        }

        // Các route add/edit/delete yêu cầu đăng nhập và không bị hạn chế
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (isRestricted(user)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này");
            return;
        }

        if (uri.endsWith("add")) {
            req.getRequestDispatcher("/views/admin/Categories.jsp").forward(req, resp);
            return;
        }

        if (uri.endsWith("edit")) {
            Integer id = parseIntOrNull(req.getParameter("id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai tham số id");
                return;
            }
            Category cate = service.findById(id);
            if (cate != null) {
                req.setAttribute("category", cate);
                req.getRequestDispatcher("/views/admin/Categories.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        if (uri.endsWith("delete")) {
            Integer id = parseIntOrNull(req.getParameter("id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai tham số id");
                return;
            }
            Category cate = service.findById(id);
            if (cate != null) {
                service.delete(id);
                resp.sendRedirect(req.getContextPath() + "/category/list");
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
            return;
        }

        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    /* ============================
       POST
       ============================ */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        User user = (User) req.getSession().getAttribute("user");

        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (isRestricted(user)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền thay đổi dữ liệu");
            return;
        }

        String name = req.getParameter("cate_name");

        if (uri.endsWith("add")) {
            Category cate = new Category();
            cate.setCate_name(name);
            cate.setUser(user);
            service.save(cate);

        } else if (uri.endsWith("edit")) {
            Integer id = parseIntOrNull(req.getParameter("cate_id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai cate_id");
                return;
            }
            Category cate = service.findById(id);
            if (cate != null) {
                cate.setCate_name(name);
                // Không đổi chủ sở hữu ở đây (nếu cần đổi, thêm setUser(...))
                service.save(cate);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/category/list");
    }
}