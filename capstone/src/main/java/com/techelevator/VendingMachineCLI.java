package com.techelevator;

import com.techelevator.view.Menu;
import com.techelevator.view.MenuException;

import java.util.Scanner;

public class VendingMachineCLI {
	// Main Menu variable setup
	private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
	private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
	private static final String MAIN_MENU_OPTION_EXIT = "Exit";
	private static final String[] MAIN_MENU_OPTIONS = { MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT };

	// Purchase Menu variable setup
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
				menu.displayItems();
			} else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
				// do purchase
				purchase();

			} else if (choice.equalsIgnoreCase(MAIN_MENU_OPTION_EXIT)) {
				// exit the application
				System.exit(0);
			} else if (choice.equalsIgnoreCase("Generate sales report")) {
				menu.generateSalesReport();
			}
		}
	}

	public void purchase() {
		while(true) {
			String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
			Scanner input = new Scanner(System.in);

			if(choice.equals(PURCHASE_MENU_OPTION_FEED_MONEY)) {
				// feed money menu
				menu.feedMoney(input);

			} else if(choice.equals(PURCHASE_MENU_OPTION_SELECT_PRODUCT)) {
				menu.displayForPurchase();

				try {
					menu.transaction(input);
				} catch (MenuException e) {
					System.out.println(e.getMessage() +"\n");
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
