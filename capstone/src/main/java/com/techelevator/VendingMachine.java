package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class VendingMachine {

    public static Map<String, Product> catalogueItems() {
        File stockFile = new File("capstone/vendingmachine.csv");
        Map<String, Product> inventory = new HashMap<>();
        try (Scanner reader = new Scanner(stockFile)) {
            while (reader.hasNextLine()) {
                String productInfo = reader.nextLine();
                String[] infoSplit = productInfo.split("\\|");
                String productCode = infoSplit[0];
                String productName = infoSplit[1];
                double productPrice = Double.parseDouble(infoSplit[2]);
                String productType = infoSplit[3];

                switch (productType) {
                    case "Chip":
                        inventory.put(productCode, new Chip(productName, productPrice));
                        break;
                    case "Candy":
                        inventory.put(productCode, new Candy(productName, productPrice));
                        break;
                    case "Drink":
                        inventory.put(productCode, new Drink(productName, productPrice));
                        break;
                    case "Gum":
                        inventory.put(productCode, new Gum(productName, productPrice));
                        break;
                    default:
                        throw new Exception("Unknown product type");
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inventory;
    }
}
