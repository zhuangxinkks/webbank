package com.webbank.service;

import com.webbank.dao.AccountDAO;
import com.webbank.dao.UserDAO;
import com.webbank.model.Account;
import com.webbank.model.User;

import java.util.List;

public class BankService {
    UserDAO userDAO = new UserDAO();
    AccountDAO accountDAO = new AccountDAO();

    public boolean registerUser(String userNumber, String userPassword) {

        return userDAO.addUser(new User(userNumber, userPassword));
    }

    public boolean loginUser(String userNumber, String userPassword) {
        User user = userDAO.getUser(userNumber);
        if (user == null) {
            return false;
        }
        return validateUserPassword(user, userPassword);
    }

    public boolean changeUserPassword(String userNumber, String oldUserPassword, String newUserPassword) {
        User user = userDAO.getUser(userNumber);
        if (user == null) {
            return false;
        }
        if (!validateUserPassword(user, oldUserPassword)) {
            return false;
        }
        return userDAO.updateUserPassword(new User(userNumber, newUserPassword));
    }

    public boolean deleteUser(String userNumber) {
        User user = userDAO.getUser(userNumber);
        if (user == null) {
            return false;
        }
        return userDAO.deleteUser(userNumber);
    }

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public User getUser(String userNumber) {
        return userDAO.getUser(userNumber);
    }

    public boolean createAccount(User user, String accountNumber, double accountBalance) {
        String userNumber = user.getUserNumber();
        if (user == null) {
            return false;
        }
        return accountDAO.addAccount(new Account(accountNumber, accountBalance, userNumber));
    }

    public boolean deposit(Account account, double amount) {
        account.deposit(amount);
        return accountDAO.updateAccount(account);
    }

    public boolean withdraw(Account account, double amount) {
        account.withdraw(amount);
        return accountDAO.updateAccount(account);
    }

    public boolean deleteAccount(String accountNumber) {
        return accountDAO.deleteAccount(accountNumber);
    }

    public Account getAccount(String accountNumber) {
        return accountDAO.getAccount(accountNumber);
    }

    public List<Account> getAllAccounts(String userNumber) {
        return accountDAO.getAllAccounts(userNumber);
    }

    private boolean validateUserPassword(User user, String userPassword) {
        return user.getUserPassword().equals(userPassword);
    }
}
