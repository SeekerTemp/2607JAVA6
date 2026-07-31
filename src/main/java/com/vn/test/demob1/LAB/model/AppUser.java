package com.vn.test.demob1.LAB.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * LAB 7 - Bài 3: người sử dụng {username, password, fullname, enabled, role}
 * role thuộc {"USER", "ADMIN"}
 */
@Entity
@Table(name = "app_user")
public class AppUser {

    @Id
    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String fullname;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private String role;

    public AppUser() {
    }

    public AppUser(String username, String password, String fullname, boolean enabled, String role) {
        this.username = username;
        this.password = password;
        this.fullname = fullname;
        this.enabled = enabled;
        this.role = role;
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

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "AppUser{username='" + username + "', fullname='" + fullname
                + "', enabled=" + enabled + ", role='" + role + "'}";
    }
}
