package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

import java.util.Scanner;

/**
 * Different behavior execution of different types determined at runtime
 * PolymorphicFactorys demonstrates the Factory pattern using polymorphism with interactive user input.
 * The factory method getMovie() creates different types of Movie objects
 * based on the type parameter, without requiring the client to know the specific class types.
 * This demonstrates loose coupling and flexibility in object creation.
 *
 * Interactive features:
 * - Users can input movie types and titles via Scanner
 * - Supports multiple movie creations in a loop
 * - Shows supported movie types
 */
public class PolymorphicFactorys {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== INTERACTIVE FACTORY PATTERN WITH POLYMORPHISM ===\n");

        System.out.println("Supported Movie Types (by first letter):");
        System.out.println("  A - Adventure");
        System.out.println("  C - Comedy");
        System.out.println("  H - Horror");
        System.out.println("  S - Sci-Fi");
        System.out.println("  D - Drama");
        System.out.println("  * - Any other letter defaults to Movie\n");

        String continueInput = "yes";

        while (continueInput.equalsIgnoreCase("yes") || continueInput.equalsIgnoreCase("y")) {
            System.out.println("--- Creating a Movie ---");

            // Get movie type from user
            System.out.print("Enter movie type (Adventure, Comedy, Horror, Sci-Fi, Drama): ");
            String movieType = scanner.nextLine().trim();

            if (movieType.isEmpty()) {
                System.out.println("⚠️  Type cannot be empty. Please try again.\n");
                continue;
            }

            // Get movie title from user
            System.out.print("Enter movie title: ");
            String movieTitle = scanner.nextLine().trim();

            if (movieTitle.isEmpty()) {
                System.out.println("⚠️  Title cannot be empty. Please try again.\n");
                continue;
            }

            // Use factory method to create the movie
            Movie movie = Movie.getMovie(movieType, movieTitle);

            Adventure jaws = (Adventure) Movie.getMovie("A", "Jaws");
            jaws.watchMovie();

            // if assign object to reference of different type - passing C for Comedy type - Will get runtime class caste exception error
            // If don't o
            Object comedy = Movie.getMovie("C", "Airplane!");
            // comedy.watchMovie(); // compile error - Object class doesn't have watchMovie method
            Movie comedyMovie = Movie.getMovie("C", "Airplane!");
            // need add method for each subclass for watchMovie - if want to call watchMovie on comedyMovie reference - need to cast to Movie type - comedyMovie.watchMovie() - works because comedyMovie is declared as Movie type, but runtime type is Comedy (which is a subclass of Movie) - this is polymorphism in action

            // using references that are too generic like Object make you do too much casting
//            Movie comedyMovie = (Movie) comedy; // need to caste to specific type comedy
//            Comedy comedyMovie = (Comedy) comedy;
//            comedyMovie.watchComedy();

            var airplane = Movie.getMovie("C", "Airplane!");
            airplane.watchMovie(); // works because watchMovie is defined in Movie class and comedyMovie is

            // local variable type inference was introduced with java 10 - reduce boiler plate and make code readable
            // local variable type inference - can't be used in field declarations or method signatures, either as parameter type or return type
            // can't be used without an assignment because the type can't be inferred in that case
            // It can't be assigned a null literal, again, because a type can't be inferred in that case


            // compile time type as declared type - as var reference, method return type or method parameter, we don't explicitly declare a type for a compiled reference type. Instead it gets inferred by the compiler, but byte code
            // generated is same if we had declared the type
            
            
            

            // In LVTI, we don't explicitly declared a
            
            // compile time type for the reference, but at runtime, the actual type of the object is determined by the factory method and can be different for each movie created - this is polymorphism in action - we can have a list of movies created with the factory method and call watchMovie() on each movie without needing to know the specific type of movie at compile time - the actual method that gets executed depends on the runtime type of the movie object
            // compile time type is declared type to left of assignment operator
            // What is returned on right side of assignment operator from whatever expression or method is executed sometimes can only be determined at runtime
            
            // code is executing conditionaly 
            
            // you can assign a runtime instance to different compile time types - different because of polymorphism - you can assign a runtime instance of a subclass to a compile time reference of its superclass - this is polymorphism in action - the actual method that gets executed depends on the runtime type of the object, not the compile time type of the reference variable
            // It's more generetic -ach type was able to execute behavior unique to the class

            // We saw that those two lines of code, using a single compile time type of Movie, actually supported four different runtime types,

            // Each type was able to execute behavior unique to the class, and we didn't have to change any of the client code that was using the Movie type to support those different types - this is the power of polymorphism in Java!

            /// Rules: inheritance rule
            // You can assign an instance to a variable of the same type, or a parent type or a parent's parent type, java.lang.Object - the ultimate base class


            // Testing runtime object types - instanceof operator - checks if an object is an instance of a specific class or interface - returns true if the object is an instance of the specified type (class or subclass or interface) and false otherwise
            // Can always assign without casting if parent of base type assigning

            // determine runtime type - code - determine class name of object at runtime - getClass() method - returns the runtime class of the object - can use getSimpleName() to get just the class name without package - this is useful for debugging and logging to see what type of object we are working with at runtime

            var plan = new Adventure("Plan", "Director", 2026, 120, "Adventure",
                    "Quest", "Hero", true);
            System.out.println("Runtime class of plan object: " + plan.getClass().getName());
            System.out.println("Runtime class simple name of plan object: " + plan.getClass().getSimpleName());

            // Create an unknown object using factory method and get its class name
            System.out.println("\n--- DEMONSTRATING UNKNOWN OBJECT CLASS DETECTION ---");
            Object unknownObject = Movie.getMovie(movieType, movieTitle);

            // Get class information from the unknown object
            String unknownObjectClassName = unknownObject.getClass().getSimpleName();
            String unknownObjectFullName = unknownObject.getClass().getName();
            String unknownObjectPackage = unknownObject.getClass().getPackageName();

            System.out.println("Unknown Object Created by Factory Method:");
            System.out.println("  Simple Class Name: " + unknownObjectClassName);
            System.out.println("  Full Class Name: " + unknownObjectFullName);
            System.out.println("  Package Name: " + unknownObjectPackage);

            // METHOD 1: Testing class type WITHOUT instanceof using getClass()
            System.out.println("\n--- METHOD 1: CLASS TYPE TESTING WITHOUT instanceof ---");
            if (unknownObject.getClass().getSimpleName().equals("Adventure")) {
                System.out.println("✓ The object is an Adventure movie (detected using getClass())");
                Adventure adventureMovie = (Adventure) unknownObject;
                adventureMovie.watchMovie();
            } else if (unknownObject.getClass().getSimpleName().equals("Comedy")) {
                System.out.println("✓ The object is a Comedy movie (detected using getClass())");
                Movie comedysMovie = (Movie) unknownObject;
                comedysMovie.watchMovie();
            } else if (unknownObject.getClass().getSimpleName().equals("Horror")) {
                System.out.println("✓ The object is a Horror movie (detected using getClass())");
                Movie horrorMovie = (Movie) unknownObject;
                horrorMovie.watchMovie();
            } else {
                System.out.println("✓ The object is a generic Movie type (detected using getClass())");
            }

            // METHOD 2: Testing class type WITH instanceof operator (Traditional approach)
            System.out.println("\n--- METHOD 2: CLASS TYPE TESTING WITH instanceof (Traditional) ---");
            if (unknownObject instanceof Adventure) {
                System.out.println("✓ The object is an Adventure movie (detected using instanceof)");
                Adventure adventureMovie = (Adventure) unknownObject;
                adventureMovie.watchMovie();
            } else if (unknownObject instanceof Movie) {
                System.out.println("✓ The object is a Comedy movie (detected using instanceof)");
                Movie comedysMovieInstanceOf = (Movie) unknownObject;
                comedysMovieInstanceOf.watchMovie();
            } else if (unknownObject instanceof Movie) {
                System.out.println("✓ The object is a Horror movie (detected using instanceof)");
                Movie horrorMovie = (Movie) unknownObject;
                horrorMovie.watchMovie();
            } else if (unknownObject instanceof Movie) {
                System.out.println("✓ The object is a Movie type (detected using instanceof)");
            }

            // METHOD 3: Testing class type WITH Pattern Matching for instanceof (Java 16+)
            System.out.println("\n--- METHOD 3: CLASS TYPE TESTING WITH Pattern Matching instanceof (Java 16+) ---");
            if (unknownObject instanceof Adventure adventure) {
                System.out.println("✓ The object is an Adventure movie (detected using pattern matching)");
                System.out.println("  Adventure variable 'adventure' is now available for use");
                adventure.watchMovie();
            } else if (unknownObject instanceof Movie comedyInstanceOf) {
                System.out.println("✓ The object is a Comedy movie (detected using pattern matching)");
                System.out.println("  Comedy variable 'comedy' is now available for use");
                comedyInstanceOf.watchMovie();
            } else if (unknownObject instanceof Movie horror) {
                System.out.println("✓ The object is a Horror movie (detected using pattern matching)");
                System.out.println("  Horror variable 'horror' is now available for use");
                horror.watchMovie();
            } else if (unknownObject instanceof Movie simpleMovie) {
                System.out.println("✓ The object is a Movie type (detected using pattern matching)");
                System.out.println("  Movie variable 'simpleMovie' is now available for use");
                simpleMovie.watchMovie();
            }

            System.out.println("\n✓ Created: " + movie.getClass().getSimpleName());
            System.out.println("✓ First character of type used: '" + movieType.charAt(0) + "'");

            // Watch the movie (calls the appropriate overridden watchMovie method)
            movie.watchMovie();

            System.out.println("\n");

            // Ask if user wants to create another movie
            System.out.print("Do you want to create another movie? (yes/no): ");
            continueInput = scanner.nextLine().trim();

            System.out.println();
        }

        System.out.println("=== FACTORY PATTERN BENEFITS ===");
        System.out.println("✓ Client code doesn't need to know specific Movie subclasses");
        System.out.println("✓ Factory method handles object creation logic");
        System.out.println("✓ Easy to add new movie types without changing client code");
        System.out.println("✓ Switch statement maps type to appropriate Movie class");
        System.out.println("✓ Demonstrates polymorphism - same Movie reference, different runtime types");
        System.out.println("✓ Interactive input allows dynamic object creation\n");

        System.out.println("Thank you for using the Movie Factory!");
        scanner.close();
    }
}
