package controller;

import dao.StudentDb;
import entity.Problem;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet("/ProblemController")
public class ProblemController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        // Protect route: Check user session
        if (session != null && session.getAttribute("userEmail") != null) {
            StudentDb db = new StudentDb();
            List<Problem> list = db.getAllProblems();

            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < list.size(); i++) {
                Problem p = list.get(i);

                // Escape quotes and backslashes to prevent JSON syntax errors
                String title = escapeJson(p.getTitle());
                String category = escapeJson(p.getCategory());
                String difficulty = escapeJson(p.getDifficulty());
                String rate = escapeJson(p.getAcceptanceRate());

                json.append("{")
                        .append("\"id\":").append(p.getId()).append(",")
                        .append("\"title\":\"").append(title).append("\",")
                        .append("\"difficulty\":\"").append(difficulty).append("\",")
                        .append("\"category\":\"").append(category).append("\",")
                        .append("\"acceptanceRate\":\"").append(rate).append("\"")
                        .append("}");
                if (i < list.size() - 1) json.append(",");
            }
            json.append("]");

            response.getWriter().write(json.toString());
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}