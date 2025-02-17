import java.util.HashSet;

/**
 * Represents a class object in a UML Class Diagram
 * @author kdichter
 */
public class UMLClass {
    private String name;
    private HashSet<String> attributes;
    private HashSet<String> incoming;
    private HashSet<String> outgoing;
    private final String allowedCharacters = " _aeioubcdfghjklmnpqrstvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-";

    /**
     * Constructs a new Class object with a name and empty set of attributes
     * @param name name the user wants to give a class
     */
    UMLClass(String name)
    {
        validateCharacters(name);
        this.name = name;
        attributes = new HashSet<String>();
        incoming = new HashSet<String>();
        outgoing = new HashSet<String>();
    }

    /**
     * Throws an IllegalArgumentException if the user provides a string that contains illegal characters
     * @param name name of the new class/attribute the user desires
     */
    void validateCharacters(String name)
    {
        if (name.length() > 50)
        {
            throw new IllegalArgumentException("String is longer than 50 characters");
        }
        for (char c : name.toCharArray())
        {
            if (allowedCharacters.indexOf(c) == -1)
            {
                throw new IllegalArgumentException("String contains invalid characters");
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
        if (name.equals(attribute))
            return false;
        return attributes.add(attribute);
    }

    /**
     * Attemps to delete an attribute from the class
     * @param attribute name of the attribute the user wants to delete
     * @return true if the attribute is deleted, false otherwise
     */
    boolean removeAttribute (String attribute)
    {
        if (!attributes.remove(attribute))
        {
            throw new IllegalArgumentException("Attribute provided does not exist");
        }
        return true;
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
            if (!name.equals(newName)) {
                attributes.remove(attribute);
                return attributes.add(newName);
            }
            else {
                return false;
            }
        }
        return false;
    }

    /**
     * Returns whether or not an attribute exists in the attribute list or not
     * @param attribute attribute to test whether it exists
     * @return true if the attribute exists, false otherwise
     */
    boolean exists(String attribute)
    {
        return attributes.contains(attribute);
    }

    /**
     * Returns the list of attributes to the user
     * @return the list of attributes to the user
     */
    HashSet<String> getAllAttributes()
    {
        return attributes;
    }

    /**
     * Add an item to either the incoming hashset or the outgoing hashset
     * depending on what the src and dest are
     * @param src source class
     * @param dest desination class
     */
    boolean addRelationship (String src, String dest)
    {
        // B --> C
        if (!src.equals(name) && !dest.equals(name))
        {
            throw new IllegalArgumentException("Wrong class for relationship");
        }
        // A --> A
        if (src.equals(name) && dest.equals(name))
        {
            if (incoming.contains(src) && outgoing.contains(dest))
                throw new IllegalArgumentException("Relationship already exists");
            return incoming.add(src) && outgoing.add(dest);
        }
        // A --> B
        else if (src.equals(name))
        {
            if (outgoing.contains(dest))
                throw new IllegalArgumentException("Relationship already exists");
            return outgoing.add(dest);
        }
        // C --> A
        else if (dest.equals(name))
        {
            if (incoming.contains(src))
                throw new IllegalArgumentException("Relationship already exists");
            return incoming.add(src);
        }
        else
        {
            throw new IllegalArgumentException("Relationship cannot be created");
        }
        
    }

    /**
     * Removes an item from either the incoming hashset or the outgoing hashset
     * depending on what the src and dest are
     * @param src source class
     * @param dest destination class
     */
    boolean removeRelationship (String src, String dest)
    {
       // B --> C
       if (!src.equals(name) && !dest.equals(name))
       {
           throw new IllegalArgumentException("Relationship does not exist");
       }
       // A --> A
       if (src.equals(name) && dest.equals(name))
       {
           return incoming.remove(src) && outgoing.remove(dest);
       }
       // A --> B
       else if (src.equals(name))
       {
           return outgoing.remove(dest);
       }
       // C --> A
       else if (dest.equals(name))
       {
           return incoming.remove(src);
       }
       else
       {
           throw new IllegalArgumentException("Relationship cannot be removed");
       }
    }

    /**
     * Returns the incoming hashset
     * @return the incoming hashset
     */
    HashSet<String> getIncoming()
    {
        return incoming;
    }

    /**
     * Returns the outgoing hashset
     * @return the outgoing hashset
     */
    HashSet<String> getOutgoing()
    {
        return outgoing;
    }
}
