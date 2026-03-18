package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Motherboard class representing a motherboard component.
 * Inherits from Product and adds motherboard-specific properties like chipset and socket type.
 * This class demonstrates inheritance and extending functionality in OOP.
 */
public class Motherboard extends Product {
    private String chipset; // chipset specification (e.g., "Z490", "B450")
    private String socketType; // socket type (e.g., "LGA1200", "AM4")

    /**
     * Default constructor for Motherboard class.
     * Required for object instantiation without parameters.
     */
    public Motherboard() {
        super();
    }

    /**
     * Constructor to initialize a Motherboard with manufacturer, chipset, and socket type.
     *
     * @param manufacturer The motherboard manufacturer (e.g., "ASUS", "MSI", "Gigabyte")
     * @param chipset Chipset specification (e.g., "Z490", "B450")
     * @param socketType Socket type (e.g., "LGA1200", "AM4")
     */
    public Motherboard(String manufacturer, String chipset, String socketType) {
        super(manufacturer, "Default Motherboard Model");
        this.chipset = chipset;
        this.socketType = socketType;
    }

    /**
     * Constructor to initialize a Motherboard with all details including model.
     *
     * @param manufacturer The motherboard manufacturer
     * @param model The motherboard model identifier
     * @param chipset Chipset specification
     * @param socketType Socket type
     */
    public Motherboard(String manufacturer, String model, String chipset, String socketType) {
        super(manufacturer, model);
        this.chipset = chipset;
        this.socketType = socketType;
    }

    /**
     * Gets the chipset of the motherboard.
     *
     * @return Chipset specification
     */
    public String getChipset() {
        return chipset;
    }

    /**
     * Sets the chipset of the motherboard.
     *
     * @param chipset Chipset specification to set
     */
    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    /**
     * Gets the socket type of the motherboard.
     *
     * @return Socket type
     */
    public String getSocketType() {
        return socketType;
    }

    /**
     * Sets the socket type of the motherboard.
     *
     * @param socketType Socket type to set
     */
    public void setSocketType(String socketType) {
        this.socketType = socketType;
    }

    /**
     * Provides a string representation of the Motherboard.
     * Includes parent class information plus Motherboard-specific details.
     *
     * @return A string representation with manufacturer, model, chipset, and socket type
     */
    @Override
    public String toString() {
        return "Motherboard{" +
                "manufacturer='" + getManufacturer() + '\'' +
                ", model='" + getModel() + '\'' +
                ", chipset='" + chipset + '\'' +
                ", socketType='" + socketType + '\'' +
                '}';
    }
}

