package com.vn.test.demob1.LAB.model;

/**
 * LAB 8: tài khoản đăng nhập của panel chat (dữ liệu để ngay trong code, không dùng CSDL).
 */
public class User {

    private String username;
    private String password;
    private String displayName;

    public User() {
    }

    public User(String username, String password, String displayName) {
        this.username = username;
        this.password = password;
        this.displayName = displayName;
    }

    public String getUsername() {
        return username;
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
