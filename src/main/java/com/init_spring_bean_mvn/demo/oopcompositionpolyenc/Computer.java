package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

public class Computer {

    public static void main(String[] args) {
        // Example of COMPOSITION in OOP
        // personalComputer, monitor, motherboard, and computerCase are all components of a computer
        // This demonstrates composition where a computer is composed of several independent components

        System.out.println("=== COMPOSITION PATTERN DEMONSTRATION ===\n");

        // Step 1: Create individual components separately
        Monitor monitor = new Monitor("Dell", "U2723DE", 27, "4K");
        Motherboard motherboard = new Motherboard("ASUS", "ROG MAXIMUS Z790", "Z790", "LGA1700");
        ComputerCase computerCase = new ComputerCase("NZXT", "H510 Flow", "Mid Tower", "ATX");

        // Step 2: Assemble the components into a PersonalComputer
        // This shows composition - combining multiple classes into a single coherent object
        PersonalComputer gamingPC = new PersonalComputer(
            "AMD",
            "Ryzen 9 5900X",
            32,
            "1TB NVMe SSD",
            monitor,
            motherboard,
            computerCase
        );

        // Step 3: Access the PersonalComputer and its composed components
        System.out.println("Gaming PC Details:");
        System.out.println("CPU Manufacturer: " + gamingPC.getManufacturer());
        System.out.println("CPU Model: " + gamingPC.getModel());
//        System.out.println("RAM: " + gamingPC.getRam() + " GB");
//        System.out.println("Storage: " + gamingPC.getStorage());

        System.out.println("\n--- Composed Components ---");

        // Access Monitor component through composition
        // Monitor pcMonitor = gamingPC.getMonitor();
//        System.out.println("\nMonitor Component:");
//        System.out.println("  Manufacturer: " + pcMonitor.getManufacturer());
//        System.out.println("  Model: " + pcMonitor.getModel());
//        System.out.println("  Screen Size: " + pcMonitor.getScreenSize() + " inches");
//        System.out.println("  Resolution: " + pcMonitor.getResolution());

        // Access Motherboard component through composition
//        Motherboard pcMotherboard = gamingPC.getMotherboard();
//        System.out.println("\nMotherboard Component:");
//        System.out.println("  Manufacturer: " + pcMotherboard.getManufacturer());
//        System.out.println("  Model: " + pcMotherboard.getModel());
//        System.out.println("  Chipset: " + pcMotherboard.getChipset());
//        System.out.println("  Socket Type: " + pcMotherboard.getSocketType());

        // Access ComputerCase component through composition
//        ComputerCase pcCase = gamingPC.getComputerCase();
//        System.out.println("\nComputer Case Component:");
//        System.out.println("  Manufacturer: " + pcCase.getManufacturer());
//        System.out.println("  Model: " + pcCase.getModel());
//        System.out.println("  Case Style: " + pcCase.getCaseStyle());
//        System.out.println("  Form Factor: " + pcCase.getFormFactor());
//
//        System.out.println("\n--- Full Computer Object ---");
//        System.out.println(gamingPC);

        // Step 4: Call the drawLogo() method to display visual representation
        System.out.println("\n--- Visual Representation ---");
        gamingPC.drawLogo();

        // This demonstrates how composition allows us to build complex objects from simpler ones
        // while maintaining clear separation of concerns and modularity in our code.
        // Composition is fundamentally different from inheritance:
        // - Inheritance: IS-A relationship (a PersonalComputer IS-A Product)
        // - Composition: HAS-A relationship (a PersonalComputer HAS-A Monitor, Motherboard, ComputerCase)
        // We pass objects as constructor parameters, simulating the assembly of a computer system.
        // By using public methods like drawLogo(), we expose functionality while hiding implementation details.

        // personal computer didn't need to know how to create a monitor, motherboard, or case - it just needs to know how to use them (e.g., through methods like drawLogo())
        // Creating a main object and object within objects - Personal Computer

        //IMPORTANT - Use composition before implementing inheritance. Inheritance should only be used when there is a clear IS-A relationship, while composition can be used to build complex objects from simpler ones without forcing an unnatural hierarchy.

        // All parts were able to inherity a set of attributes
        // Calling code didn't need to know anything about the parts - powerUp, drawLogo

        // Composition - more flexible - add parts in or remove and less lidkely to have downlastream effect,
        // - provides functional reuse outside of class hierarchy,

        // javas inheritance breaks encapsulation because subclasses may need direct access to a parents state or behavior
        // INHERITANCE - less flexible, adding class or removing may impact class hierachy
        // subclass may not need all fucntionaly or attributes of parent

        //      example : digital Product - version, releaseDate, runProgram(); digital product wouldn't have width, height and depth
        //  Instead use composition to include on certain products but not all

        //      Create Dimensions class --- > width, height, depth remove from product - add attribute to Motherboard dimensions: Dimensions
        //          - Allow for future enhancements like addition of subclass Digital Product without casugin problemsm for existing code
        //          - apply attributes to specific products that need them without forcing all products to have them (e.g., monitor, case, but not digital product)

    }
}
