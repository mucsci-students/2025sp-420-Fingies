package org.fingies.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Relationship Handler class to add, delete and get relationships
 * 
 * @author Nick Hayes & Lincoln Craddock
 */
public class RelationshipHandler {
    // List so that classes can be modified while remaining in the container
    private static List<Relationship> relationships = new ArrayList<Relationship>();

    // Gets the index of a relationship in 'relationships', given the src & dest
    // strings
    private static int indexOf(String src, String dest) {
        UMLClassHandler.getClass(src);
        UMLClassHandler.getClass(dest);

        // Iterate through relationships to find index of relationship (Objects.equals
        // to handle null values)
        for (int i = 0; i < relationships.size(); i++) {
            Relationship r = relationships.get(i);
            if (r.getSrc().getName().equals(src) && r.getDest().getName().equals(dest)) {
                return i; // Return index as soon as found
            }
        }
        return -1;
    }

    /**
     * Removes all relationships with a src or dest equal to the classname
     * 
     * @param className Name of class to remove relationships of
     */
    public static void removeAllRelationshipsForClassname(String className) {
        for (int i = 0; i < relationships.size(); i++) {
            if (relationships.get(i).getSrc().getName().equals(className)
                    || relationships.get(i).getDest().getName().equals(className)) {
                relationships.remove(i);
                i--;
            }
        }
    }

    /**
     * Gets all the relationships with a src or dest equal to the classname
     * 
     * @param className Name of class to get the relationships of
     * @return A list of all of the relationships associated with the class
     */
    public static List<Relationship> getAllRelationshipsForClassname(String className) {
        List<Relationship> list = new ArrayList<Relationship>();

        for (int i = 0; i < relationships.size(); i++)
            if (relationships.get(i).getSrc().getName().equals(className)
                    || relationships.get(i).getDest().getName().equals(className))
                list.add(relationships.get(i));

        return list;
    }

    /**
     * Adds a new relationship to the list of relationships in the diagram.
     * 
     * @param src  The name of the source class
     * @param dest The name of the destination class
     * @throws IllegalArgumentException when trying to add a relationship that
     *                                  already exists
     */
    public static boolean addRelationship(String src, String dest, RelationshipType type) {
        UMLClass source = UMLClassHandler.getClass(src);
        UMLClass destination = UMLClassHandler.getClass(dest);

        Relationship r = new Relationship(source, destination, type);
        if (relationships.contains(r)) {
            throw new IllegalArgumentException("This relationship already exists");
        }
        return relationships.add(r);
    }

    /**
     * Removes a relationship from the list of relationhsips in the diagram.
     * 
     * @param src  The name of the source class
     * @param dest The name of the destination class
     * @return true if the relationship was removed, false otherwise
     * @throws IllegalArgumentException when trying to add a relationship that
     *                                  already exists
     */
    public static void removeRelationship(String src, String dest) {
        int i = indexOf(src, dest);
        if (i == -1) {
            throw new IllegalArgumentException("This relationship does not exist");
        }
        relationships.remove(i);
    }

    /**
     * Changes the type of a relationship to a new one.
     * 
     * @param src     The source of the relationship to change
     * @param dest    The destination of the relationship to change
     * @param newType The new type to give the relationship
     * @return true if the relationship type was changed, false otherwise
     * @throws IllegalArgumentException when trying to add a relationship that
     *                                  already exists
     */
    public static boolean changeRelationshipType(String src, String dest, RelationshipType newType) {
        int i = indexOf(src, dest);
        if (i == -1) {
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
    public static String listRelationships() {
        if (relationships.size() != 0) {
            String lst = "";
            for (Relationship r : relationships)
                lst += r + " (" + r.getType().getName() + ")\n";
            lst = lst.substring(0, lst.length() - 1); // trims the remaining \n
            return lst;
        } else {
            return "There are no current relationships";
        }
    }

    /**
     * Checks whether a relationship exists given a src and dest
     * 
     * @param src  src class name
     * @param dest dest class name
     * @return true if the relationship exists, false otherwise
     */
    public static boolean exists(String src, String dest) {
        return indexOf(src, dest) != -1;
    }

    /**
     * Provides the list of all relation objects
     * 
     * @return A list of relationship objects
     * @author trush
     */
    public static List<Relationship> getRelationObjects() {
        return relationships;
    }

    /**
     * Resets all relationships
     * 
     * @author trush
     */
    public static void reset() {
        relationships = new ArrayList<Relationship>();
    }

    /**
     * Replaces a class object with another object in each relationship associated
     * with the class.
     * 
     * @param class1 The class to replace in each relationship.
     * @param class2 The class to replace class1 with.
     */
    public static void replace(UMLClass class1, UMLClass class2) {
        if (class1 == null)
            return;
        else if (class2 == null) {
            removeAllRelationshipsForClassname(class1.getName());
        } else {
            for (Relationship relationship : relationships) {
                if (relationship.getSrc().getName().equals(class1.getName()))
                    relationship.setSrc(class2);

                if (relationship.getDest().getName().equals(class1.getName()))
                    relationship.setDest(class2);
            }
        }
    }

    /**
     * Replaces all of the relationships associated with a particular class with a
     * different list of relationships.
     * 
     * @param classname The name of the class associated with all of the
     *                  relationships to replace.
     * @param list      A list of new relationships to add to the diagram.
     */
    public static void replaceAllRelationshipsForClassname(String classname, List<Relationship> list) {
        relationships.removeIf(new Predicate<Relationship>() {

            @Override
            public boolean test(Relationship t) {
                return t.getSrc().getName().equals(classname) || t.getDest().getName().equals(classname);
            }
        });

        relationships.addAll(list);
    }
}