package dao;

import entity.Admin;
import entity.Problem;
import entity.Student;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDb {
    private static final String url = "jdbc:mysql://localhost:3306/RoxCode";
    private static final String user = "root";
    private static final String pass = "a34862@TKRS";
    private Connection con = null;

    public void connection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public List<Problem> getAllProblems() {
        List<Problem> list = new ArrayList<>();
        String sql = "SELECT * FROM problems";
        try {
            if (con == null || con.isClosed()) {
                connection();
            }
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String title = rs.getString("title");
                String category = rs.getString("category");
                String difficulty = rs.getString("difficulty");
                String acceptanceRate = rs.getString("acceptanceRate");

                // Pass all 5 arguments directly:
                Problem p = new Problem(id, title, category, difficulty, acceptanceRate);
                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insert(Student a) {
        String query = "insert into register values (?,?,?)";
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
        String query = "delete from register where email=?";
        try {
            PreparedStatement prt = con.prepareStatement(query);
            prt.setString(1, email);
            return prt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(Student a) {
        String query = "update register set name=?, Pass=? where email=?";
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
        String sql = "select * from register where email=?";
        try {
            PreparedStatement prt = con.prepareStatement(sql);
            prt.setString(1, email);
            ResultSet rs = prt.executeQuery();
            if (rs.next()) {
                return rs.getString(2);
            } else {
                return "Invalid credential";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Admin Database Methods
    public int insert(Admin a) {
        String query = "insert into Admin_register values (?,?,?)";
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
        String query = "delete from Admin_register where email=?";
        try {
            PreparedStatement prt = con.prepareStatement(query);
            prt.setString(1, a.getEmail());
            return prt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int update(Admin a) {
        String query = "update Admin_register set name=?, Pass=? where email=?";
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
        String query = "select * from Admin_register where email=?";
        try {
            PreparedStatement prt = con.prepareStatement(query);
            prt.setString(1, email);
            ResultSet rs = prt.executeQuery();
            if (rs.next()) {
                return rs.getString(2);
            } else {
                return "Invalid";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean emailExists(String email) {
        String sql = "SELECT email FROM register WHERE email = ?";
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
        String query = "UPDATE register SET Pass = ? WHERE email = ?";
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

    public boolean validateStudent(String email, String password) {
        String sql = "SELECT * FROM register WHERE email = ? AND Pass = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}