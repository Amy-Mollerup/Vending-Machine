package com.techelevator;

import java.math.BigDecimal;

public class Gum extends Product {
    public Gum(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Chew Chew, Yum!");
    }
}
