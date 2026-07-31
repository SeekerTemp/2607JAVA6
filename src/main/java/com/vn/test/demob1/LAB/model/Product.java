package com.vn.test.demob1.LAB.model;

import java.time.LocalDate;

/**
 * LAB 7 - Bài 2: sản phẩm {id, name, price, date, categoryId}
 * categoryId là khóa ngoại tham chiếu đến Category.id
 */
public class Product {

    private String id;
    private String name;
    private double price;
    private LocalDate date;
    private String categoryId;

    public Product() {
    }

    public Product(String id, String name, double price, LocalDate date, String categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.date = date;
        this.categoryId = categoryId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Product{id='" + id + "', name='" + name + "', price=" + price
                + ", date=" + date + ", categoryId='" + categoryId + "'}";
    }
}
