package com.techelevator;

public class Candy extends Product{
    public Candy(String name, double price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Munch Munch, Yum!");
    }
}
