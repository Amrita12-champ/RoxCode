package dao;

import entity.Admin;
import entity.Student;
import entity.Problem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDb {

    private static final String url = "jdbc:mysql://localhost:3306/RoxCode";
    private static final String user = "root";
    private static final String pass = "YOUR_MYSQL_PASSWORD";

    private Connection con = null;

    public StudentDb() {
        connection();
    }

    // ================= CONNECTION =================

    public void connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            con = DriverManager.getConnection(url, user, pass);

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Database connection error: " + e.getMessage(), e);
        }
    }

    // ================= STUDENT =================

    public int insert(Student a) {
        String query = "INSERT INTO register VALUES (?,?,?)";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, a.getName());
            prt.setString(2, a.getEmail());
            prt.setString(3, a.getPassword());

            return prt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(String email) {
        String query = "DELETE FROM register WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, email);

            return prt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(Student a) {
        String query = "UPDATE register SET name=?, Pass=? WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, a.getName());
            prt.setString(2, a.getPassword());
            prt.setString(3, a.getEmail());

            return prt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String search(String email) {
        String query = "SELECT Pass FROM register WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, email);

            ResultSet rs = prt.executeQuery();

            if (rs.next()) {
                return rs.getString("Pass");
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Check whether student email exists
    public boolean emailExists(String email) {
        String query = "SELECT email FROM register WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, email);

            ResultSet rs = prt.executeQuery();

            return rs.next();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Update student password
    public boolean updatePassword(String email, String newPassword) {
        String query = "UPDATE register SET Pass=? WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, newPassword);
            prt.setString(2, email);

            int rows = prt.executeUpdate();

            System.out.println("Student password update rows affected: " + rows);

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Student update error: " + e.getMessage());
            return false;
        }
    }

    // ================= ADMIN =================

    public int insert(Admin a) {
        String query = "INSERT INTO Admin_register VALUES (?,?,?)";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, a.getName());
            prt.setString(2, a.getEmail());
            prt.setString(3, a.getPassword());

            return prt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete(Admin a) {
        String query = "DELETE FROM Admin_register WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, a.getEmail());

            return prt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(Admin a) {
        String query = "UPDATE Admin_register SET name=?, Pass=? WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, a.getName());
            prt.setString(2, a.getPassword());
            prt.setString(3, a.getEmail());

            return prt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String SearchAdm(String email) {
        String query = "SELECT Pass FROM Admin_register WHERE email=?";

        try {
            PreparedStatement prt = con.prepareStatement(query);

            prt.setString(1, email);

            ResultSet rs = prt.executeQuery();

            if (rs.next()) {
                return rs.getString("Pass");
            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // ================= PROBLEM =================

    public List<Problem> getAllProblems() {
        List<Problem> problems = new ArrayList<>();

        String query = "SELECT * FROM problems";

        try {
            PreparedStatement pst = con.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                problems.add(new Problem(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("difficulty"),
                        rs.getString("category"),
                        rs.getString("acceptance_rate")
                ));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return problems;
    }
}