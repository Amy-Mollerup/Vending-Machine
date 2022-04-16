package com.techelevator;

import java.math.BigDecimal;

public class Candy extends Product{
    public Candy(String name, BigDecimal price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Munch Munch, Yum!");
    }
}
