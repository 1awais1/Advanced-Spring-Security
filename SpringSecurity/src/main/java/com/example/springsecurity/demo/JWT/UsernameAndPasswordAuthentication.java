package com.example.springsecurity.demo.JWT;

public class UsernameAndPasswordAuthentication {

    // Default constructor (no-argument constructor)
    public UsernameAndPasswordAuthentication() {
    }

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "UsernameAndPasswordAuthentication{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UsernameAndPasswordAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
