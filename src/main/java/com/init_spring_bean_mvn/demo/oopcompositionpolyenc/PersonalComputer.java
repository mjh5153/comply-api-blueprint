package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * PersonalComputer class representing a complete personal computer system.
 * Inherits from Product and demonstrates composition pattern by aggregating multiple components.
 * A PersonalComputer is composed of: Monitor, Motherboard, and ComputerCase.
 * This class demonstrates both inheritance and composition in object-oriented programming.
 */
public class PersonalComputer extends Product {
    private int ram; // in GB
    private String storage; // storage specification (e.g., "512GB SSD")
    private Monitor _monitor; // composed Monitor component
    private Motherboard _motherboard; // composed Motherboard component
    private ComputerCase _computerCase; // composed ComputerCase component

    /**
     * Default constructor for PersonalComputer class.
     * Required for object instantiation without parameters.
     */
    public PersonalComputer() {
        super();
    }

    /**
     * Constructor to initialize a PersonalComputer with manufacturer, RAM, and storage.
     *
     * @param manufacturer The CPU manufacturer (e.g., "Intel", "AMD")
     * @param ram RAM capacity in GB
     * @param storage Storage specification (e.g., "512GB SSD")
     */
    public PersonalComputer(String manufacturer, int ram, String storage) {
        super(manufacturer, "Default CPU Model");
        this.ram = ram;
        this.storage = storage;
    }

    /**
     * Constructor to initialize a PersonalComputer with all details including model.
     *
     * @param manufacturer The CPU manufacturer
     * @param model The CPU model identifier
     * @param ram RAM capacity in GB
     * @param storage Storage specification
     */
    public PersonalComputer(String manufacturer, String model, int ram, String storage) {
        super(manufacturer, model);
        this.ram = ram;
        this.storage = storage;
    }

    /**
     * Constructor to initialize a PersonalComputer with all components (composition pattern).
     * Creates a complete personal computer with monitor, motherboard, and case.
     *
     * @param manufacturer The CPU manufacturer
     * @param model The CPU model identifier
     * @param ram RAM capacity in GB
     * @param storage Storage specification
     * @param monitor The Monitor component
     * @param motherboard The Motherboard component
     * @param computerCase The ComputerCase component
     */
    public PersonalComputer(String manufacturer, String model, int ram, String storage,
                           Monitor monitor, Motherboard motherboard, ComputerCase computerCase) {
        super(manufacturer, model);
        this.ram = ram;
        this.storage = storage;
        this._monitor = monitor;
        this._motherboard = motherboard;
        this._computerCase = computerCase;
    }


    /**
     * Sets the RAM capacity of the personal computer.
     *
     * @param ram RAM capacity in GB to set
     */
    public void setRam(int ram) {
        this.ram = ram;
    }


    /**
     * Sets the storage specification of the personal computer.
     *
     * @param storage Storage specification to set
     */
    public void setStorage(String storage) {
        this.storage = storage;
    }


    /**
     * Sets the Monitor component of the personal computer.
     *
     * @param monitor The Monitor component to set
     */
    public void setMonitor(Monitor monitor) {
        this._monitor = monitor;
    }


    /**
     * Sets the Motherboard component of the personal computer.
     *
     * @param motherboard The Motherboard component to set
     */
    public void setMotherboard(Motherboard motherboard) {
        this._motherboard = motherboard;
    }


    /**
     * Sets the ComputerCase component of the personal computer.
     *
     * @param computerCase The ComputerCase component to set
     */
    public void setComputerCase(ComputerCase computerCase) {
        this._computerCase = computerCase;
    }

    /**
     * Draws an ASCII art logo/representation of the personal computer.
     * This method demonstrates a public interface for displaying computer information.
     * Also calls drawPixelAt on the Monitor component to demonstrate composition in action.
     */
    public void drawLogo() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║     PERSONAL COMPUTER SYSTEM          ║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  CPU: " + String.format("%-30s", getManufacturer() + " " + getModel()) + "║");
        System.out.println("║  RAM: " + String.format("%-30s", ram + " GB") + "║");
        System.out.println("║  Storage: " + String.format("%-26s", storage) + "║");
        System.out.println("╠════════════════════════════════════════╣");
        System.out.println("║  COMPONENTS:                           ║");
        System.out.println("║  ┌──────────────────────────────────┐  ║");
        System.out.println("║  │ Monitor: " + String.format("%-23s", (_monitor != null ? _monitor.getManufacturer() + " " + _monitor.getModel() : "N/A")) + "│  ║");
        System.out.println("║  │ Motherboard: " + String.format("%-19s", (_motherboard != null ? _motherboard.getManufacturer() : "N/A")) + "│  ║");
        System.out.println("║  │ Case: " + String.format("%-26s", (_computerCase != null ? _computerCase.getManufacturer() : "N/A")) + "│  ║");
        System.out.println("║  └──────────────────────────────────┘  ║");
        System.out.println("╚════════════════════════════════════════╝\n");

        // Call drawPixelAt on the Monitor component to demonstrate composition in action
        if (_monitor != null) {
            System.out.println("Rendering pixels on monitor...");
            _monitor.drawPixelAt(0, 0, "RED");
            _monitor.drawPixelAt(100, 50, "GREEN");
            _monitor.drawPixelAt(200, 100, "BLUE");
            _monitor.drawPixelAt(150, 150, "WHITE");
            System.out.println();
        }
    }

    /**
     * Provides a string representation of the PersonalComputer.
     * Includes parent class information, CPU specifications, and all composed components.
     *
     * @return A string representation with manufacturer, model, RAM, storage, and all components
     */
    @Override
    public String toString() {
        return "PersonalComputer{" +
                "manufacturer='" + getManufacturer() + '\'' +
                ", model='" + getModel() + '\'' +
                ", ram=" + ram +
                ", storage='" + storage + '\'' +
                ", monitor=" + _monitor +
                ", motherboard=" + _motherboard +
                ", computerCase=" + _computerCase +
                '}';
    }
}



