package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * BAD EXAMPLE: Player class WITHOUT encapsulation
 * This class demonstrates poor design by exposing all fields directly to the public.
 * This shows WHY encapsulation is important for data integrity and maintainability.
 *
 * Problems with this approach:
 * 1. No control over data validation - anyone can set invalid values
 * 2. No data integrity - fields can be modified directly to illegal states
 * 3. Tight coupling - changing field names breaks all calling code
 * 4. No business logic enforcement - can't prevent invalid operations
 */
public class Player {
    // BAD: All fields are public - NO ENCAPSULATION
    public String name;
    public int level;
    public int health;
    public int maxHealth;
    public int experience;
    public int gold;
    public String weapon;

    /**
     * Constructor - but no validation happens here
     */
    public Player(String name) {
        this.name = name;
        this.level = 1;
        this.health = 100;
        this.maxHealth = 100;
        this.experience = 0;
        this.gold = 0;
    }

    /**
     * Reduces the player's health by the specified damage amount.
     * This demonstrates the lack of encapsulation - anyone can call this
     * and modify health, but there's no real validation here either.
     *
     * @param damage The amount of damage to apply
     */
    public void loseHealth(int damage) {
        this.health = this.health - damage;
    }

    /**
     * Returns the current health of the player.
     * Demonstrates a getter method that allows reading the health value.
     *
     * @return The current health value
     */
    public int getHealth() {
        return this.health;
    }

    /**
     * Restores the player's health by the specified amount.
     * This demonstrates the lack of encapsulation - anyone can call this
     * and increase health without validation or upper limit checks.
     *
     * @param extraHealth The amount of health to restore
     */
    public void restoreHealth(int extraHealth) {
        this.health = this.health + extraHealth;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", level=" + level +
                ", health=" + health + "/" + maxHealth +
                ", experience=" + experience +
                ", gold=" + gold +
                ", weapon='" + weapon + '\'' +
                '}';
    }
}

