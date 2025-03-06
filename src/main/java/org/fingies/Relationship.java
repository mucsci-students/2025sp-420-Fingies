package org.fingies;

import java.util.Objects;

/**
 * A class to create a relationship between two other classes
 * @author Nick Hayes & Lincoln Craddock
 */
public class Relationship 
{	
    private UMLClass src;
    private UMLClass dest;
    
    private RelationshipType type;

    /**
     * Relationship constructor
     * 
     * @param src The source of the relationship
     * @param dest The destination of the relationship
     * @param type The relationship type
     */
    public Relationship(UMLClass src, UMLClass dest, RelationshipType type)
    {
        this.src = src;
        this.dest = dest;
        this.type = type;
    }

    /**
     * Getter method for the src field
     * 
     * @return The src of the object called upon
     */
    public UMLClass getSrc()
    {
        return this.src;
    }

    /**
     * Getter method for the dest field
     * 
     * @return The dest of the object called upon
     */
    public UMLClass getDest()
    {
        return this.dest;
    }
    
    /**
     * Getter for the type of the relationship
     * 
     * @return The type of this relationship
     */
    public RelationshipType getType()
    {
    	return type;
    }
    
    /**
     * Setter for the type of this relationship
     * 
     * @param newType The new type to give this relationship
     */
    public void setType(RelationshipType newType)
    {
    	type = newType;
    }

    // Doesn't check the relationship type, only checks the source & destination relationships
    @Override
    public boolean equals(Object obj)
    {
        if(obj != null && obj.getClass() == this.getClass())
        {
            Relationship r = (Relationship)obj;
            if(this.src.getName().equals(r.src.getName()) && this.dest.getName().equals(r.dest.getName()))
            {
                return true;
            }
        }
        return false;
    }

    @Override 
    public String toString()
    {
        return this.src.getName() + " " + type + " " + this.dest.getName();
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.src.hashCode(), this.dest.hashCode());
    }
}    