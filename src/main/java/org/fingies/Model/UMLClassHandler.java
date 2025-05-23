package org.fingies.Model;

import java.util.List;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Handles all interactions made to the classes
 * 
 * @author kdichter, Lincoln Craddock
 */
public class UMLClassHandler {

    /**
     * Map of Class Name to Class Object
     */
    private static HashMap<String, UMLClass> classes = new HashMap<>();

    /**
     * Attempts to create a new class
     * 
     * @param name name the user wants to give to the class
     * @return true if the class was created, false otherwise
     */
    public static boolean createClass(String name) {
        if (!classes.containsKey(name)) {
            UMLClass c = new UMLClass(name);
            classes.put(name, c);
            return true;
        }
        throw new IllegalArgumentException("Class " + name + " already exists");
    }

    /**
     * Attempts to add a UMLClass object to the list of classes
     * 
     * @param classObject The UMLClass to add to the list of UMLClasses
     * @return true if the class was added, false otherwise
     * @author trush
     */
    public static boolean addClassObject(UMLClass classObject) {
        if (!classes.containsKey(classObject.getName())) {
            classes.put(classObject.getName(), classObject);
            return true;
        }
        return false;
    }

    /**
     * Attempts to delete a class from the hashmap of classes
     * 
     * @param name name of the class that the user wants to remove
     * @return true if the class was deleted, false otherwise
     */
    public static void removeClass(String name) {
        if (!exists(name)) {
            throw new IllegalArgumentException("Class " + name + " does not exist");
        }
        classes.remove(name);
    }

    /**
     * Attempts to rename a class in the hashmap of classes
     * 
     * @param className name of the class to have its name changed
     * @param newName   name the user wants to change the class name to
     * @return true if the class name is changed, false otherwise
     */
    public static boolean renameClass(String className, String newName) {
        if (classes.containsKey(className)) {
            if (!classes.containsKey(newName)) {
                UMLClass c = classes.get(className);
                c.renameClass(newName);
                classes.remove(className);
                classes.put(newName, c);
                return true;
            } else
                throw new IllegalArgumentException("Another class already has the name " + newName);
        } else
            throw new IllegalArgumentException("Class " + className + " does not exist");
    }

    /**
     * Creates a hashset of all of the classes in the classes hashmap
     * 
     * @return a hashset of all of the classes in the classes hashmap
     */
    public static HashSet<UMLClass> getAllClasses() {
        HashSet<UMLClass> classesSet = new HashSet<UMLClass>();
        for (UMLClass c : classes.values()) {
            classesSet.add(c);
        }
        return classesSet;
    }

    /**
     * Gets a class given its name.
     * 
     * @param name The name of the class.
     * @return the class.
     * @throws IllegalArgumentException if the class does not exist
     */
    public static UMLClass getClass(String name) {
        if (exists(name)) {
            return classes.get(name);
        }
        throw new IllegalArgumentException("Class " + name + " does not exist");
    }

    /**
     * Checks whether a class exists given its name.
     * 
     * @param name True if the class exists.
     * @return the class.
     */
    public static boolean exists(String name) {
        return classes.containsKey(name);
    }

    /**
     * Returns a string that lists all of the classes & their attributes.
     * 
     * @return A string containing a list of classes & their attributes.
     */
    public static String listClasses() {
        if (classes.isEmpty())
            return "No current classes exist";

        String str = "";
        for (UMLClass c : classes.values()) {
            str += listClass(c) + "\n";
        }
        return str.substring(0, str.length() - 1); // trim off the extra \n
    }

    /**
     * Returns a string listing the attributes of a class.
     * 
     * @param className The name of the class to list the attributes for.
     * @return A string containing a list of attributes for a class.
     */
    public static String listClass(UMLClass c) {
        String str = c.getName();
        List<Field> fields = c.getFields();
        List<Method> methods = c.getMethods();

        if (fields.isEmpty() && methods.isEmpty())
            return str;

        // prints each field if the list of fields isn't empty
        if (!fields.isEmpty()) {
            str += "\n\t";
            for (Field field : fields) {
                str += field + "\n\t";
            }
            str = str.substring(0, str.length() - 2); // trim off the extra \n\t
        }

        // prints each method followed a list of it's parameters if the list of methods
        // isn't empty
        if (!methods.isEmpty()) {
            str += "\n\t";
            for (Method method : methods) {
                str += method + "\n\t";
            }
            str = str.substring(0, str.length() - 2); // trim off the extra \n\t
        }
        return str;
    }

    /**
     * Resets all classes and attributes
     * 
     * @author trush
     */
    public static void reset() {
        HashMap<String, UMLClass> reset = new HashMap<>();
        classes = reset;
    }

    /**
     * Replaces one UMLClass object with another one. Both parameters can be null.
     * 
     * @param class1 The old class to replace. If null, adds class2 to the diagram
     * @param class2 The new class to replace it with. If null, removes class1 from
     *               the diagram
     */
    public static void replace(UMLClass class1, UMLClass class2) {
        if (class1 != null) {
            classes.remove(class1.getName());
        }
        if (class2 != null) {
            classes.put(class2.getName(), class2);
        }
    }
}
