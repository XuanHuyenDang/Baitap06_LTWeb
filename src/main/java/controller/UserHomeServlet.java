package controller;

import dao.CategoryDAO;
import entity.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/user/home")
public class UserHomeServlet extends HttpServlet {
    private CategoryDAO dao = new CategoryDAO();

    public UserHomeServlet() {
        super(); // constructor mặc định
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) 
            throws ServletException, IOException {
        List<Category> list = dao.findAll();
        req.setAttribute("categories", list);
        req.getRequestDispatcher("/views/web/user-home.jsp").forward(req, resp);
    }
}
