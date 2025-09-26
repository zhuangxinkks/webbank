package com.webbank.model;

public class User {
    private String userNumber;
    private String userPassword;
    private int userId;

    public User(String userNumber, String userPassword) {
        this.userNumber = userNumber;
        this.userPassword = userPassword;
    }

    public User() {

    }


    public String getUserNumber() {
        return userNumber;
    }

    public boolean setUserPassword(String newUserPassword) {
        this.userPassword = newUserPassword;
        return true;
    }

    public String getUserPassword() {
        return userPassword;
    }

}
