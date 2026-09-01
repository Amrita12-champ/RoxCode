package controller;

import dao.StudentDb;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/UserDataController")
public class UserDataController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session != null && session.getAttribute("userEmail") != null) {
            String email = (String) session.getAttribute("userEmail");

            // Return user details as JSON
            String jsonResponse = "{\"email\":\"" + email + "\",\"status\":\"success\"}";
            response.getWriter().write(jsonResponse);
        } else {
            // Return unauthenticated status
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"status\":\"unauthorized\"}");
        }
    }
}