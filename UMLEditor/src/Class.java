import java.util.TreeSet;

public class Class {
    private String name;
    private TreeSet<String> attributes;

    /**
     * Constructs a new Class object with a name and empty set of attributes
     * @param name name the user wants to give a class
     */
    Class(String name)
    {
        this.name = name;
        attributes = new TreeSet<String>();
    }

    /**
     * Gets the name of the class
     * @return the name of the class
     */
    String getName()
    {
        return name;
    }

    /**
     * Changes the name of the class
     * @param name name the user wants to change the class name to
     */
    void renameClass(String name)
    {
        this.name = name;
    }

    /**
     * Attempts to add an attribute to the class
     * @param attribute name of attribute the user wants to add
     * @return true if the attribute was added to the set, false otherwise
     */
    boolean addAttribute (String attribute)
    {
        return attributes.add(attribute);
    }

    /**
     * Attemps to delete an attribute from the class
     * @param attribute name of the attribute the user wants to delete
     * @return true if the attribute is deleted, false otherwise
     */
    boolean deleteAttribute (String attribute)
    {
        return attributes.remove(attribute);
    }

    /**
     * Attempts to rename an attribute in the set
     * @param attribute name of the attribute the user wants to rename
     * @param newName name the user wants to give to the attribute
     * @return true if the attribute was renamed, false otherwise
     */
    boolean renameAttribute (String attribute, String newName)
    {
        if (attributes.contains(attribute) && !attributes.contains(newName))
        {
            attributes.remove(attribute);
            attributes.add(newName);
            return true;
        }
        return false;
    }

    /**
     * Returns the list of attributes to the user
     * @return the list of attributes to the user
     */
    TreeSet<String> getAllAttributes()
    {
        return attributes;
    }
}
