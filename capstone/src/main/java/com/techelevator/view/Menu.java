package com.techelevator.view;

import com.techelevator.Product;
import com.techelevator.VendingMachine;
import com.techelevator.VendingMachineCLI;

import java.io.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;
	private BigDecimal balance = new BigDecimal("0.00");


	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}

	public Object getChoiceFromOptions(Object[] options) {
		Object choice = null;
		while (choice == null) {
			displayMenuOptions(options);
			choice = getChoiceFromUserInput(options);
		}
		return choice;
	}

	private Object getChoiceFromUserInput(Object[] options) {
		Object choice = null;
		String userInput = in.nextLine();
		try {
			int selectedOption = Integer.valueOf(userInput);
			if (selectedOption > 0 && selectedOption <= options.length) {
				choice = options[selectedOption - 1];
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	private void displayMenuOptions(Object[] options) {
		out.println();
		for (int i = 0; i < options.length; i++) {
			int optionNum = i + 1;
			out.println(optionNum + ") " + options[i]);
		}
		if(balance.signum() > 0) {
			out.println();
			out.println("Current Money Provided: $" + balance);
		}

		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public void displayStock(Map<String, Product> availableProducts) {
		for(Map.Entry<String, Product> productKey : availableProducts.entrySet()) {
			Product product = productKey.getValue();
			if(product.getStock() > 0) {
				out.println(productKey.getKey() + " - " + product.getName() + " $" + String.format("%.2f", product.getPrice()) + " | " + product.getStock());
			} else {
				out.println(productKey.getKey() + " - " + product.getName() + " $" + String.format("%.2f", product.getPrice()) + " | SOLD OUT");
			}
		}
		out.print("\nPlease select an item: ");
		out.flush();
	}

	public void feedMoney(Scanner input) {
		// updates balance and displays it to the user.


		while(true) {
			System.out.print("Please enter a whole dollar amount, without the '$' (1, 2, 5, or 10) >>> ");
			String nextSelection = input.nextLine();

			switch (nextSelection) {
				case "1", "2", "5", "10":
					// Collect old balance then pass new amount added to BigDecimal
					BigDecimal preCalculationBalance = balance;
					BigDecimal fedAmount = new BigDecimal(nextSelection);

					// Add new amount to balance then log the transaction
					balance = balance.add(fedAmount);
					auditLog("FEED MONEY:", preCalculationBalance, balance);

					// Prompt next action and restart loop
					System.out.println("\nCurrent Money Provided: $" + balance + "\n\nFeed more or exit with 'X'\n");
					feedMoney(input);

				case "x", "X":
					// X breaks the loop and leaves the function
					break;

				default:
					// Invalid amount entered, tells user and restarts function
					System.out.println("\nPlease enter a valid amount or 'X'.\n");
					feedMoney(input);
			}
			break;
		}
	}

	public void transaction(Product product) {
		// Collects balance before subtracting product price
		BigDecimal preCalculationBalance = balance;
		balance = balance.subtract(product.getPrice());

		// Checks to see if funds are sufficient and executes accordingly
		if(balance.signum() > 0) { // If funds are sufficient
			out.println("\n" + product.getName() + ": $" + product.getPrice() + "\n$" + balance+ " remaining");
			out.flush();
			product.dispense();
		} else {
			balance = balance.add(product.getPrice());
			out.println("Insufficient funds. $" + balance.abs() + " required.");
			out.flush();
		}

		// Clears the stream and audits the sale
		auditLog(product.getName(), preCalculationBalance, balance);

	}

	public void finishTransaction() {
		int quarters = 0;
		int dimes = 0;
		int nickels = 0;

		BigDecimal beforeValue = balance;
		final BigDecimal QUARTER_VALUE = new BigDecimal(".25");
		final BigDecimal DIME_VALUE = new BigDecimal(".10");
		final BigDecimal NICKEL_VALUE = new BigDecimal(".05");

		out.println("Please take your change \n");


		while(balance.compareTo(BigDecimal.ZERO) > 0) {
			if(balance.compareTo(QUARTER_VALUE) >= 0) {
				balance = balance.subtract(QUARTER_VALUE);
				quarters++;
			} else if (balance.compareTo(DIME_VALUE) >= 0) {
				balance = balance.subtract(DIME_VALUE);
				dimes++;
			} else if (balance.compareTo(NICKEL_VALUE) >= 0) {
				balance = balance.subtract(NICKEL_VALUE);
				nickels++;
			}
		}

		auditLog("GIVE CHANGE:", beforeValue, balance);

		out.println("Your change is " + quarters + " quarter(s), " + dimes + " dime(s), and " + nickels + " nickel(s).");
		out.println("$" + balance + " balance remaining");
		out.flush();
	}

	public void auditLog(String action, BigDecimal startingAmount, BigDecimal endingAmount) {
				File log = new File("capstone/Log.txt");
				try(PrintWriter writer = new PrintWriter(new FileOutputStream(log), true)) {
					writer.write(">" + new Date() +
							" " + action +
							" $" + startingAmount +
							" $" + endingAmount);
					writer.flush();
				} catch (Exception e) {
					e.getMessage();
				}
	}

}
