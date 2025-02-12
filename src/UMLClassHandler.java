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
     * Creates a hashset of all of the classes in the classes hashmap
     * @return a hashset of all of the classes in the classes hashmap
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
     * adds a relationship from both the src and dest classes
     * @param src src class 
     * @param dest dest class
     * @return true if the relationship was successfully added, false otherwise
     */
    static boolean addRelationship(String src, String dest)
    {
        UMLClass srcClass = getClass(src);
        UMLClass destClass = getClass(dest);
        return srcClass.addRelationship (srcClass, destClass) && destClass.addRelationship (srcClass, destClass);
    }

    /**
     * removes a relationship from both the src and dest classes
     * @param src src class 
     * @param dest dest class
     * @return true if the relationship was successfully removed, false otherwise
     */
    static boolean removeRelationship(String src, String dest)
    {
        UMLClass srcClass = getClass(src);
        UMLClass destClass = getClass(dest);
        return srcClass.removeRelationship (srcClass, destClass) && destClass.removeRelationship (srcClass, destClass);
    }

    static void listClasses()
    {
        if (classes.isEmpty())
        {
            System.out.println("No current classes exist");
            return;
        }
        for (UMLClass c : classes.values())
        {
            listClass(c.getName());
        }
    }

    static void listClass(String className)
    {
        UMLClass c = getClass(className);
        System.out.print(c + ": ");
        for (String atr : c.getAllAttributes())
        {
            System.out.print(", " + atr);
        }
    }

    static void listRelationships()
    {
        boolean isAllEmpty = true;
        for (UMLClass c : classes.values())
        {
            HashSet<UMLClass> outgoing = c.getOutgoing();
            // checking to see if every class has no outgoing relationships
            if (isAllEmpty && !outgoing.isEmpty())
                isAllEmpty = false;
            for (UMLClass out : outgoing)
            {
                System.out.println(c + ": " + out);
            }
        }
        if (isAllEmpty)
        {
            System.out.println("No current relationships exist");
        }
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

