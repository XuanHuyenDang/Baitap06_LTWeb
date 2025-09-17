package controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import dao.UserDAO;
import entity.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@MultipartConfig
public class ProfileServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserDAO userDAO = new UserDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("user_id"));
        String fullname = request.getParameter("fullname");
        String phone = request.getParameter("phone");

        // Lấy file upload
        Part filePart = request.getPart("image");
        String fileName = null;

        if (filePart != null && filePart.getSize() > 0) {
            fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
            String uploadPath = getServletContext().getRealPath("") + File.separator + "uploads";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) uploadDir.mkdir();
            filePart.write(uploadPath + File.separator + fileName);
        }

        // Load user từ DB
        User user = userDAO.find(userId);
        if (user != null) {
            user.setFullname(fullname);
            user.setPhone(phone);
            if (fileName != null) {
                user.setImage(fileName); // chỉ lưu tên file
            }
            userDAO.update(user);

            // Cập nhật lại session
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
        }

        // Forward về lại trang profile
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/profile.jsp").forward(request, response);
    }
}
