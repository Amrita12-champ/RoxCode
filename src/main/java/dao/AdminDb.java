package dao;

import entity.Admin;
import java.sql.*;

public class AdminDb {
    private static final String url = "jdbc:mysql://localhost:3306/RoxCode";
    private static final String user = "root";
    private static final String pass = "a34862@TKRS";
    private Connection con = null;

    public void connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insert(Admin a) {
        String query = "INSERT INTO admin_register VALUES (?, ?, ?)";
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
        String query = "DELETE FROM admin_register WHERE email = ?";
        try {
            PreparedStatement prt = con.prepareStatement(query);
            prt.setString(1, a.getEmail());
            return prt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(Admin a) {
        String query = "UPDATE admin_register SET name = ?, password = ? WHERE email = ?";
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

    public String searchAdm(String email) {
        String query = "SELECT * FROM admin_register WHERE email = ?";
        try {
            PreparedStatement prt = con.prepareStatement(query);
            prt.setString(1, email);
            ResultSet rs = prt.executeQuery();
            if (rs.next()) {
                return rs.getString(3); // Column 3 is password
            } else {
                return "Invalid";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT email FROM admin_register WHERE email = ?";
        try {
            PreparedStatement prt = con.prepareStatement(sql);
            prt.setString(1, email);
            ResultSet rs = prt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updatePassword(String email, String newPassword) {
        String query = "UPDATE Admin_register SET Pass = ? WHERE email = ?";
        try {
            PreparedStatement prt = con.prepareStatement(query);
            prt.setString(1, newPassword);
            prt.setString(2, email);
            int rows = prt.executeUpdate();
            System.out.println("Admin password update rows affected: " + rows);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Admin update error: " + e.getMessage());
            return false;
        }
    }

    public boolean validateAdmin(String email, String password) {
        String sql = "SELECT * FROM Admin_register WHERE email = ? AND Pass = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // returns true if email & Pass match
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}