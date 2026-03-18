#!/bin/bash

# Burger Order App - Interactive Demo Script
# This script demonstrates the BurgerOrderApp with sample inputs

cat << 'EOF'

╔════════════════════════════════════════════════════════════════════════════╗
║                  BURGER ORDER APP - DEMONSTRATION                          ║
║                                                                            ║
║  This interactive application allows customers to:                         ║
║  • Create default meals with pre-configured items                          ║
║  • Create custom meals with personalized options                           ║
║  • Create deluxe meals with 5 premium toppings included                    ║
║  • Add extra toppings to regular burgers (max 3)                           ║
║  • Change drink sizes (Small, Medium, Large)                               ║
║  • View itemized receipts with price breakdown                             ║
║  • Track total meal costs                                                  ║
╚════════════════════════════════════════════════════════════════════════════╝

================================================================================
                           CLASS ARCHITECTURE
================================================================================

📦 COMPOSITION PATTERN - Meal "has-a" Burger, Drink, SideItem

┌─────────────────────────────────────────────────────────────────────────┐
│                             MEAL CLASS                                  │
│  ├─ Burger (either regular or Deluxe)                                  │
│  │  └─ List<Topping> (0-3 toppings for regular, 5 fixed for deluxe)   │
│  ├─ Drink                                                              │
│  │  └─ Size (Small, Medium, Large with different prices)             │
│  └─ SideItem                                                           │
└─────────────────────────────────────────────────────────────────────────┘

================================================================================
                         CLASS RELATIONSHIPS
================================================================================

TOPPING CLASS:
  • Represents an extra topping with name and price
  • Used to add optional toppings to burgers
  • Price varies by topping type (Bacon, Cheese, Mushrooms, etc.)

BURGER CLASS:
  • Base burger class with type, price, and extra toppings
  • Maximum of 3 extra toppings
  • Methods: addTopping(), removeTopping(), getTotalPrice()
  • getTotalPrice() = basePrice + sum of topping prices

DELUXEBURGER CLASS (extends Burger):
  • Premium burger that includes 5 toppings in base price
  • Toppings included: Bacon, Cheese, Lettuce, Tomato, Pickles
  • Cannot add/remove toppings (already at maximum)
  • No additional topping charges - all included in base price

DRINK CLASS:
  • Represents a beverage with size-based pricing
  • Sizes: Small, Medium, Large (each with different price)
  • Default size: Medium
  • Method: setSize() to change drink size dynamically

SIDEITEM CLASS:
  • Represents side items (Fries, Onion Rings, etc.)
  • Fixed price (not size-dependent)

MEAL CLASS:
  • Composes Burger, Drink, and SideItem
  • No-arg constructor creates default meal (Classic Burger, Coca-Cola, Fries)
  • Custom constructor allows any combination of items
  • printItemizedReceipt() shows detailed pricing breakdown
  • getTotalPrice() calculates: burger + drink + side

================================================================================
                          FEATURES OVERVIEW
================================================================================

DEFAULT MEAL (No Customization):
  ✓ Classic Burger: $7.99
  ✓ Medium Coca-Cola: $2.99
  ✓ French Fries: $2.49
  ─────────────────────────
  Total: $13.47

CUSTOM MEAL:
  ✓ User selects burger type and price
  ✓ User selects drink type and prices for all sizes
  ✓ User selects side item and price

DELUXE MEAL:
  ✓ Premium Beef Burger: $14.99 (includes 5 toppings)
  ✓ Includes: Bacon, Cheese, Lettuce, Tomato, Pickles
  ✓ Can select custom drink and side
  ✓ Cannot add additional toppings

CUSTOMIZATION OPTIONS:
  • Add extra toppings to regular burgers (up to 3)
    - Bacon ($1.50)
    - Cheese ($0.75)
    - Mushrooms ($1.00)
    - Jalapenos ($0.50)
    - Avocado ($2.00)
    - Fried Egg ($1.25)

  • Change drink size:
    - Small: Lower price (e.g., $2.50 for Coca-Cola)
    - Medium: Medium price (e.g., $2.99 for Coca-Cola)
    - Large: Higher price (e.g., $3.49 for Coca-Cola)

================================================================================
                       EXAMPLE MEAL CALCULATIONS
================================================================================

Example 1: DEFAULT MEAL
  Classic Burger:           $7.99
  Medium Coca-Cola:         $2.99
  French Fries:             $2.49
  ─────────────────────────────────
  TOTAL DUE:               $13.47

