package com.project.repository;

import com.project.config.DatabaseConfig;
import com.project.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserRepository {
    
    public User save(User user) throws SQLException {
        String sql = "INSERT INTO users (name, phone, email, password) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getPhone());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPassword());
            
            pstmt.executeUpdate();
            
            // Retrieve the auto-generated ID from MySQL
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
            
            return user;
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT id, name, phone, email, password FROM users WHERE email = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            
            try(ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()){
                    User user = new User(rs.getString("name"),
                                          rs.getString("phone"),
                                          rs.getString("email"),
                                          rs.getString("password"));
                    user.setId(rs.getInt("id"));
                    return user;
                }
                return null;
            }
        }    
    }

}
