package com.techelevator;

import com.techelevator.view.Menu;

import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class VendingMachineCLI {

	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	private static final Map<String, Product> availableProducts = new TreeMap<>(VendingMachine.catalogueItems());

	private static final String PURCHASE_MENU_OPTION_FEED_MONEY = "Feed Money";
	private static final String PURCHASE_MENU_OPTION_SELECT_PRODUCT = "Select Product";
	private static final String PURCHASE_MENU_OPTION_FINISH = "Finish Transaction";
	private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH};


	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
				// display vending machine items
				menu.displayStock(availableProducts);
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				purchase();

			} else if (choice.equalsIgnoreCase(MAIN_MENU_OPTION_EXIT)) {
				// exit the application
				System.exit(0);
			}
		}
	}

	public void purchase() {
		while(true) {
			String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
			Scanner input = new Scanner(System.in);

			if(choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
				// feed money menu
				while(true) {
					System.out.print("Please enter a whole dollar amount, without the '$' (1, 2, 5, or 10) >>> ");
					if (input.hasNextInt()) {
						System.out.println(menu.feedMoney(input.nextInt()));
					} else if (input.nextLine().equalsIgnoreCase("x")){
						purchase();
					} else {
						System.out.println("Please enter a valid amount or 'X'");
					}
				}
			} else if(choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
				// show products and allow customer to purchase
				menu.displayStock(availableProducts);
				System.out.print("Please select an item: ");
				String selection = input.nextLine();

				if(!availableProducts.containsKey(selection)) {
					System.out.println("Please select a valid product code.");
					purchase();
				} else if(availableProducts.get(selection).getStock() == 0){
					System.out.println(selection + " is SOLD OUT, please select another.");
					purchase();
				} else {
					menu.transaction(availableProducts.get(selection));
					purchase();
				}

			} else if(choice.equals(PURCHASE_MENU_OPTION_FINISH)) {
				// exit the application
				menu.finishTransaction();
				run();
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
