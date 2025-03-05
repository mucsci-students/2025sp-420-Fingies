import java.util.HashMap; 
import java.util.HashSet;

/**
 * Handles all interactions made to the classes
 * @author kdichter, Lincoln Craddock
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
     * Returns a string that lists all of the classes & their attributes.
     * 
     * @return A string containing a list of classes & their attributes.
     */
    static String listClasses()
    {
        if (classes.isEmpty())
            return "No current classes exist";
        
        String str = "";
        for (UMLClass c : classes.values())
        {
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
    static String listClass(UMLClass c)
    {
    	String str = c.getName();
        HashSet<Field> fields = c.getFields();
        HashSet<Method> methods = c.getMethods();
        
        if (fields.isEmpty() && methods.isEmpty())
	        return str;
        
        // prints each field if the list of fields isn't empty
        if (!fields.isEmpty())
        {
            str += ": \n\tfields:";
            for (Field field : fields)
            {
                str += " " + field.getName() + ", ";
            }
            str = str.substring(0, str.length() - 2); // trim off the extra comma
        }
        
        // prints each method followed a list of it's parameters if the list of methods isn't empty
        if (!methods.isEmpty())
        {
            str += "\n\tmethods:";
            for (Method method : methods)
            {
                str += method + ", ";
            }
            str = str.substring(0, str.length() - 2); // trim off the extra comma
        }
        return str;
    }

    /**
     * Resets all classes, attributes, and relationships
     * 
     * @author trush
     */
    static void reset() {
        HashMap<String, UMLClass> reset = new HashMap<>();
        classes = reset;
    }
}

