package com.worksync;

import com.worksync.util.DBConnection;
import com.worksync.util.PasswordUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetupAdmin {
    public static void main(String[] args) {

        String username = "admin";
        String email = "admin@worksync.com";
        String password = "Admin@123";

        String sql = "INSERT INTO users (username, email, password, role, status) " +
                "VALUES (?, ?, ?, 'SUPER_ADMIN', 'ACTIVE')";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, PasswordUtil.hashPassword(password));

            int rows = statement.executeUpdate();

            if (rows > 0) {
                System.out.println("SUPER_ADMIN account created successfully!");
                System.out.println("Username: " + username);
                System.out.println("Password: " + password);
            }

        } catch (SQLException e) {
            System.out.println("Unable to create the admin account.");
            e.printStackTrace();
        }
    }
}

