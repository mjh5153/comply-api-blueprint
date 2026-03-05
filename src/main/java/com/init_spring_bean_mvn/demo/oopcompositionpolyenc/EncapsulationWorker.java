package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * EncapsulationWorker demonstrates why encapsulation is important in OOP.
 * This class shows the problems that occur when a class (like Player) doesn't use encapsulation.
 *
 * Key lessons:
 * - Public fields allow direct access and modification without validation
 * - No encapsulation leads to invalid object states
 * - Business logic cannot be enforced
 * - Data integrity is compromised
 */
public class EncapsulationWorker {
    public static void main(String[] args) {
        // PROBLEMS WITH NO ENCAPSULATION - Direct field access allows data corruption

        System.out.println("=== NO ENCAPSULATION PROBLEMS ===\n");

        Player hero = new Player("Aragorn");
        System.out.println("Initial player: " + hero);

        // PROBLEM 1: Can set health to invalid values
        System.out.println("\n--- PROBLEM 1: Invalid Health Values ---");
        hero.health = -9999;  // NEGATIVE HEALTH?! No validation!
        System.out.println("After setting health to -9999: " + hero);
        System.out.println("^ INVALID STATE: Dead player but system doesn't know!\n");

        // PROBLEM 2: Can set health higher than maxHealth
        System.out.println("--- PROBLEM 2: Health Exceeds Max ---");
        hero.health = 500;  // Can have more health than maxHealth!
        System.out.println("After setting health to 500 (maxHealth=100): " + hero);
        System.out.println("^ INVALID STATE: Health > maxHealth!\n");

        // PROBLEM 3: Can set level to invalid values
        System.out.println("--- PROBLEM 3: Invalid Level Values ---");
        hero.level = -10;  // Negative level?!
        System.out.println("After setting level to -10: " + hero);
        System.out.println("^ INVALID STATE: Negative level doesn't make sense!\n");

        // PROBLEM 4: Can set gold to negative
        System.out.println("--- PROBLEM 4: Negative Currency ---");
        hero.gold = -1000;  // Negative money?!
        System.out.println("After setting gold to -1000: " + hero);
        System.out.println("^ INVALID STATE: Player owes money instead of having it!\n");

        // PROBLEM 5: Can set maxHealth to less than current health
        System.out.println("--- PROBLEM 5: Inconsistent State ---");
        hero.maxHealth = 10;
        System.out.println("After setting maxHealth to 10: " + hero);
        System.out.println("^ INVALID STATE: Current health (500) > maxHealth (10)!\n");

        // PROBLEM 6: Can change name to anything including invalid values
        System.out.println("--- PROBLEM 6: Invalid Names ---");
        hero.name = null;
        System.out.println("After setting name to null: " + hero);
        System.out.println("^ INVALID STATE: Player has no name!\n");

        System.out.println("=== CONCLUSION ===");
        System.out.println("Without encapsulation:");
        System.out.println("✗ No data validation");
        System.out.println("✗ No enforcement of business rules");
        System.out.println("✗ Data can enter INVALID STATES");
        System.out.println("✗ Calling code can directly corrupt object state");
        System.out.println("✗ Impossible to maintain data integrity\n");
        System.out.println("SOLUTION: Use encapsulation with private fields and public getters/setters!");
    }
}
