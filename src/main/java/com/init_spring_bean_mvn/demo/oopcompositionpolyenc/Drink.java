package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Represents a drink with a base type and size-based pricing.
 * Supports three sizes: Small, Medium, Large
 * Each size has a different price increment.
 * This class demonstrates the Strategy pattern with size-based pricing.
 */
public class Drink {
    private final String _drinkType;
    private final double _smallPrice;
    private final double _mediumPrice;
    private final double _largePrice;
    private Size _currentSize;

    /**
     * Enum representing drink sizes
     */
    public enum Size {
        SMALL("Small"),
        MEDIUM("Medium"),
        LARGE("Large");

        private final String _displayName;

        Size(String displayName) {
            this._displayName = displayName;
        }

        public String getDisplayName() {
            return _displayName;
        }
    }

    /**
     * Constructor for creating a drink with type and size-based prices
     * Default size is MEDIUM
     *
     * @param drinkType The type of drink (e.g., "Coca-Cola", "Sprite", "Water")
     * @param smallPrice Price for small size
     * @param mediumPrice Price for medium size
     * @param largePrice Price for large size
     */
    public Drink(String drinkType, double smallPrice, double mediumPrice, double largePrice) {
        this._drinkType = drinkType;
        this._smallPrice = smallPrice;
        this._mediumPrice = mediumPrice;
        this._largePrice = largePrice;
        this._currentSize = Size.MEDIUM;  // Default size
    }

    /**
     * Gets the drink type
     * @return The drink type name
     */
    public String getDrinkType() {
        return _drinkType;
    }

    /**
     * Gets the current size of the drink
     * @return The current Size
     */
    public Size getSize() {
        return _currentSize;
    }

    /**
     * Sets the size of the drink
     * @param size The new size to set
     */
    public void setSize(Size size) {
        this._currentSize = size;
    }

    /**
     * Gets the price of the drink based on current size
     * @return The price for the current size
     */
    public double getPrice() {
        return switch (_currentSize) {
            case SMALL -> _smallPrice;
            case MEDIUM -> _mediumPrice;
            case LARGE -> _largePrice;
        };
    }

    /**
     * Gets the price for a specific size
     * @param size The size to get price for
     * @return The price for that size
     */
    public double getPriceForSize(Size size) {
        return switch (size) {
            case SMALL -> _smallPrice;
            case MEDIUM -> _mediumPrice;
            case LARGE -> _largePrice;
        };
    }

    @Override
    public String toString() {
        return _drinkType + " (" + _currentSize.getDisplayName() + ") - $" + String.format("%.2f", getPrice());
    }
}

