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

        // // C -- > A
        // for (String inc : classes.get(name).getIncoming())
        // {
        //     classes.get(inc).removeRelationship(inc, name);
        // }

        // // A --> B
        // for (String out : classes.get(name).getOutgoing())
        // {
        //     classes.get(out).removeRelationship(name, out);
        // }
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
            
            // hot fix--updates incoming/outgoing relationships with the new name
            // for(UMLClass cl : classes.values())
            // {
            // 	HashSet<String> incoming = cl.getIncoming();
            // 	if (incoming.contains(className))
            // 	{
            // 		incoming.remove(className);
            // 		incoming.add(newName);
            // 	}
            // 	HashSet<String> outgoing = cl.getOutgoing();
            // 	if (outgoing.contains(className))
            // 	{
            // 		outgoing.remove(className);
            // 		outgoing.add(newName);
            // 	}
            // }  
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
    // static boolean addRelationship(String src, String dest)
    // {
    //     UMLClass srcClass = getClass(src);
    //     UMLClass destClass = getClass(dest);
    //     if (src.equals(dest))
    //         return srcClass.addRelationship (src, dest);
    //     return srcClass.addRelationship (src, dest) && destClass.addRelationship (src, dest);
    // }

    // /**
    //  * removes a relationship from both the src and dest classes
    //  * @param src src class 
    //  * @param dest dest class
    //  * @return true if the relationship was successfully removed, false otherwise
    //  */
    // static boolean removeRelationship(String src, String dest)
    // {
    //     UMLClass srcClass = getClass(src);
    //     UMLClass destClass = getClass(dest);
    //     return srcClass.removeRelationship (src, dest) && destClass.removeRelationship (src, dest);
    // }

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
            str += ": \n\t fields:";
            for (Field field : fields)
            {
                str += " " + field.getName() + ",";
            }
            str = str.substring(0, str.length() - 1); // trim off the extra comma
        }
        
        // prints each method followed a list of it's parameters if the list of methods isn't empty
        if (!methods.isEmpty())
        {
            str += "\nmethods:";
            for (Method method : methods)
            {
                str += " " + method.getName() + "(";
                for (String s : method.getParameters())
                {
                    str += s + ", ";
                }
                str = str.substring(0, str.length() - 1); // trim off the extra comma
                str += "), ";
            }
            str = str.substring(0, str.length() - 1); // trim off the extra comma
        }
        return str;
    }

    /**
     * Returns a list of the relationships between classes. The string will contain
     * each class's outgoing relationships.
     * 
     * @return A string containing a list of each class's outgoing relationships.
     */
    // static String listRelationships()
    // {
    // 	String str = "";
    //     for (UMLClass c : classes.values())
    //     {
    //         HashSet<String> outgoing = c.getOutgoing();
    //         for (String out : outgoing)
    //         {
    //             str += c.getName() + " --> " + out + "\n";
    //         }
    //     }
        
    //     if (str.isEmpty())
    //         return "No current relationships exist";
    //     else
    //     	return str.substring(0, str.length() - 1);
    // }

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

