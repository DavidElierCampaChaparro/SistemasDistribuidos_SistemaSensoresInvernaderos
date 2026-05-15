package com.greenhouse.auth.repo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepository {

    private static final String URL = "jdbc:mysql://localhost:3306/greenhouse_system_admins";
    private static final String USER = "root";
    private static final String PASS = "root";

    public boolean authenticate(String username, String password) {
        String sql = "SELECT 1 FROM administrator WHERE username = ? AND password = ? LIMIT 1";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}