package com.techelevator;

public class Chip extends Product{
    public Chip(String name, double price) {
        super(name, price);
    }

    @Override
    public void dispense() {
        super.dispense();
        System.out.println("Crunch Crunch, Yum!");
    }
}
