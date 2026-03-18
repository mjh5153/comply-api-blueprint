package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

public class PlayerRunner {
    public static void main(String[] args) {
        Player player = new Player("Legolas");

        // Set player variables
        player.health = 100;
        player.weapon = "Elven Bow";
            // bad - accesses to application in ways it shouldn't be, should be private
        System.out.println("=== PLAYER HEALTH SYSTEM ===\n");
        System.out.println("Initial Player State: " + player);

        // Set damage variable
        int damage = 25;

        // With big applications you need to take a look at the code in Player to see what the name is - When
        // deplyed in library, setting yourself up for a lot of problems when external people are accessing your code
        // Also manually initializing variables at the start, code might forget to initialize manually
        // With constructor can make sure data and object is valid before the game starts

        // No real way with manual initialization to make sure the data is valid, can set health to negative or weapon to null, etc.

        // Encapsulation main benefit - make items a black box to outside code and have full control over how the data is accessed and modified, can enforce rules and validation, and prevent invalid states

        /* Encapsulation best practices: */
        // Use setter methods sparingly and only as needed
        // use access modifiers that aren't private, only for methods that calling code needs to use
        // Create constructors for object initialization which enforces only objects with valid data will get created


        // First loseHealth call
        System.out.println("\n--- First Attack ---");
        System.out.println("Damage taken: " + damage);
        player.loseHealth(damage);
        System.out.println("Health after first attack: " + player.getHealth());
        System.out.println("Player State: " + player);

        // Second loseHealth call
        System.out.println("\n--- Second Attack ---");
        System.out.println("Damage taken: " + damage);
        player.loseHealth(damage);
        System.out.println("Health after second attack: " + player.getHealth());
        System.out.println("Player State: " + player);

        System.out.println("\n--- Final Summary ---");
        System.out.println("Total damage taken: " + (damage * 2));
        System.out.println("Remaining health: " + player.getHealth() + "/" + player.maxHealth);
    }
}
