package com.example.asm_ad.Model;

public class User {
    private int id;
    private String email;
    private String password;
    private String lastname;
    private String firstname;

    // Constructor không có id (dùng khi tạo user mới)
    public User(String password, String lastname, String username, int id, String firstname) {
        this.password = password;
        this.email = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.id = id;
    }
    public User(String username, String password, String firstname, String lastname) {
        this.email = username;
        this.password = password;
        this.lastname = lastname;
        this.firstname = firstname;
    }
    public User(String email, String lastname, String firstname) {
        this.email = email;
        this.lastname = lastname;
        this.firstname = firstname;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }



}