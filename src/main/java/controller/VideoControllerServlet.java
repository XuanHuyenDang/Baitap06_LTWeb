package controller;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;

import dao.VideoDAO;
import entity.User;
import entity.Video;


@WebServlet({"/video/list", "/video/add", "/video/edit", "/video/delete", "/video/search"})
public class VideoControllerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final VideoService service = new VideoServiceImpl();

    /* ============================
       Service layer inlined
       ============================ */
    public static interface VideoService {
        List<Video> findAll();
        Video findById(int id);
        List<Video> searchByTitle(String keyword);
        void save(Video video);
        void delete(int id);
    }

    public static class VideoServiceImpl implements VideoService {
        private final VideoDAO dao = new VideoDAO();

        @Override
        public List<Video> findAll() {
            return dao.findAll();
        }

        @Override
        public Video findById(int id) {
            return dao.find(id);
        }

        @Override
        public List<Video> searchByTitle(String keyword) {
            return dao.search("title", keyword);
        }

        @Override
        public void save(Video video) {
            if (video.getVideoId() > 0) {
                dao.update(video);
            } else {
                dao.save(video);
            }
        }

        @Override
        public void delete(int id) {
            Video v = dao.find(id);
            if (v != null) dao.delete(v);
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

    /** Vai trò bị hạn chế theo code gốc: roleId == 2 thì không được thay đổi dữ liệu */
    private boolean isRestricted(User u) {
        return u != null && u.getRole_id() == 2;
    }

    private boolean isLoggedIn(HttpServletRequest req) {
        return req.getSession(false) != null && req.getSession(false).getAttribute("user") != null;
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
            List<Video> list = service.findAll();
            req.setAttribute("videos", list);
            req.getRequestDispatcher("/views/admin/Videos.jsp").forward(req, resp);
            return;
        }

        if (uri.endsWith("search")) {
            String keyword = req.getParameter("keyword");
            List<Video> list = (keyword == null || keyword.isBlank())
                    ? service.findAll()
                    : service.searchByTitle(keyword);
            req.setAttribute("videos", list);
            req.getRequestDispatcher("/views/admin/Videos.jsp").forward(req, resp);
            return;
        }

        // Các route cần quyền
        if (!isLoggedIn(req)) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }
        if (isRestricted(user)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền");
            return;
        }

        if (uri.endsWith("add")) {
            req.getRequestDispatcher("/views/admin/Videos.jsp").forward(req, resp);
            return;
        }

        if (uri.endsWith("edit")) {
            Integer id = parseIntOrNull(req.getParameter("id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai tham số id");
                return;
            }
            Video v = service.findById(id);
            if (v != null) {
                req.setAttribute("video", v);
                req.getRequestDispatcher("/views/admin/Videos.jsp").forward(req, resp);
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
            Video v = service.findById(id);
            if (v != null) {
                service.delete(id);
                resp.sendRedirect(req.getContextPath() + "/video/list");
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

        String title  = req.getParameter("title");
        String desc   = req.getParameter("description");
        String url    = req.getParameter("url");
        String poster = req.getParameter("poster");
        boolean active = req.getParameter("active") != null;

        if (uri.endsWith("add")) {
            Video v = new Video();
            v.setTitle(title);
            v.setDescription(desc);
            v.setUrl(url);
            v.setPoster(poster);
            v.setViews(0);
            v.setActive(active);
            service.save(v);

        } else if (uri.endsWith("edit")) {
            Integer id = parseIntOrNull(req.getParameter("video_id"));
            if (id == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu hoặc sai video_id");
                return;
            }
            Video v = service.findById(id);
            if (v != null) {
                v.setTitle(title);
                v.setDescription(desc);
                v.setUrl(url);
                v.setPoster(poster);
                v.setActive(active);
                service.save(v);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/video/list");
    }
}
