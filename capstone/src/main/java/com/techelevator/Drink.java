package com.techelevator;

import java.math.BigDecimal;

public class Drink extends Product {
    public Drink(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Glug Glug, Yum!");
    }
}
