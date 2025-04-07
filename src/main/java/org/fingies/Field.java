package org.fingies;

import java.util.Objects;

public class Field extends Attribute {
    private String name;
    private String type;

    public Field (String name, String type)
    {
        validateCharacters(name);
        validateCharacters(type);
        this.name = name;
        this.type = type;
    }
    
    /**
     * Copy ctor
     * 
     * @param field The field to copy the name & type of
     */
    public Field (Field field)
    {
    	this.name = field.name;
    	this.type = field.type;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void renameAttribute(String name) {
        this.name = name;
    }

    /**
     * Gets the data type of the field
     * @return the data type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the data type of the field
     * @param type the data type to set the field to.
     */
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName()); // Hash based on name
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) 
            return true;
        if (obj == null || getClass() != obj.getClass()) 
            return false;
        Field field = (Field) obj;
        return getName().equals(field.getName()); // Compare by name
    }

    @Override
    public String toString ()
    {
        return type + " " + name;
    }
}
