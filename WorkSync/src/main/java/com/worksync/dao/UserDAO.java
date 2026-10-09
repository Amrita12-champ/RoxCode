package com.worksync.dao;

import com.worksync.constants.AccountStatus;
import com.worksync.constants.Role;
import com.worksync.model.User;
import com.worksync.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    public User findByUsername(String username) throws SQLException {

        String sql = "SELECT user_id, username, email, password, role, status, created_at " +
                "FROM users WHERE username = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    User user = new User();

                    user.setUserId(resultSet.getInt("user_id"));
                    user.setUsername(resultSet.getString("username"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRole(Role.valueOf(resultSet.getString("role")));
                    user.setStatus(AccountStatus.valueOf(resultSet.getString("status")));

                    return user;
                }
            }
        }

        return null;
    }
}
