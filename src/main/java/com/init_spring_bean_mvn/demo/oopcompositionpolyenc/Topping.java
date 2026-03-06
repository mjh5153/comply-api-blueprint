package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Represents an extra topping that can be added to a burger.
 * Each topping has a name and associated price.
 * This class is immutable and follows the enum-like pattern for predefined toppings.
 */
public class Topping {
    private final String _name;
    private final double _price;

    /**
     * Constructor for creating a topping with name and price
     * @param name The name of the topping (e.g., "Bacon", "Cheese")
     * @param price The price of the topping
     */
    public Topping(String name, double price) {
        this._name = name;
        this._price = price;
    }

    /**
     * Gets the name of the topping
     * @return The topping name
     */
    public String getName() {
        return _name;
    }

    /**
     * Gets the price of the topping
     * @return The topping price
     */
    public double getPrice() {
        return _price;
    }

    @Override
    public String toString() {
        return _name + " (+$" + String.format("%.2f", _price) + ")";
    }
}

