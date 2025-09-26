package com.webbank.dao;

import com.webbank.model.Account;
import com.webbank.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/webbank";
    private static final String USER = "root";
    private static final String PASSWORD = "123456";

    public Account getAccount(String accountNumber) {
        Account account = null;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "select a.account_Number,a.account_balance,u.user_number " +
                    "from account a join user u on a.user_id=u.user_id " +
                    "where a.account_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            rs = stmt.executeQuery();
            while (rs.next()) {
                account = new Account(rs.getString("account_number"),
                        rs.getDouble("account_balance"),
                        rs.getString("user_number"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);
        }
        return account;

    }

    public List<Account> getAllAccounts(String userNumber) {
        List<Account> allAccounts = new ArrayList<>();
        UserDAO userDAO = new UserDAO();
        int userId = userDAO.getUserId(userNumber);
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "select a.account_number,a.account_balance,u.user_number " +
                    "from account a join user u on a.user_id=u.user_id where a.user_id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            rs = stmt.executeQuery();
            while (rs.next()) {
                Account account = new Account(rs.getString("account_number"),
                        rs.getDouble("account_balance"),
                        rs.getString("user_number"));
                allAccounts.add(account);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);
        }
//        for(Account account: allAccounts){
//            System.out.println(account.getAccountNumber()+account.getAccountBalance()+account.getAccountBalance());
//        }

        return allAccounts;

    }

    public boolean addAccount(Account newAccount) {
        Account account = newAccount;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        UserDAO userDAO = new UserDAO();
        int userId = userDAO.getUserId(newAccount.getUserNumber());
        int row = 0;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "insert into account(account_number,account_balance,user_id) values(?,?,?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, account.getAccountNumber());
            stmt.setDouble(2, account.getAccountBalance());
            stmt.setInt(3, userId);
            row = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);
        }
        return row > 0;

    }

    public boolean deleteAccount(String accountNumber) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int row = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "delete from account where account_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, accountNumber);
            row = stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);
        }
        return row > 0;

    }

    public boolean updateAccount(Account newAccount) {
        Account account = newAccount;
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        int row = 0;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "update account set account_balance=? where account_number=?";
            stmt = conn.prepareStatement(sql);
            stmt.setDouble(1, newAccount.getAccountBalance());
            stmt.setString(2, newAccount.getAccountNumber());
            row = stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            finalOperate(rs, stmt, conn);
        }
        return row > 0;

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
