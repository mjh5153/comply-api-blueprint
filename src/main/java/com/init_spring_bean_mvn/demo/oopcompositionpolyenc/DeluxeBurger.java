package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Represents a premium Deluxe Burger that includes a minimum of 5 extra toppings.
 * All 5 toppings are included in the base price - no additional topping charges.
 * This class extends Burger and demonstrates specialization through inheritance.
 *
 * DeluxeBurger features:
 * - Includes 5 preset premium toppings at no extra cost
 * - Cannot add additional toppings (already at maximum with included toppings)
 * - Premium pricing reflects the included toppings
 */
public class DeluxeBurger extends Burger {
    private static final int INCLUDED_TOPPINGS = 5;

    /**
     * Constructor for creating a deluxe burger with preset premium toppings
     * Automatically includes 5 toppings: Bacon, Cheese, Lettuce, Tomato, Pickles
     * All toppings are included in the base price
     *
     * @param burgerType The type of burger (e.g., "Premium Beef", "Deluxe Classic")
     * @param basePrice The base price which includes all 5 toppings
     */
    public DeluxeBurger(String burgerType, double basePrice) {
        super(burgerType, basePrice);
        initializeDeluxeToppings();
    }

    /**
     * Initializes the 5 premium toppings that come with the deluxe burger
     * These toppings are included in the base price
     */
    private void initializeDeluxeToppings() {
        // Add 5 preset toppings - all included in price (so price is 0 for each)
        super.addTopping(new Topping("Bacon", 0.0));
        super.addTopping(new Topping("Cheese", 0.0));
        super.addTopping(new Topping("Lettuce", 0.0));
        super.addTopping(new Topping("Tomato", 0.0));
        super.addTopping(new Topping("Pickles", 0.0));
    }

    /**
     * Override to prevent adding more toppings to deluxe burger
     * Deluxe burgers already include 5 premium toppings
     *
     * @param topping The topping to add (will be rejected)
     * @return Always false - cannot add additional toppings to deluxe burger
     */
    @Override
    public boolean addTopping(Topping topping) {
        System.out.println("⚠️  Deluxe Burger already includes 5 premium toppings - no additional toppings allowed!");
        return false;
    }

    /**
     * Override to prevent removing included toppings from deluxe burger
     * @param index The index of topping to remove
     * @return Always false - cannot remove preset toppings
     */
    @Override
    public boolean removeTopping(int index) {
        System.out.println("⚠️  Cannot remove included toppings from Deluxe Burger!");
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("🌟 ").append(getBurgerType()).append(" (DELUXE) - $")
                .append(String.format("%.2f", getBasePrice()));
        sb.append("\n  Includes 5 Premium Toppings:");
        for (int i = 0; i < getExtraToppings().size(); i++) {
            sb.append("\n    ").append(i + 1).append(". ").append(getExtraToppings().get(i).getName());
        }
        return sb.toString();
    }
}

