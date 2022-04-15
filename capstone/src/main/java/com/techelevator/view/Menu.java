package com.techelevator.view;

import com.techelevator.Product;
import com.techelevator.VendingMachine;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Scanner;

public class Menu {

	private PrintWriter out;
	private Scanner in;
	private BigDecimal balance = new BigDecimal("0");


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
			out.println("Current Money Provided: $" + String.format("%.2f", balance));
		}

		out.print(System.lineSeparator() + "Please choose an option >>> ");
		out.flush();
	}

	public void displayStock(Map<String, Product> availableProducts) {
		for(Map.Entry<String, Product> productKey : availableProducts.entrySet()) {
			Product product = productKey.getValue();
			if(product.getStock() > 0) {
				System.out.println(productKey.getKey() + " - " + product.getName() + " $" + String.format("%.2f", product.getPrice()) + " | " + product.getStock());
			} else {
				System.out.println(productKey.getKey() + " - " + product.getName() + " $" + String.format("%.2f", product.getPrice()) + " | SOLD OUT");
			}
		}
	}

	public String feedMoney(int amount) {
		// updates balance and displays it to the user.
		BigDecimal amt = new BigDecimal(String.valueOf(amount));
		balance = balance.add(amt);

		return switch (amount) {
			case 1, 2, 5, 10 -> "\nCurrent Money Provided: $" + String.format("%.2f", balance) + "\n\nFeed more or exit with 'X'\n";
			default -> "\nPlease enter 1, 2, 5, or 10.\n";
		};
	}

	public void transaction(Product product) {
		BigDecimal price = new BigDecimal(String.valueOf(product.getPrice()));
		balance = balance.subtract(price);

		if(balance.signum() > 0) {
			System.out.println(product.getName() + ": $" + product.getPrice() + "\n$" + balance+ " remaining \n");
			product.dispense();
		} else {
			balance = balance.add(price);
			System.out.println("Insufficient funds. $" + balance.abs() + " required.");
		}
	}

	public void finishTransaction() {
		int quarters = 0;
		int dimes = 0;
		int nickels = 0;

		BigDecimal quarterValue = new BigDecimal(".25");
		BigDecimal dimeValue = new BigDecimal(".10");
		BigDecimal nickelValue = new BigDecimal(".05");

		System.out.println("Please take your change \n");


		while(balance.compareTo(BigDecimal.ZERO) > 0) {
			if(balance.compareTo(quarterValue) >= 0) {
				balance = balance.subtract(quarterValue);
				quarters++;
			} else if (balance.compareTo(dimeValue) >= 0) {
				balance = balance.subtract(dimeValue);
				dimes++;
			} else if (balance.compareTo(nickelValue) >= 0) {
				balance = balance.subtract(nickelValue);
				nickels++;
			}
		}

		System.out.println("Your change is " + quarters + " quarter(s), " + dimes + " dime(s), and " + nickels + " nickel(s).");
		System.out.println("$" + balance + " balance remaining");
	}

}
