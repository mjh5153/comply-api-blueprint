package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Parent class representing a generic Product with common properties and methods.
 * This class serves as the base for all computer components (PersonalComputer, Monitor, Motherboard, ComputerCase).
 * Demonstrates inheritance and abstraction principles in object-oriented programming.
 */
public class Product {
    private String manufacturer;
    private String model;

    /**
     * Default constructor for Product class.
     * Required for object instantiation without parameters.
     */
    public Product() {
    }

    /**
     * Constructor to initialize a Product with manufacturer and model.
     *
     * @param manufacturer The name of the product manufacturer
     * @param model The model identifier or name
     */
    public Product(String manufacturer, String model) {
        this.manufacturer = manufacturer;
        this.model = model;
    }

    /**
     * Gets the manufacturer of the product.
     *
     * @return The manufacturer name
     */
    public String getManufacturer() {
        return manufacturer;
    }

    /**
     * Sets the manufacturer of the product.
     *
     * @param manufacturer The manufacturer name to set
     */
    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Gets the model of the product.
     *
     * @return The model identifier
     */
    public String getModel() {
        return model;
    }

    /**
     * Sets the model of the product.
     *
     * @param model The model identifier to set
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Provides a string representation of the Product.
     * Can be overridden by subclasses for more specific information.
     *
     * @return A string representation containing manufacturer and model
     */
    @Override
    public String toString() {
        return "Product{" +
                "manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}

