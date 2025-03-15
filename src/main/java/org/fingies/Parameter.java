package org.fingies;

import java.util.Objects;

/**
 * A representation of a parameter of a method in a UML Class Diagram
 * @author Kevin Dichter, Lincoln Craddock, Tristan Rush
 */
public class Parameter {

    private String name;
    private String type;

    /**
     * Constructor for the parameter class
     * @param name the name of the parameter to add
     * @param type the data type of the parameter to add
     */
    public Parameter (String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    /**
     * Gets the name of the parameter   
     * @return the name of the parameter
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name of the parameter
     * @param name the name of the parameter
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * Gets the data type of the parameter
     * @return the data type of the parameter
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the data type for the parameter
     * @param type the data type to set for the parameter.
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Same object reference
        if (obj == null || getClass() != obj.getClass()) return false; // Different class

        Parameter parameter = (Parameter) obj;
        return getName().equals(parameter.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString()
    {
        return type + " " + name;
    }
}
