import java.util.HashMap; 
import java.util.HashSet;

/**
 * 
 */
public class UMLClassHandler {
    private static HashMap<String, UMLClass> classes = new HashMap<>();

    /**
     * Attemps to create a new class
     * @param name name the user wants to give to the class
     * @return true if the class was creasted, false otherwise
     */
    static boolean createClass(String name)
    {
        if (!classes.containsKey(name))
        {
            UMLClass c = new UMLClass(name);
            classes.put(name, c);
            return true;
        }
        return false;
    }

    /**
     * Attempts to delete a class from the hashmap of classes
     * @param name name of the class that the user wants to remove
     * @return true if the class was deleted, false otherwise
     */
    static boolean removeClass(String name)
    {
        if (!exists(name))
        {
            return false;
        }
        // C -- > A
        for (UMLClass inc : classes.get(name).getIncoming())
        {
            inc.removeRelationship(inc, classes.get(name));
        }

        // A --> B
        for (UMLClass out : classes.get(name).getOutgoing())
        {
            out.removeRelationship(classes.get(name), out);
        }

        return classes.remove(name) != null;
    }

    /**
     * Attempts to rename a class in the hashmap of classes
     * @param className name of the class to have its name changed
     * @param newName name the user wants to change the class name to
     * @return true if the class name is changed, false otherwise
     */
    static boolean renameClass(String className, String newName)
    {
        if (classes.containsKey(className) && !classes.containsKey(newName))
        {
            UMLClass c = classes.get(className);
            c.renameClass(newName);
            classes.remove(className);
            classes.put(newName, c);
            return true;
        }
        return false;
    }

    /**
     * Creates a treeset of all of the classes in the classes hashmap
     * @return a treeset of all of the classes in the classes hashmap
     */
    static HashSet<UMLClass> getAllClasses()
    {
        HashSet<UMLClass> classesSet = new HashSet<UMLClass>();
        for (UMLClass c : classes.values())
        {
            classesSet.add (c);
        }
        return classesSet;
    }
    
    
    /**
     * Gets a class given its name.
     * @param name The name of the class.
     * @return the class.
     * @throws NullPointerException if the class does not exist
     */
    static UMLClass getClass(String name)
    {
        if (exists(name))
        {
            return classes.get(name);
        }
        throw new IllegalArgumentException("Class does not exist");
    }
    
    /**
     * Checks whether a class exists given its name.
     * @param name True if the class exists.
     * @return the class.
     */
    static boolean exists(String name)
    {
    	return classes.containsKey(name);
    }
    
    /**
     * @throws IllegalArgumentException when trying to add a relationship that already exists
     * @throws IllegalArgumentException when trying to add a relationship between at least 1 non existing class
     */
    static boolean addRelationship(String src, String dest)
    {
        UMLClass srcClass = getClass(src);
        UMLClass destClass = getClass(dest);
        return srcClass.addRelationship (srcClass, destClass) && destClass.addRelationship (srcClass, destClass);
    }

    static boolean removeRelationship(String src, String dest)
    {
        UMLClass srcClass = getClass(src);
        UMLClass destClass = getClass(dest);
        return srcClass.removeRelationship (srcClass, destClass) && destClass.removeRelationship (srcClass, destClass);
    }

    /**
     * Resets all classes, attributes, and relationships
     * @author trush
     */
    static void reset() {
        HashMap<String, UMLClass> reset = new HashMap<>();
        classes = reset;
    }
}

