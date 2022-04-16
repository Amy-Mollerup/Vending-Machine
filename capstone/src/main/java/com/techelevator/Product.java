package com.techelevator;

import java.math.BigDecimal;

public abstract class Product {
    private String name;
    private BigDecimal price;
    private int stock = 5;

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void dispense() {
        stock--;
    }


}
