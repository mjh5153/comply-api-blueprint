package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;

/**
 * Monitor class representing a display monitor component.
 * Inherits from Product and adds monitor-specific properties like screen size and resolution.
 * This class demonstrates inheritance and extending functionality in OOP.
 */
public class Monitor extends Product {
    private int screenSize; // in inches
    private String resolution; // resolution specification (e.g., "4K", "1080p")

    /**
     * Default constructor for Monitor class.
     * Required for object instantiation without parameters.
     */
    public Monitor() {
        super();
    }

    /**
     * Constructor to initialize a Monitor with manufacturer, screen size, and resolution.
     *
     * @param manufacturer The monitor manufacturer (e.g., "Dell", "LG", "ASUS")
     * @param screenSize Screen size in inches
     * @param resolution Resolution specification (e.g., "4K", "1080p")
     */
    public Monitor(String manufacturer, int screenSize, String resolution) {
        super(manufacturer, "Default Monitor Model");
        this.screenSize = screenSize;
        this.resolution = resolution;
    }

    /**
     * Constructor to initialize a Monitor with all details including model.
     *
     * @param manufacturer The monitor manufacturer
     * @param model The monitor model identifier
     * @param screenSize Screen size in inches
     * @param resolution Resolution specification
     */
    public Monitor(String manufacturer, String model, int screenSize, String resolution) {
        super(manufacturer, model);
        this.screenSize = screenSize;
        this.resolution = resolution;
    }

    /**
     * Gets the screen size of the monitor.
     *
     * @return Screen size in inches
     */
    public int getScreenSize() {
        return screenSize;
    }

    /**
     * Sets the screen size of the monitor.
     *
     * @param screenSize Screen size in inches to set
     */
    public void setScreenSize(int screenSize) {
        this.screenSize = screenSize;
    }

    /**
     * Gets the resolution of the monitor.
     *
     * @return Resolution specification
     */
    public String getResolution() {
        return resolution;
    }

    /**
     * Sets the resolution of the monitor.
     *
     * @param resolution Resolution specification to set
     */
    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    /**
     * Draws a pixel at the specified coordinates on the monitor display.
     * This method simulates drawing a pixel on the monitor screen.
     *
     * @param x The x-coordinate of the pixel
     * @param y The y-coordinate of the pixel
     * @param color The color of the pixel (e.g., "RED", "GREEN", "BLUE", "WHITE")
     */
    public void drawPixelAt(int x, int y, String color) {
        System.out.println("  [Monitor] Drawing pixel at (" + x + ", " + y + ") with color: " + color +
                          " on " + this.getManufacturer() + " " + this.getModel() +
                          " (" + screenSize + "\" " + resolution + ")");
    }

    /**
     * Provides a string representation of the Monitor.
     * Includes parent class information plus Monitor-specific details.
     *
     * @return A string representation with manufacturer, model, screen size, and resolution
     */
    @Override
    public String toString() {
        return "Monitor{" +
                "manufacturer='" + getManufacturer() + '\'' +
                ", model='" + getModel() + '\'' +
                ", screenSize=" + screenSize +
                ", resolution='" + resolution + '\'' +
                '}';
    }
}

