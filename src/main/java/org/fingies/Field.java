package org.fingies;

import java.util.Objects;

public class Field extends Attribute {
    private String name;

    public Field (String name)
    {
        validateCharacters(name);
        this.name = name;
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
    public int hashCode() {
        return Objects.hash(getName()); // Hash based on name
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void renameAttribute(String name) {
        this.name = name;
    }
}
