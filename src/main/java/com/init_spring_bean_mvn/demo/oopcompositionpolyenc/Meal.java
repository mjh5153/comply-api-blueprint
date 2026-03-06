package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Represents a complete meal consisting of a burger, drink, and side item.
 * This class demonstrates composition - a meal is composed of three separate items.
 *
 * Meals can be created:
 * 1. With no arguments (default meal with standard burger, drink, and fries)
 * 2. With custom burger, drink, and side item
 *
 * Features:
 * - Add extra toppings to the burger
 * - Change drink size
 * - Calculate itemized pricing
 * - Calculate total meal cost
 */
public class Meal {
    private Burger _burger;
    private Drink _drink;
    private SideItem _sideItem;

    /**
     * No-argument constructor that creates a default meal
     * Default meal includes:
     * - Classic Burger ($7.99)
     * - Medium Coca-Cola ($2.50 for small, $2.99 for medium, $3.49 for large)
     * - French Fries ($2.49)
     */
    public Meal() {
        this._burger = new Burger("Classic", 7.99);
        this._drink = new Drink("Coca-Cola", 2.50, 2.99, 3.49);
        this._sideItem = new SideItem("French Fries", 2.49);
    }

    /**
     * Constructor for creating a custom meal with specific items
     * @param burger The burger for the meal
     * @param drink The drink for the meal
     * @param sideItem The side item for the meal
     */

    //@Todo - Do a video demonstrating Agent ability to modify existing code inline via prompts
    // Can make constructor take Strings for each type so that the calling code knows even less about the underlying impl
    public Meal(Burger burger, Drink drink, SideItem sideItem) {
        this._burger = burger;
        this._drink = drink;
        this._sideItem = sideItem;
    }

    // can be evolved to use a container for burger, drink, side item to allow for more flexible meal compositions in the future


    /**
     * Gets the burger in this meal
     * @return The burger object
     */
    public Burger getBurger() {
        return _burger;
    }

    /**
     * Sets the burger in this meal
     * @param burger The new burger to set
     */
    public void setBurger(Burger burger) {
        this._burger = burger;
    }

    /**
     * Gets the drink in this meal
     * @return The drink object
     */
    public Drink getDrink() {
        return _drink;
    }

    /**
     * Sets the drink in this meal
     * @param drink The new drink to set
     */
    public void setDrink(Drink drink) {
        this._drink = drink;
    }

    /**
     * Gets the side item in this meal
     * @return The side item object
     */
    public SideItem getSideItem() {
        return _sideItem;
    }

    /**
     * Sets the side item in this meal
     * @param sideItem The new side item to set
     */
    public void setSideItem(SideItem sideItem) {
        this._sideItem = sideItem;
    }

    /**
     * Calculates the total price of the meal
     * Total = Burger price (with toppings) + Drink price (based on size) + Side item price
     * @return The total meal price
     */
    public double getTotalPrice() {
        return _burger.getTotalPrice() + _drink.getPrice() + _sideItem.getPrice();
    }

    /**
     * Prints an itemized list of all meal components with prices
     * Shows:
     * - Burger with base price and any extra toppings with their individual prices
     * - Drink with size and size-based price
     * - Side item with price
     * - Total amount due
     */
    public void printItemizedReceipt() {
        System.out.println("\n" + "=".repeat(50));
        System.out.println("ITEMIZED MEAL RECEIPT");
        System.out.println("=".repeat(50));

        // Print burger details
        System.out.println("\n🍔 BURGER:");
        System.out.println("   " + _burger.getBurgerType() + " Burger: $" +
                String.format("%.2f", _burger.getBasePrice()));

        // Print toppings if any
        if (_burger.getToppingCount() > 0) {
            System.out.println("   Extra Toppings (" + _burger.getToppingCount() + "):");
            for (Topping topping : _burger.getExtraToppings()) {
                System.out.println("      + " + topping.getName() + ": $" +
                        String.format("%.2f", topping.getPrice()));
            }
        }
        System.out.println("   Subtotal (Burger): $" + String.format("%.2f", _burger.getTotalPrice()));

        // Print drink details
        System.out.println("\n🥤 DRINK:");
        System.out.println("   " + _drink.getDrinkType() + " (" + _drink.getSize().getDisplayName() + "): $" +
                String.format("%.2f", _drink.getPrice()));

        // Print side item details
        System.out.println("\n🍟 SIDE:");
        System.out.println("   " + _sideItem.getItemName() + ": $" +
                String.format("%.2f", _sideItem.getPrice()));

        // Print total
        System.out.println("\n" + "-".repeat(50));
        System.out.println("TOTAL DUE: $" + String.format("%.2f", getTotalPrice()));
        System.out.println("=".repeat(50) + "\n");
    }

    @Override
    public String toString() {
        return "Meal{\n" +
                "  " + _burger + "\n" +
                "  " + _drink + "\n" +
                "  " + _sideItem + "\n" +
                "  Total Price: $" + String.format("%.2f", getTotalPrice()) +
                "\n}";
    }
}

