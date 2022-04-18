package com.techelevator;

import junit.framework.TestCase;
import org.junit.Assert;

public class VendingMachineCLITest extends TestCase {

    public void testTestRun() {
        run();

        String menuOutput = "1) Display Vending Machine Items\n" +
                "2) Purchase\n" +
                "3) Exit\n" +
                "\n" +
                "Please choose an option >>> ";

        Assert.assertEquals(menuOutput, run().toString());
    }

    public void testPurchase() {
    }

    public void testMain() {
    }
}