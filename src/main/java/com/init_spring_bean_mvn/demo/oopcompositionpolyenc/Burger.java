package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a burger with a type, base price, and optional extra toppings.
 * Burgers can have a maximum of 3 extra toppings.
 * This class demonstrates composition - a burger is composed of toppings.
 *
 * Maximum toppings: 3
 * Toppings can be added via addTopping() method
 */
public class Burger {
    private final String _burgerType;
    private final double _basePrice;
    private final List<Topping> _extraToppings;
    private static final int MAX_TOPPINGS = 3;

    /**
     * Constructor for creating a burger with type and base price
     * Standard burgers have a maximum of 3 extra toppings
     *
     * @param burgerType The type of burger (e.g., "Classic", "Beef", "Chicken")
     * @param basePrice The base price of the burger before toppings
     */
    public Burger(String burgerType, double basePrice) {
        this._burgerType = burgerType;
        this._basePrice = basePrice;
        this._extraToppings = new ArrayList<>();
    }

    /**
     * Gets the burger type
     * @return The burger type name
     */
    public String getBurgerType() {
        return _burgerType;
    }

    /**
     * Gets the base price of the burger (before toppings)
     * @return The base price
     */
    public double getBasePrice() {
        return _basePrice;
    }

    /**
     * Gets the list of extra toppings on this burger
     * @return List of Topping objects
     */
    public List<Topping> getExtraToppings() {
        return new ArrayList<>(_extraToppings);
    }

    /**
     * Adds an extra topping to the burger if maximum hasn't been reached
     * @param topping The topping to add
     * @return true if topping was added, false if maximum toppings reached
     */
    public boolean addTopping(Topping topping) {
        if (_extraToppings.size() < MAX_TOPPINGS) {
            _extraToppings.add(topping);
            return true;
        }
        System.out.println("⚠️  Cannot add topping: Maximum " + MAX_TOPPINGS + " toppings allowed!");
        return false;
    }

    /**
     * Removes a topping from the burger by index
     * @param index The index of the topping to remove
     * @return true if topping was removed, false if index is invalid
     */
    public boolean removeTopping(int index) {
        if (index >= 0 && index < _extraToppings.size()) {
            _extraToppings.remove(index);
            return true;
        }
        return false;
    }

    /**
     * Calculates the total price of the burger including all extra toppings
     * @return Total price (base price + toppings price)
     */
    public double getTotalPrice() {
        double toppingsTotal = _extraToppings.stream()
                .mapToDouble(Topping::getPrice)
                .sum();
        return _basePrice + toppingsTotal;
    }

    /**
     * Gets the count of extra toppings on this burger
     * @return Number of extra toppings
     */
    public int getToppingCount() {
        return _extraToppings.size();
    }

    /**
     * Checks if burger has reached maximum toppings
     * @return true if at maximum toppings, false otherwise
     */
    public boolean isMaxToppings() {
        return _extraToppings.size() >= MAX_TOPPINGS;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(_burgerType).append(" Burger - $").append(String.format("%.2f", _basePrice));
        if (!_extraToppings.isEmpty()) {
            sb.append("\n  Extra Toppings (").append(_extraToppings.size()).append("):");
            for (Topping topping : _extraToppings) {
                sb.append("\n    - ").append(topping);
            }
        }
        return sb.toString();
    }
}

