package com.techelevator;

public class Drink extends Product {
    public Drink(String name, double price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Glug Glug, Yum!");
    }
}
