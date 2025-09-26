package com.webbank.model;


public class Account {
    private String accountNumber;
    private double accountBalance;
    private String userNumber;

    public Account(String accountNumber, double accountBalance, String userNumber) {
        this.accountNumber = accountNumber;
        this.accountBalance = accountBalance;
        this.userNumber = userNumber;
    }

    public Account() {
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public boolean withdraw(double amount) {
        if (amount > accountBalance || amount < 0) {
            return false;
        }
        accountBalance -= amount;
        return true;
    }

    public boolean deposit(double amount) {
        if (amount < 0) {
            return false;
        }
        accountBalance += amount;
        return true;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }
}
