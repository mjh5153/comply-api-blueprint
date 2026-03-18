package com.init_spring_bean_mvn.demo.oopcompositionpolyenc;


/**
 * ComputerCase class representing a computer case/chassis component.
 * Inherits from Product and adds case-specific properties like case style and form factor.
 * This class demonstrates inheritance and extending functionality in OOP.
 */
public class ComputerCase extends Product {
    private String caseStyle; // case style (e.g., "Mid Tower", "Full Tower", "Mini ITX")
    private String formFactor; // form factor or type

    /**
     * Default constructor for ComputerCase class.
     * Required for object instantiation without parameters.
     */
    public ComputerCase() {
        super();
    }

    /**
     * Constructor to initialize a ComputerCase with manufacturer, case style, and form factor.
     *
     * @param manufacturer The case manufacturer (e.g., "NZXT", "Corsair", "Lian Li")
     * @param caseStyle Case style (e.g., "Mid Tower", "Full Tower")
     * @param formFactor Form factor or type
     */
    public ComputerCase(String manufacturer, String caseStyle, String formFactor) {
        super(manufacturer, "Default Case Model");
        this.caseStyle = caseStyle;
        this.formFactor = formFactor;
    }

    /**
     * Constructor to initialize a ComputerCase with all details including model.
     *
     * @param manufacturer The case manufacturer
     * @param model The case model identifier
     * @param caseStyle Case style
     * @param formFactor Form factor or type
     */
    public ComputerCase(String manufacturer, String model, String caseStyle, String formFactor) {
        super(manufacturer, model);
        this.caseStyle = caseStyle;
        this.formFactor = formFactor;
    }

    /**
     * Gets the case style of the computer case.
     *
     * @return Case style
     */
    public String getCaseStyle() {
        return caseStyle;
    }

    /**
     * Sets the case style of the computer case.
     *
     * @param caseStyle Case style to set
     */
    public void setCaseStyle(String caseStyle) {
        this.caseStyle = caseStyle;
    }

    /**
     * Gets the form factor of the computer case.
     *
     * @return Form factor
     */
    public String getFormFactor() {
        return formFactor;
    }

    /**
     * Sets the form factor of the computer case.
     *
     * @param formFactor Form factor to set
     */
    public void setFormFactor(String formFactor) {
        this.formFactor = formFactor;
    }

    /**
     * Provides a string representation of the ComputerCase.
     * Includes parent class information plus ComputerCase-specific details.
     *
     * @return A string representation with manufacturer, model, case style, and form factor
     */
    @Override
    public String toString() {
        return "ComputerCase{" +
                "manufacturer='" + getManufacturer() + '\'' +
                ", model='" + getModel() + '\'' +
                ", caseStyle='" + caseStyle + '\'' +
                ", formFactor='" + formFactor + '\'' +
                '}';
    }
}

