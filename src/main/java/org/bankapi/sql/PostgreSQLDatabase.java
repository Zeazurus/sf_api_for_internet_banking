package org.bankapi.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PostgreSQLDatabase {
    private final String url;
    private final String user;
    private final String password;

    public PostgreSQLDatabase(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public boolean checkBalance(int userId, double amount) throws SQLException {
        return !(getBalance(userId) < amount);
    }

    public double getBalance(int userId) throws SQLException {
        String sql = "SELECT balance FROM users WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double result = rs.getDouble("balance");
                    markOperation(userId, result, 1);
                    return result;
                } else {
                    throw new SQLException("User not found");
                }
            }
        }
    }

    public void putMoney(int userId, double amount) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);

            conn.setAutoCommit(false);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("User not found");
            } else {
                markOperation(userId, amount, 2);
                conn.commit();
            }
        }
    }

    public void takeMoney(int userId, double amount) throws SQLException {
        String sql = "UPDATE users SET balance = balance - ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);

            conn.setAutoCommit(false);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("User not found");
            } else {
                markOperation(userId, amount, 3);
                conn.commit();
            }
        }
    }

    private void markOperation(int userId, double amount, int type) throws SQLException {
        String sql = "INSERT INTO operation_list (user_id, operation_type_id, amount) VALUES (?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, type);
            pstmt.setDouble(3, amount);


            conn.setAutoCommit(false);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("No update!");
            } else {
                conn.commit();
            }
        }
    }
}