package com.example.dataproperti.Cloud;

import com.google.firebase.database.Exclude;

import java.io.Serializable;

public class UserInfo implements Serializable {

    @Exclude
    private String key;
    private String Username;
    private String Email;
    private String PhoneNumber;
    private String Role;

    public UserInfo() {
    }

    public UserInfo(String Username, String Email, String PhoneNumber, String Role) {
        this.Username = Username;
        this.Email = Email;
        this.PhoneNumber = PhoneNumber;
        this.Role = Role;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }

    public String getKey() { return key; }

    public void setKey(String key) { this.key = key; }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
