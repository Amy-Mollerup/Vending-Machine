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
	private static final String[] PURCHASE_MENU_OPTIONS = { PURCHASE_MENU_OPTION_FEED_MONEY, PURCHASE_MENU_OPTION_SELECT_PRODUCT, PURCHASE_MENU_OPTION_FINISH };


	private Menu menu;

	public VendingMachineCLI(Menu menu) {
		this.menu = menu;
	}

	// Main Menu
	public void run() {
		while (true) {
			String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

			switch (choice) {
				case MAIN_MENU_OPTION_DISPLAY_ITEMS ->
						menu.displayItems();

				case MAIN_MENU_OPTION_PURCHASE ->
						purchase();

				case MAIN_MENU_OPTION_EXIT ->
						System.exit(0);

				// Hidden option 4
				case "Generate sales report" ->
						menu.generateSalesReport();
			}
		}
	}

	// Purchase Menu
	public void purchase() {
		while(true) {
			String choice = (String) menu.getChoiceFromOptions(PURCHASE_MENU_OPTIONS);
			Scanner input = new Scanner(System.in);


			switch (choice) {
				case PURCHASE_MENU_OPTION_FEED_MONEY ->
						menu.feedMoney(input);

				case PURCHASE_MENU_OPTION_SELECT_PRODUCT -> {
					menu.displayForPurchase();

					// Handles transaction and error cases
					try {
						menu.selectProduct(input);
					} catch (MenuException e) {
						System.out.println(e.getMessage() + "\n");
					}
				}

				case PURCHASE_MENU_OPTION_FINISH -> {
					// Returns change and goes back to main menu
					menu.finishTransaction();
					run();
				}
			}
		}
	}

	public static void main(String[] args) {
		Menu menu = new Menu(System.in, System.out);
		VendingMachineCLI cli = new VendingMachineCLI(menu);
		cli.run();
	}
}
