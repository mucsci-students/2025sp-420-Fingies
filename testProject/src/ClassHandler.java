import java.util.HashMap; 
import java.util.TreeSet;

public class ClassHandler {
    private static HashMap<String, Class> classes;

    /**
     * Attemps to create a new class
     * @param name name the user wants to give to the class
     * @return true if the class was creasted, false otherwise
     */
    static boolean createClass(String name)
    {
        if (!classes.containsKey(name))
        {
            Class c = new Class(name);
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
    static boolean deleteClass(String name)
    {
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
        if (classes.containsKey(className))
        {
            Class c = classes.get(className);
            c.renameClass (newName);
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
    static TreeSet<Class> getAllClasses()
    {
        TreeSet<Class> classesSet = new TreeSet<Class>();
        for (Class c : classes.values())
        {
            classesSet.add (c);
        }
        return classesSet;
    }
}
