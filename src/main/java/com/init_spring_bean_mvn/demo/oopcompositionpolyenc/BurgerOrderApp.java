package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Interactive Burger Ordering Application
 * Demonstrates composition with Meal, Burger, Drink, and SideItem classes.
 *
 * Features:
 * - Default meal creation with no arguments
 * - Custom meal creation with specific items
 * - Add extra toppings to burgers (max 3 for regular, 5 included for deluxe)
 * - Change drink sizes dynamically
 * - Itemized receipt with pricing breakdown
 * - Total meal cost calculation
 * - Support for Deluxe Burgers with 5 included premium toppings
 *
 * This application demonstrates:
 * - Composition pattern (Meal "has-a" Burger, Drink, SideItem)
 * - Interactive user input with Scanner
 * - Object-oriented design principles
 * - Encapsulation of meal components
 */
public class BurgerOrderApp {
    private static final Scanner _scanner = new Scanner(System.in);
    private static final List<Meal> _mealHistory = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   WELCOME TO BILL'S BURGER ORDERING SYSTEM ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        String continueOrdering = "yes";

        while (continueOrdering.equalsIgnoreCase("yes") || continueOrdering.equalsIgnoreCase("y")) {
            System.out.println("┌─ CREATE YOUR MEAL ─┐");
            System.out.println("1. Default Meal (no customization)");
            System.out.println("2. Custom Meal");
            System.out.println("3. Deluxe Meal (with premium 5-topping burger)");
            System.out.print("Choose option (1-3): ");

            String choice = _scanner.nextLine().trim();
            Meal currentMeal = null;

            switch (choice) {
                case "1" -> currentMeal = createDefaultMeal();
                case "2" -> currentMeal = createCustomMeal();
                case "3" -> currentMeal = createDeluxeMeal();
                default -> {
                    System.out.println("⚠️  Invalid choice. Defaulting to Default Meal.\n");
                    currentMeal = createDefaultMeal();
                }
            }

            // Customize the meal
            customizeMeal(currentMeal);

            // Display receipt
            currentMeal.printItemizedReceipt();

            // Save to history
            _mealHistory.add(currentMeal);

            // Ask if user wants to order again
            System.out.print("Order another meal? (yes/no): ");
            continueOrdering = _scanner.nextLine().trim();
            System.out.println();
        }

