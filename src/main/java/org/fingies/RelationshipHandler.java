package org.fingies;

import java.util.ArrayList;
import java.util.List;

/**
 * Relationship Handler class to add, delete and get relationships
 * @author Nick Hayes & Lincoln Craddock
 */
public class RelationshipHandler
{
    // List so that classes can be modified while remaining in the container
    private static List<Relationship> relationships = new ArrayList<Relationship>();
    
    // Gets the index of a relationship in 'relationships', given the src & dest strings
    private static int indexOf(String src, String dest)
    {
    	UMLClass source = UMLClassHandler.getClass(src);
    	UMLClass destination = UMLClassHandler.getClass(dest);

    	Relationship r = new Relationship(source, destination, RelationshipType.DEFAULT);
    	return relationships.indexOf(r);
    }

    /**
     * Removes all relationships with a src or dest equal to the classname
     * @param className name of class to remove relationships of
     */
    public static void removeAllRelationshipsForClassname(String className)
    {
        for (int i = 0; i < relationships.size(); i++)
        {
            if (relationships.get(i).getSrc().getName().equals(className) || relationships.get(i).getDest().getName().equals(className))
            {
                relationships.remove(i);
                i--;
            }
        }
    }

    /**
     * Adds a new relationship to the list of relationships in the diagram.
     * 
     * @param src The name of the source class
     * @param dest The name of the destination class
     * @throws IllegalArgumentException when trying to add a relationship that already exists
     */
    public static boolean addRelationship(String src, String dest, RelationshipType type)
    {
    	UMLClass source = UMLClassHandler.getClass(src);
    	UMLClass destination = UMLClassHandler.getClass(dest);
    	
    	Relationship r = new Relationship (source, destination, type);
        if(relationships.contains(r))
        {
            throw new IllegalArgumentException("This relationship already exists.");
        }
        return relationships.add(r);
    }
    
    /**
     * Removes a relationship from the list of relationhsips in the diagram.
     * 
     * @param src The name of the source class
     * @param dest The name of the destination class
     * 
     * @throws IllegalArgumentException when trying to add a relationship that already exists
     */
    public static boolean removeRelationship(String src, String dest)
    {
        int i = indexOf(src, dest);
        if(i == -1)
        {
        	throw new IllegalArgumentException("This relationship does not exist");
        }
        return relationships.remove(i) != null;
    }
    
    /**
     * Changes the type of a relationship to a new one.
     * 
     * @param src The source of the relationship to change
     * @param dest The destination of the relationship to change
     * @param newType The new type to give the relationship
     * 
     * @throws IllegalArgumentException when trying to add a relationship that already exists
     */
    public static boolean changeRelationshipType(String src, String dest, RelationshipType newType)
    {
    	int i = indexOf(src, dest);
    	if (i == -1)
    	{
    		throw new IllegalArgumentException("This relationship does not exist");
    	}
    	relationships.get(i).setType(newType);
        return relationships.get(i).getType().equals(newType);
    }
    
    /**
     * Returns a string listing all of the relationships in the diagram.
     * 
     * @return A string list of all of the relationships.
     */
    public static String listRelationships()
    {
    	String lst = "";
    	for (Relationship r : relationships)
    		lst += r + "\n";
    	lst = lst.substring(0, lst.length() - 1); // trims the remaining \n
    	return lst;
    }
    
    public static List<Relationship> getRelationships()
    {
        return relationships;
    }
    
    /**
     * Provides the list of all relation objects
     * @return A list of relationship objects
     * @author trush
     */
    public static List<Relationship> getRelationObjects() 
    {
        return relationships;
    }

    /**
     * Resets all relationships
     * @author trush
     */
    public static void reset() {
        relationships = new ArrayList<Relationship>();
    }
}