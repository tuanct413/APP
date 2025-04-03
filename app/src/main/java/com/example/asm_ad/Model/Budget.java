package com.example.asm_ad.Model;

import java.sql.Date;

public class Budget {
    private long id;
    private long userId;
    private double amount;
    private String category;
    private String date;


    // Constructor mặc định
    public Budget() {}

    // Constructor đầy đủ
    public Budget(long id, long userId, double amount, String category) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;

    }

    public Budget(long userId,double amount, String category) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
    }

    // Constructor mới cho BudgetAdapter
    public Budget(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
    public Budget(int id, String category, double amount) {
        this.id = id;
        this.category = category;
        this.amount = amount;


    }

    // Getter và Setter
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUserId() { return userId; }
    public void setUserId(long userId) { this.userId = userId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDate() {
        return date;
    }
    public  String getDate(String date) {
        this.date = date;
        return date;
    }
}