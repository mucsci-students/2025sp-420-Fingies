import java.util.TreeSet;

public class Class {
    private String name;
    private TreeSet<String> attributes;
    private String allowedCharacters = " _aeioubcdfghjklmnpqrstvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-";
    private TreeSet<Relationship> relationships;

    /**
     * Constructs a new Class object with a name and empty set of attributes
     * @param name name the user wants to give a class
     */
    Class(String name)
    {
        validateCharacters(name);
        this.name = name;
        attributes = new TreeSet<String>();
        relationships = new TreeSet<Relationship>();
    }

    /**
     * Throws an IllegalArgumentException if the user provides a string that contains illegal characters
     * @param name name of the new class/attribute the user desires
     */
    void validateCharacters(String name)
    {
        for (char c : name.toCharArray())
        {
            if (!allowedCharacters.contains(""+c))
            {
                throw new IllegalArgumentException("That string contains invalid characters");
            }
        }
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
        validateCharacters(name);
        this.name = name;
    }

    /**
     * Attempts to add an attribute to the class
     * @param attribute name of attribute the user wants to add
     * @return true if the attribute was added to the set, false otherwise
     */
    boolean addAttribute (String attribute)
    {
        validateCharacters(attribute);
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
        validateCharacters(newName);
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

    void addRelationship (Relationship r)
    {
        if (r.getSrc().equals(name) || r.getDest().equals(name))
            if (relationships.contains(r))
                throw new IllegalArgumentException("This class already has that relationship.");
            else
                relationships.add(r);
        else
            throw new IllegalArgumentException ("This class must be either the source or destination of its own relationship.");
    }

    void deleteRelationship (Relationship r)
    {
        if (r.getSrc().equals(name) || r.getDest().equals(name))
            relationships.remove(r);
        else
            throw new IllegalArgumentException ("This class must be either the source or destination of its own relationship.");
    }
}
