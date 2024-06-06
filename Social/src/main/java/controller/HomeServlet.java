package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dao.PostDAO;
import model.Post;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/HomeServlet")
public class HomeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public HomeServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login");
            return;
        }

        PostDAO postDAO = new PostDAO();
        ArrayList<Post> posts = new ArrayList<>();
        try {
            posts = postDAO.getAllPost();
        } catch (Exception e) {
            e.printStackTrace();
        }
        request.setAttribute("posts", posts);

        request.getRequestDispatcher("WEB-INF/home.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user_id") == null) {
            response.sendRedirect("login");
            return;
        }

        String post = request.getParameter("post").trim();

        if(!post.equals("")) {
            try {
                PostDAO postDAO = new PostDAO();
                postDAO.insertPost((int) session.getAttribute("user_id"), post);
                response.sendRedirect("home");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}