Example 2: CUSTOM MEAL WITH TOPPINGS
  Beef Burger:              $8.99
    + Bacon:                $1.50
    + Cheese:               $0.75
    + Mushrooms:            $1.00
  Large Sprite:             $3.49
  Onion Rings:              $2.99
  ─────────────────────────────────
  TOTAL DUE:               $18.72

Example 3: DELUXE MEAL
  Premium Beef (DELUXE):   $14.99
    Includes:
    • Bacon                 (no charge)
    • Cheese                (no charge)
    • Lettuce               (no charge)
    • Tomato                (no charge)
    • Pickles               (no charge)
  Small Coca-Cola:          $2.50
  French Fries:             $2.49
  ─────────────────────────────────
  TOTAL DUE:               $19.98

================================================================================
                          KEY DESIGN PATTERNS
================================================================================

1. COMPOSITION (HAS-A relationship):
   • Meal HAS-A Burger (not Meal extends Burger)
   • Burger HAS-A List<Topping>
   • Meal HAS-A Drink
   • Meal HAS-A SideItem
   → More flexible than inheritance

2. ENCAPSULATION:
   • Private fields with underscore prefix (_burger, _drink, etc.)
   • Public getter/setter methods to control access
   • Validation in setters (e.g., topping limit check)

3. INHERITANCE (Specialization):
   • DeluxeBurger extends Burger
   • Adds specialized behavior (fixed toppings)
   • Overrides methods to prevent adding toppings

4. STRATEGY PATTERN (Size-based pricing):
   • Drink sizes have different prices
   • Size is changeable at runtime
   • Price calculated based on selected size

5. BUILDER-LIKE PATTERN:
   • Meal can be customized step-by-step
   • Add toppings incrementally
   • Change drink size after meal creation

================================================================================
                            HOW TO RUN
================================================================================

To run the application:

  1. Navigate to project directory:
     cd /Users/mjh5153/Downloads/demo

  2. Compile the project:
     mvn clean compile

  3. Run the application:
     mvn exec:java@burger-order-app

     OR manually:

     java -cp target/classes \\
     com.init_spring_bean_mvn.demo.oopcompositionpolyenc.BurgerOrderApp

  4. Follow the interactive prompts to:
     • Select meal type (Default, Custom, or Deluxe)
     • Customize burger with toppings
     • Change drink size
     • View itemized receipt
     • See total cost

================================================================================
                       LEARNING OBJECTIVES
================================================================================

This application demonstrates:

✓ Object-Oriented Programming (OOP) Principles:
  • Encapsulation: Private fields, public methods
  • Inheritance: DeluxeBurger extends Burger
  • Polymorphism: Different burger types behave differently
  • Composition: Complex objects made of simpler objects

✓ Design Patterns:
  • Composition Pattern
  • Strategy Pattern (size-based pricing)
  • Builder Pattern (step-by-step customization)

✓ Java Features:
  • Enum (Size enumeration)
  • Switch expressions (Java 14+)
  • Stream API (calculating topping prices)
  • ArrayList for dynamic collections
  • Scanner for user input

✓ Best Practices:
  • Meaningful class and method names
  • Comprehensive JavaDoc documentation
  • Validation of user input
  • Consistent naming conventions (underscore prefix for private fields)
  • Constructor overloading (no-arg and custom constructors)

================================================================================
                          PROJECT STRUCTURE
================================================================================

demo/src/main/java/com/init_spring_bean_mvn/demo/oopcompositionpolyenc/
  ├── Topping.java              - Extra topping with name and price
  ├── Burger.java               - Regular burger with up to 3 toppings
  ├── DeluxeBurger.java         - Premium burger with 5 included toppings
  ├── Drink.java                - Beverage with size-based pricing
  ├── SideItem.java             - Side dish with fixed price
  ├── Meal.java                 - Complete meal (burger + drink + side)
  └── BurgerOrderApp.java       - Interactive main application

================================================================================

Ready to explore the burger ordering system!

EOF

echo ""
echo "To run the application, execute:"
echo ""
echo "  cd /Users/mjh5153/Downloads/demo"
echo "  mvn exec:java@burger-order-app"
echo ""
echo "Or directly:"
echo ""
echo "  java -cp target/classes com.init_spring_bean_mvn.demo.oopcompositionpolyenc.BurgerOrderApp"
echo ""

