import java.util.HashSet;
import java.util.List;

/**
 * Represents a class object in a UML Class Diagram
 * @author kdichter
 */
public class UMLClass {
    private String name;
    private HashSet<Field> fields;
    private HashSet<Method> methods;
    // private HashSet<String> incoming;
    // private HashSet<String> outgoing;
    private final String allowedCharacters = " _aeioubcdfghjklmnpqrstvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-";

    /**
     * Constructs a new Class object with a name and empty set of attributes
     * @param name name the user wants to give a class
     */
    UMLClass(String name)
    {
        validateCharacters(name);
        this.name = name;
        fields = new HashSet<Field>();
        methods = new HashSet<Method>();
        // incoming = new HashSet<String>();
        // outgoing = new HashSet<String>();
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
     * Returns a field object specified by the field parameter
     * @param field name of the field
     * @return the field object specified by the field parameter or null if the object doesn't exist
     */
    Field getField (String field)
    {
        for (Field f : fields)
        {
            if (f.getName().equals(field))
                return f;
        }
        return null;
    }

    /**
     * Returns a method object specified by the method parameter and paramNum
     * @param method name of the method
     * @param paramNum the number of parameters the method must contain
     * @return the method object specified by the method parameter and paramNum or null if the object doesn't exist
     */ 
    Method getMethod (String method, int paramNum)
    {
        for (Method m : methods)
        {
            if (m.getName().equals(method) && m.getParameters().size() == paramNum)
            {
                return m;
            }
        }
        return null;
    }

    /**
     * Attempts to add a field to the list of fields
     * @param attribute name of field the user wants to add
     * @precondition field name is not the name of the class
     * @return true if the field was added to the set, false otherwise
     */
    boolean addField (String field)
    {
        validateCharacters(field);
        if (name.equals(field))
            return false;
        return fields.add(new Field(field));
    }

    /**
     * Attempts to add a method to the list of methods
     * @param attribute name of method the user wants to add
     * @return true if the method was added to the set, false otherwise
     */
    boolean addMethod (String method, List<String>parameters)
    {
        validateCharacters(method);
        if (name.equals(method))
            return false;
        return methods.add(new Method(method, parameters));
    }

    /**
     * Attempts to remove a field from the list of fields
     * @param attribute name of field the user wants to remove
     * @return true if the field was removed to the set, false otherwise
     */
    boolean removeField (String field)
    {
        return fields.remove(getField(field));
    }

    /**
     * Attempts to remove a method from the list of methods with paramNum parameters
     * @param method name of method to be removed
     * @param paramNum number of parameters of the desired method to remove
     * @return true if the method was paramNum parameters was removed, false otherwise
     */
    boolean removeMethod (String method, int paramNum)
    {
        return methods.remove(getMethod(method, paramNum));
    }

    /**
     * Attemps to rename an existing field to a new one
     * @param field name of field to be changed
     * @param newName new name of field to replace old name
     * @return true if the name of the field was replaced, false otherwise
     */
    boolean renameField (String field, String newName)
    {
        validateCharacters(newName);
        Field f = getField(field);
        Field newF = getField(newName);

        if (field.equals(newName) || f == null || newF != null || name.equals(newName))
            return false;
        if (fields.contains(f))
        {
            f.renameAttribute(newName);
            return true;
        }
        return false;
    }

    /**
     * Attemps to rename an existing method with paramNum parameters to a new one
     * @param method name of method to be changed
     * @param paramNum number of parameters
     * @param newName new name of method to replace old name
     * @return true if the name of the method was replaced based on paramNum, false otherwise
     */
    boolean renameMethod (String method, int paramNum, String newName)
    {
        validateCharacters(newName);
        Method m = getMethod (method, paramNum);
        Method newM = getMethod (newName, paramNum);

        if (m == null || newM != null)
            return false;
        if (methods.contains(m))
        {
            m.renameAttribute(newName);
            return true;
        }
        return false;
    }

    /**
     * Returns whether a method with paramNum parameters exists in methods or not
     * @param method name of method
     * @param paramNum number of parameters
     * @return true if the method with paramNum parameters does exist in methods, false otherwise
     */
    boolean fieldExists(String field)
    {
        return fields.contains(getField(field));
    }

    /**
     * Returns whether a method with paramNum parameters exists in methods or not
     * @param method name of method
     * @param paramNum number of parameters
     * @return true if the method with paramNum parameters does exist in methods, false otherwise
     */
    boolean methodExists(String method, int paramNum)
    {
        return methods.contains(getMethod(method, paramNum));
    }

    /**
     * Returns the list of fields to the user
     * @return the list of fields to the user
     */
    HashSet<Field> getFields()
    {
        return fields;
    }

    /**
     * Returns the list of methods to the user
     * @return the list of methods to the user
     */
    HashSet<Method> getMethods()
    {
        return methods;
    }

    /**
     * Add an item to either the incoming hashset or the outgoing hashset
     * depending on what the src and dest are
     * @param src source class
     * @param dest desination class
     */
    // boolean addRelationship (String src, String dest)
    // {
    //     // B --> C
    //     if (!src.equals(name) && !dest.equals(name))
    //     {
    //         throw new IllegalArgumentException("Wrong class for relationship");
    //     }
    //     // A --> A
    //     if (src.equals(name) && dest.equals(name))
    //     {
    //         if (incoming.contains(src) && outgoing.contains(dest))
    //             throw new IllegalArgumentException("Relationship already exists");
    //         return incoming.add(src) && outgoing.add(dest);
    //     }
    //     // A --> B
    //     else if (src.equals(name))
    //     {
    //         if (outgoing.contains(dest))
    //             throw new IllegalArgumentException("Relationship already exists");
    //         return outgoing.add(dest);
    //     }
    //     // C --> A
    //     else if (dest.equals(name))
    //     {
    //         if (incoming.contains(src))
    //             throw new IllegalArgumentException("Relationship already exists");
    //         return incoming.add(src);
    //     }
    //     else
    //     {
    //         throw new IllegalArgumentException("Relationship cannot be created");
    //     }
        
    // }

    // /**
    //  * Removes an item from either the incoming hashset or the outgoing hashset
    //  * depending on what the src and dest are
    //  * @param src source class
    //  * @param dest destination class
    //  */
    // boolean removeRelationship (String src, String dest)
    // {
    //    // B --> C
    //    if (!src.equals(name) && !dest.equals(name))
    //    {
    //        throw new IllegalArgumentException("Relationship does not exist");
    //    }
    //    // A --> A
    //    if (src.equals(name) && dest.equals(name))
    //    {
    //        return incoming.remove(src) && outgoing.remove(dest);
    //    }
    //    // A --> B
    //    else if (src.equals(name))
    //    {
    //        return outgoing.remove(dest);
    //    }
    //    // C --> A
    //    else if (dest.equals(name))
    //    {
    //        return incoming.remove(src);
    //    }
    //    else
    //    {
    //        throw new IllegalArgumentException("Relationship cannot be removed");
    //    }
    // }

    // /**
    //  * Returns the incoming hashset
    //  * @return the incoming hashset
    //  */
    // HashSet<String> getIncoming()
    // {
    //     return incoming;
    // }

    // /**
    //  * Returns the outgoing hashset
    //  * @return the outgoing hashset
    //  */
    // HashSet<String> getOutgoing()
    // {
    //     return outgoing;
    // }
}
