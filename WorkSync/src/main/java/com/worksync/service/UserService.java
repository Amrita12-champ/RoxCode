package com.worksync.service;

import com.worksync.constants.AccountStatus;
import com.worksync.dao.UserDAO;
import com.worksync.model.User;
import com.worksync.util.PasswordUtil;

import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public User authenticate(String username, String password)
            throws SQLException {

        if (username == null || username.isBlank()
                || password == null || password.isBlank()) {
            return null;
        }

        User user = userDAO.findByUsername(username.trim());

        if (user == null) {
            return null;
        }

        if (user.getStatus() != AccountStatus.ACTIVE) {
            return null;
        }

        if (!PasswordUtil.verifyPassword(password, user.getPassword())) {
            return null;
        }

        return user;
    }
}