        // Display summary
        displayOrderSummary();

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║   THANK YOU FOR ORDERING! ENJOY YOUR MEAL! ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        _scanner.close();
    }

    /**
     * Creates a default meal with no customization
     * Default includes: Classic Burger, Medium Coca-Cola, French Fries
     * @return A new default Meal
     */
    private static Meal createDefaultMeal() {
        System.out.println("\n✓ Creating default meal...");
        Meal defaultMeal = new Meal();
        System.out.println("✓ Default Meal Created:");
        System.out.println("  • Classic Burger ($7.99)");
        System.out.println("  • Medium Coca-Cola ($2.99)");
        System.out.println("  • French Fries ($2.49)\n");
        return defaultMeal;
    }

    /**
     * Creates a custom meal with user-selected components
     * User can choose burger type, drink type, and side item
     * @return A new custom Meal
     */
    private static Meal createCustomMeal() {
        System.out.println("\n--- CUSTOM BURGER ---");

        // Select burger type
        System.out.print("Enter burger type (e.g., 'Classic', 'Beef', 'Chicken'): ");
        String burgerType = _scanner.nextLine().trim();
        if (burgerType.isEmpty()) burgerType = "Classic";

        System.out.print("Enter burger price: $");
        double burgerPrice = getValidDoubleInput();

        Burger burger = new Burger(burgerType, burgerPrice);

        // Select drink type
        System.out.println("\n--- CUSTOM DRINK ---");
        System.out.print("Enter drink type (e.g., 'Coca-Cola', 'Sprite', 'Water'): ");
        String drinkType = _scanner.nextLine().trim();
        if (drinkType.isEmpty()) drinkType = "Coca-Cola";

        System.out.print("Enter small size price: $");
        double smallPrice = getValidDoubleInput();
        System.out.print("Enter medium size price: $");
        double mediumPrice = getValidDoubleInput();
        System.out.print("Enter large size price: $");
        double largePrice = getValidDoubleInput();

        Drink drink = new Drink(drinkType, smallPrice, mediumPrice, largePrice);

        // Select side item
        System.out.println("\n--- SIDE ITEM ---");
        System.out.print("Enter side item name (e.g., 'Fries', 'Onion Rings'): ");
        String sideName = _scanner.nextLine().trim();
        if (sideName.isEmpty()) sideName = "French Fries";

        System.out.print("Enter side item price: $");
        double sidePrice = getValidDoubleInput();

        SideItem sideItem = new SideItem(sideName, sidePrice);

        return new Meal(burger, drink, sideItem);
    }

    /**
     * Creates a deluxe meal with premium burger including 5 toppings
     * User can select deluxe burger type and custom drink/side
     * @return A new Deluxe Meal
     */
    private static Meal createDeluxeMeal() {
        System.out.println("\n--- DELUXE MEAL (5 Premium Toppings Included!) ---");

        System.out.print("Enter deluxe burger name (e.g., 'Premium Beef', 'Classic Deluxe'): ");
        String burgerType = _scanner.nextLine().trim();
        if (burgerType.isEmpty()) burgerType = "Premium Beef";

        System.out.print("Enter deluxe burger price (includes 5 toppings): $");
        double burgerPrice = getValidDoubleInput();

        DeluxeBurger deluxeBurger = new DeluxeBurger(burgerType, burgerPrice);

        // Select drink
        System.out.println("\n--- DRINK ---");
        System.out.print("Enter drink type: ");
        String drinkType = _scanner.nextLine().trim();
        if (drinkType.isEmpty()) drinkType = "Coca-Cola";

        System.out.print("Enter small size price: $");
        double smallPrice = getValidDoubleInput();
        System.out.print("Enter medium size price: $");
        double mediumPrice = getValidDoubleInput();
        System.out.print("Enter large size price: $");
        double largePrice = getValidDoubleInput();

        Drink drink = new Drink(drinkType, smallPrice, mediumPrice, largePrice);

        // Select side
        System.out.println("\n--- SIDE ITEM ---");
        System.out.print("Enter side item name: ");
        String sideName = _scanner.nextLine().trim();
        if (sideName.isEmpty()) sideName = "French Fries";

        System.out.print("Enter side item price: $");
        double sidePrice = getValidDoubleInput();

        SideItem sideItem = new SideItem(sideName, sidePrice);

        return new Meal(deluxeBurger, drink, sideItem);
    }

    /**
     * Customizes an existing meal by allowing user to:
     * - Add extra toppings to the burger
     * - Change drink size
     * @param meal The meal to customize
     */
    private static void customizeMeal(Meal meal) {
        String continueCustomizing = "yes";

        while (continueCustomizing.equalsIgnoreCase("yes") || continueCustomizing.equalsIgnoreCase("y")) {
            System.out.println("\n┌─ CUSTOMIZE MEAL ─┐");
            System.out.println("1. Add topping to burger");
            System.out.println("2. Change drink size");
            System.out.println("3. Done customizing");
            System.out.print("Choose option (1-3): ");

            String choice = _scanner.nextLine().trim();

            switch (choice) {
                case "1" -> addToppingToBurger(meal.getBurger());
                case "2" -> changeDrinkSize(meal.getDrink());
                case "3" -> continueCustomizing = "no";
                default -> System.out.println("⚠️  Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Adds an extra topping to the burger
     * Regular burgers can have up to 3 toppings
     * Deluxe burgers already include 5 toppings and cannot add more
     * @param burger The burger to add topping to
     */
    private static void addToppingToBurger(Burger burger) {
        System.out.println("\n--- ADD TOPPING ---");
        System.out.println("Available Toppings:");
        System.out.println("1. Bacon ($1.50)");
        System.out.println("2. Cheese ($0.75)");
        System.out.println("3. Mushrooms ($1.00)");
        System.out.println("4. Jalapenos ($0.50)");
        System.out.println("5. Avocado ($2.00)");
        System.out.println("6. Fried Egg ($1.25)");
        System.out.print("Choose topping (1-6): ");

        String choice = _scanner.nextLine().trim();
        Topping selectedTopping = null;

        switch (choice) {
            case "1" -> selectedTopping = new Topping("Bacon", 1.50);
            case "2" -> selectedTopping = new Topping("Cheese", 0.75);
            case "3" -> selectedTopping = new Topping("Mushrooms", 1.00);
            case "4" -> selectedTopping = new Topping("Jalapenos", 0.50);
            case "5" -> selectedTopping = new Topping("Avocado", 2.00);
            case "6" -> selectedTopping = new Topping("Fried Egg", 1.25);
            default -> System.out.println("⚠️  Invalid choice.");
        }

        if (selectedTopping != null) {
            if (burger.addTopping(selectedTopping)) {
                System.out.println("✓ Added " + selectedTopping.getName() + " (+$" +
                        String.format("%.2f", selectedTopping.getPrice()) + ")");
                System.out.println("✓ Toppings on burger: " + burger.getToppingCount());
            }
        }
    }

    /**
     * Changes the drink size
     * Available sizes: Small, Medium, Large
     * @param drink The drink to change size for
     */
    private static void changeDrinkSize(Drink drink) {
        System.out.println("\n--- CHANGE DRINK SIZE ---");
        System.out.println("Current size: " + drink.getSize().getDisplayName() +
                " ($" + String.format("%.2f", drink.getPrice()) + ")");
        System.out.println("\nAvailable sizes:");
        System.out.println("1. Small ($" + String.format("%.2f", drink.getPriceForSize(Drink.Size.SMALL)) + ")");
        System.out.println("2. Medium ($" + String.format("%.2f", drink.getPriceForSize(Drink.Size.MEDIUM)) + ")");
        System.out.println("3. Large ($" + String.format("%.2f", drink.getPriceForSize(Drink.Size.LARGE)) + ")");
        System.out.print("Choose size (1-3): ");

        String choice = _scanner.nextLine().trim();

        switch (choice) {
            case "1" -> {
                drink.setSize(Drink.Size.SMALL);
                System.out.println("✓ Changed to Small ($" + String.format("%.2f", drink.getPrice()) + ")");
            }
            case "2" -> {
                drink.setSize(Drink.Size.MEDIUM);
                System.out.println("✓ Changed to Medium ($" + String.format("%.2f", drink.getPrice()) + ")");
            }
            case "3" -> {
                drink.setSize(Drink.Size.LARGE);
                System.out.println("✓ Changed to Large ($" + String.format("%.2f", drink.getPrice()) + ")");
            }
            default -> System.out.println("⚠️  Invalid choice.");
        }
    }

    /**
     * Displays a summary of all orders placed during this session
     * Shows total revenue from all meals
     */
    private static void displayOrderSummary() {
        if (_mealHistory.isEmpty()) {
            System.out.println("\nNo meals ordered in this session.\n");
            return;
        }

        System.out.println("\n╔════════════════════════════════════════════╗");
        System.out.println("║        ORDER SUMMARY (ALL MEALS)           ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        double totalRevenue = 0.0;
        for (int i = 0; i < _mealHistory.size(); i++) {
            Meal meal = _mealHistory.get(i);
            double mealTotal = meal.getTotalPrice();
            totalRevenue += mealTotal;
            System.out.println("Meal #" + (i + 1) + ": $" + String.format("%.2f", mealTotal));
        }

        System.out.println("\n" + "─".repeat(44));
        System.out.println("Total Revenue: $" + String.format("%.2f", totalRevenue));
        System.out.println("Total Meals: " + _mealHistory.size());
        System.out.println("─".repeat(44));
    }

    /**
     * Helper method to get a valid double input from user
     * Keeps asking until valid double is entered
     * @return A valid double value
     */
    private static double getValidDoubleInput() {
        while (true) {
            try {
                String input = _scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return 0.0;
                }
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("⚠️  Invalid input. Please enter a valid price: $");
            }
        }
    }
}

