package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Represents a side item that can be part of a meal.
 * Side items have a name and a fixed price.
 * Examples: Fries, Onion Rings, Coleslaw, etc.
 */
public class SideItem {
    private final String _itemName;
    private final double _price;

    /**
     * Constructor for creating a side item with name and price
     * @param itemName The name of the side item
     * @param price The price of the side item
     */
    public SideItem(String itemName, double price) {
        this._itemName = itemName;
        this._price = price;
    }

    /**
     * Gets the name of the side item
     * @return The side item name
     */
    public String getItemName() {
        return _itemName;
    }

    /**
     * Gets the price of the side item
     * @return The side item price
     */
    public double getPrice() {
        return _price;
    }

    @Override
    public String toString() {
        return _itemName + " - $" + String.format("%.2f", _price);
    }
}

