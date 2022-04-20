package com.techelevator.view;

import com.techelevator.*;

import java.io.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class Menu {

	private PrintWriter out;
	private Scanner in;
	private BigDecimal balance = new BigDecimal("0.00");
	private static final Map<String, Integer> salesLogger = new HashMap<>();
	private static final Map<String, Product> availableProducts = new TreeMap<>(catalogueItems());




	public Menu(InputStream input, OutputStream output) {
		this.out = new PrintWriter(output);
		this.in = new Scanner(input);
	}


	// ***** GENERAL MENU FUNCTIONS *****


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
			} else if (selectedOption == options.length + 1) {
				choice = "Generate sales report";
			}
		} catch (NumberFormatException e) {
			// eat the exception, an error message will be displayed below since choice will be null
		}
		if (choice == null) {
			out.println(System.lineSeparator() + "*** " + userInput + " is not a valid option ***" + System.lineSeparator());
		}
		return choice;
	}

	public static Map<String, Product> catalogueItems() {
		/* catalogueItems() collects information from vendingmachine.csv, processes it into Objects,
		then inputs the data into a map, where the Key is the item codes, and the value is the Objects of the items.
		 */

		String path = new File("").getAbsolutePath();

		File stockFile = new File(path + "/vendingmachine.csv");
		Map<String, Product> inventory = new TreeMap<>();
		try (Scanner reader = new Scanner(stockFile)) {
			while (reader.hasNextLine()) {
				// Splits the line on '|' delimiters then assigns each part to variables
				String productInfo = reader.nextLine();
				String[] infoSplit = productInfo.split("\\|");
				String productCode = infoSplit[0];
				String productName = infoSplit[1];
				BigDecimal productPrice = new BigDecimal(infoSplit[2]);
				String productType = infoSplit[3];

				// Builds the names in salesLogger
				salesLogger.put(productName, 0);

				// Processes items into objects, will throw exception if an incorrect type is found.
				switch (productType) {
					case "Chip" -> inventory.put(productCode, new Chip(productName, productPrice));
					case "Candy" -> inventory.put(productCode, new Candy(productName, productPrice));
					case "Drink" -> inventory.put(productCode, new Drink(productName, productPrice));
					case "Gum" -> inventory.put(productCode, new Gum(productName, productPrice));
					default -> throw new Exception("Unknown product type");
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return inventory;
	}

	private void displayMenuOptions(Object[] options) {
		/* displayMenuOptions() displays numbered options from an array for the user to choose from */

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



	// ***** MAIN MENU FUNCTION *****


	public void displayItems() {
		/* displayItems() displays the item codes, names, and prices */
		for(Map.Entry<String, Product> productKey : availableProducts.entrySet()) {
			Product product = productKey.getValue();
			if(product.getStock() > 0) {
				out.println(productKey.getKey() + " - " + product.getName() + " $" + String.format("%.2f", product.getPrice()) + " | " + product.getStock());
			} else {
				out.println(productKey.getKey() + " - " + product.getName() + " $" + String.format("%.2f", product.getPrice()) + " | SOLD OUT");
			}
		}
		out.flush();
	}



	// ***** PURCHASE MENU FUNCTIONS *****


	public void feedMoney(Scanner input) {
		/* feedMoney() allows the user to input money in $1, $2, $5, or $10 increments.
		This function is implemented so that the user can enter money as many times as they would like before exiting.
		These deposits are also entered into the audit log.
		 */


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

	public void displayForPurchase() {
		displayItems();
		out.print("\nPlease select an item: ");
		out.flush();
	}

	public void selectProduct(Scanner input) throws MenuException {
		/* selectProduct() executes the sale, checking for exceptions,
		subtracts balance accordingly, and logs the transaction
		 */

		// Collects balance before subtracting product price
		String selection = input.nextLine().toUpperCase();
		Product product = availableProducts.get(selection);
		BigDecimal proposedBalance;
		String productName;



		// Checks if product exists

		if (!availableProducts.containsKey(selection)) {
			throw new MenuException("*** " + selection + " is not a valid option ***");
		}


		// Checks if product is in stock
		productName = product.getName();
		if (availableProducts.get(selection).getStock() == 0) {
			throw new MenuException(productName + " is SOLD OUT, please select another.");
		}

		// Checks if funds are sufficient
		proposedBalance = balance.subtract(product.getPrice());
		if (proposedBalance.signum() < 0) {
			throw new MenuException("Insufficient funds. $" + proposedBalance.abs() + " more required.");
		}

		// Execute the transaction
		BigDecimal preCalculationBalance = balance;
		balance = balance.subtract(product.getPrice());
		out.println("\n" + productName + ": $" + product.getPrice() +
				"\n$" + balance + " remaining");
		out.flush();
		product.dispense();

		// Log to audit log and readies information for sales log
		auditLog(product.getName(),  preCalculationBalance, balance);
		// Increments amount of product sold
		salesLogger.put(productName, salesLogger.get(productName) + 1);


	}

	public void finishTransaction() {
		/* finishTransaction() returns the users change, then updates the audit log.
		 */
		int quarters = 0;
		int dimes = 0;
		int nickels = 0;

		BigDecimal beforeValue = balance;
		final BigDecimal QUARTER_VALUE = new BigDecimal(".25");
		final BigDecimal DIME_VALUE = new BigDecimal(".10");
		final BigDecimal NICKEL_VALUE = new BigDecimal(".05");

		out.println("Please take your change \n");


		// Generates appropriate change amounts
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


	// ***** LOGGING FUNCTIONS *****

	public void auditLog(String action, BigDecimal startingAmount, BigDecimal endingAmount) {
	/* auditLog() posts all actions taken in the machine to a log.txt file.
	Includes the time and date, action taken, and the starting and ending balance of each action.
	 */
		String path = new File("").getAbsolutePath();

		File logFilePath = new File(path + "/src/main/resources/log.txt");

		try(PrintWriter writer = new PrintWriter(new FileOutputStream(logFilePath, true))) {
			String pattern = "MM/dd/YYYY hh:mm:ss a";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			String date = sdf.format(new Date());

			writer.write(">" + date +
					" " + action +
					" $" + startingAmount +
					" $" + endingAmount + "\n");
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public void generateSalesReport() {
	/* generateSalesReport() generates a sales report when you press an option beyond the scope of visible menu options.
	For the default implementation this is option 4.
	 */
		String pattern = "MM-dd-YYYY_HHmmss";
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String date = sdf.format(new Date());
		String path = new File("").getAbsolutePath();
		File salesReport = new File(path + "/src/main/resources/sales_report_" + date + ".txt");
		int totalSales = 0;

		try(PrintWriter reportWriter = new PrintWriter(salesReport)) {
			for(Map.Entry<String, Product> item : availableProducts.entrySet()) {
				String productName = item.getValue().getName();
				int amountSold = salesLogger.get(productName);

				reportWriter.write(productName + "|" + amountSold + "\n");
				totalSales += amountSold;
			}
			reportWriter.write("\n________________________\nTOTAL SALES|" + totalSales);
		} catch (Exception e) {
			System.out.println(e);
		}

		System.out.println("*** SALES REPORT GENERATED ***");
	}

}
