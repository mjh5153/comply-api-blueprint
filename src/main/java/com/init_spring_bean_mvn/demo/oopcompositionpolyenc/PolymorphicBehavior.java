package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

public class PolymorphicBehavior {
    public static void main(String[] args) {
        //Polymorphism in Java allows us to write code that can call a method, but actual method that gets executed can be different for different objects at runtime

        // Means behavior that occurs during program execution depends on runtime type of object, might differe from its declared type in code

        // To work - declared type must have a relationship with the runtime type - inheritance is one way to establish this relationship, where subclass can overide a method from its superclass, enabling polymorphic  behavior

        // There are other mechanisms to achieve polymorphism in Java - interfaces and abstract classes - allow us to define a contract for behavior that multiple classes can implement, enabling polymorphic behavior across different class hierarchies

        // example : Movie -- > subclasses ActionMovie, ComedyMovie, HorrorMovie - each subclass can override a method like play() to provide its own implementation of how the movie is played - we can have a list of movies and call play() on each movie without needing to know the specific type of movie at compile time - this is polymorphic behavior because the actual method that gets executed depends on the runtime type of the movie object

        System.out.println("=== POLYMORPHIC BEHAVIOR DEMONSTRATION ===\n");

        // Create Movie references pointing to Adventure instances
        Movie movie1 = new Adventure("Raiders of the Lost Ark", "Steven Spielberg", 1981, 115, "Adventure",
                                        "Quest", "Indiana Jones", true);
        Movie movie2 = new Adventure("The Lord of the Rings: Fellowship of the Ring", "Peter Jackson", 2001, 178, "Adventure",
                                        "Epic Quest", "Frodo Baggins", true);
        Movie movie3 = new Adventure("National Treasure", "Jon Turteltaub", 2004, 131, "Adventure",
                                        "Treasure Hunt", "Nicolas Cage", true);

        // Call watchMovie() method on each Movie reference (runtime type is Adventure)
        System.out.println("--- Movie 1 (Runtime type: Adventure) ---");
        movie1.watchMovie();

        System.out.println("\n--- Movie 2 (Runtime type: Adventure) ---");
        movie2.watchMovie();

        System.out.println("\n--- Movie 3 (Runtime type: Adventure) ---");
        movie3.watchMovie();

        // Create more Movie references pointing to Adventure instances
        System.out.println("\n\n=== MORE MOVIES (DECLARED AS Movie, RUNTIME TYPE Adventure) ===\n");

        Movie adventure1 = new Adventure("The Hobbit: An Unexpected Journey", "Peter Jackson", 2012, 169, "Adventure",
                                            "Quest", "Bilbo Baggins", true);
        Movie adventure2 = new Adventure("Jungle Cruise", "Jaume Collet-Serra", 2021, 127, "Adventure",
                                            "River Exploration", "Frank Wolff", true);

        // Call watchMovie() on Movie references - demonstrates polymorphism
        System.out.println("--- Movie 4 (Runtime type: Adventure) ---");
        adventure1.watchMovie();

        System.out.println("\n--- Movie 5 (Runtime type: Adventure) ---");
        adventure2.watchMovie();

        // More polymorphic demonstration with Movie references
        System.out.println("\n\n=== POLYMORPHISM IN ACTION ===");
        System.out.println("All variables are declared as 'Movie' type,");
        System.out.println("but at runtime they are all 'Adventure' objects.\n");

        Movie adventureAsMovie = new Adventure("Uncharted", "Ruben Fleischer", 2022, 116, "Adventure",
                                              "Treasure Hunt", "Nathan Drake", true);

        System.out.println("--- Movie 6 (Runtime type: Adventure) ---");
        System.out.println("Declared type: Movie");
        System.out.println("Runtime type: Adventure");
        System.out.println();
        adventureAsMovie.watchMovie();

        System.out.println("\n=== KEY INSIGHT ===");
        System.out.println("All variables are declared as 'Movie' type.");
        System.out.println("But at runtime, they are all 'Adventure' objects.");
        System.out.println("The watchMovie() method that executed is from the Adventure class (overridden method).");
        System.out.println("This is TRUE POLYMORPHISM - the method executed depends on the runtime type!");
        System.out.println("\nThis allows us to write flexible code that can work with Movie and all its subclasses.");

        // Can repeat creating different types of Movie classes such as ActionMovie, ComedyMovie, HorrorMovie, etc., and they can all be treated as Movie type while still exhibiting their specific behavior when methods are called - this is the power of polymorphism in Java!

    }
}
