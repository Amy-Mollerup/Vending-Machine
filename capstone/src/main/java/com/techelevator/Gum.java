package com.techelevator;

public class Gum extends Product {
    public Gum(String name, double price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Chew Chew, Yum!");
    }
}
