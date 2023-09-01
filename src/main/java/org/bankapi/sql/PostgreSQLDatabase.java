package org.bankapi.sql;

import java.sql.*;
import java.util.ArrayList;

public class PostgreSQLDatabase {
    private Connection conn;

    public PostgreSQLDatabase(String url, String user, String password) throws SQLException {
        this.conn = DriverManager.getConnection(url, user, password);
        this.conn.setAutoCommit(false);
    }

    public boolean checkBalance(int userId, double amount) throws SQLException {
        return !(getBalance(userId) < amount);
    }

    public double getBalance(int userId) throws SQLException {
        String sql = "SELECT balance FROM users WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                } else {
                    throw new SQLException("User not found");
                }
            }
        }
    }

    public void putMoney(int userId, double amount) throws SQLException {
        String sql = "UPDATE users SET balance = balance + ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                conn.rollback();
                throw new SQLException("User not found");
            }
        }
    }

    public void takeMoney(int userId, double amount) throws SQLException {
        String sql = "UPDATE users SET balance = balance - ? WHERE id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, userId);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                conn.rollback();
                throw new SQLException("User not found");
            }
        }
    }

    public ArrayList<OperationEntity> getOperationListWithoutDate(int userId) throws SQLException {
        System.out.println("Get operation list without date.");

        String sql = "SELECT user_id, operation_type_id, amount, date FROM operation_list WHERE user_id = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<OperationEntity> operations = new ArrayList<>();
                while (rs.next())
                {
                    operations.add(new OperationEntity(rs.getString("date"), rs.getInt("operation_type_id"), rs.getDouble("amount")));
                }
                return operations;
            }
        }
    }

    public ArrayList<OperationEntity> getOperationListWithDate(int userId, Timestamp startDate, Timestamp endDate) throws SQLException {
        System.out.println("Get operation list with date.");

        String sql = "SELECT user_id, operation_type_id, amount, date FROM operation_list WHERE user_id = ? AND date >= ? AND date <= ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setTimestamp(2, startDate);
            pstmt.setTimestamp(3, endDate);

            try (ResultSet rs = pstmt.executeQuery()) {
                ArrayList<OperationEntity> operations = new ArrayList<>();
                while (rs.next())
                {
                    operations.add(new OperationEntity(rs.getString("date"), rs.getInt("operation_type_id"), rs.getDouble("amount")));
                }
                return operations;
            }
        }
    }

    public void markOperation(int userId, int type, double amount) throws SQLException {
        String sql = "INSERT INTO operation_list (user_id, operation_type_id, amount) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, type);
            pstmt.setDouble(3, amount);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                conn.rollback();
                throw new SQLException("No update!");
            } else {
                conn.commit();
            }
        }
    }
}