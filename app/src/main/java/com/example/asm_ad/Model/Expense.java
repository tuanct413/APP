package com.example.asm_ad.Model;

public class Expense {
    private long expense_id;
    private long userId;
    private double amount;
    private String category;
    private String date;

    public  Expense(){}
    public Expense(long expense_id, long userId, double amount, String category, String date) {
        this.expense_id = expense_id;
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }
    public Expense(long userId, double amount, String category, String date) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public Expense(double amount, String category, String date) {
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public long getExpense_id() {
        return expense_id;
    }
    public void setExpense_id(long expense_id) {
        this.expense_id = expense_id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}