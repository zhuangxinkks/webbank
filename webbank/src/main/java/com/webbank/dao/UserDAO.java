package com.webbank.dao;


import com.webbank.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private static final String URL = "jdbc:mysql://localhost:3306/webbank";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public User getUser(String UserNumber) {
        User user = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "select user_number,user_password from user where user_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, UserNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User temUser = new User(
                        rs.getString("user_number"),
                        rs.getString("user_password")
                );
                user = temUser;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);

        }
        return user;

    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "select * from user";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            while (rs.next()) {
                User user = new User(
                        rs.getString("user_number"),
                        rs.getString("user_password")
                );
                users.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);

        }
        return users;
    }

    public boolean addUser(User newUser) {
        User user = newUser;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int row = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "insert into user (user_number,user_password) values(?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUserNumber());
            stmt.setString(2, user.getUserPassword());
            row = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);

        }
        return row > 0;
    }

    public boolean updateUserPassword(User newUser) {
        User user = newUser;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int row = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "update user set user_password=? where user_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, user.getUserPassword());
            stmt.setString(2, user.getUserNumber());
            row = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);

        }
        return row > 0;
    }

    public boolean deleteUser(String userNumber) {
        User user = new User();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int row = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "delete from user where user_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, userNumber);
            row = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);
        }
        return row > 0;
    }

    public int getUserId(String UserNumber) {
        int userId = 0;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            String sql = "select user_id from user where user_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, UserNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
                userId = rs.getInt("user_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);
        }
        return userId;

    }


    private void finalOperate(ResultSet rs, PreparedStatement stmt, Connection conn) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (stmt != null) stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
