package com.techelevator;

import java.math.BigDecimal;

public class Chip extends Product{
    public Chip(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Crunch Crunch, Yum!");
    }
}
