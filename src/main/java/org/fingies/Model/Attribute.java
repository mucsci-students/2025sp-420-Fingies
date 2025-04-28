package org.fingies.Model;

public abstract class Attribute {
    
    //Name field *NEEDS* to be volatile for JSON formatting. Please leave it! @trush
    // private volatile String name;
    private final String allowedCharacters = "_aeioubcdfghjklmnpqrstvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /**
     * Throws an IllegalArgumentException if the user provides a string that contains illegal characters
     * @param name name of the new class/attribute the user desires
     */
    public void validateCharacters(String name)
    {
        for (char c : name.toCharArray())
        {
            if (allowedCharacters.indexOf(c) == -1)
            {
                throw new IllegalArgumentException("String contains invalid characters");
            }
        }
    }

    /**
     * Gets the name of the attribute
     * @return the name of the attribute
     */
    public abstract String getName ();

    /**
     * Changes the name of an attribute
     * @param name the new name to be given to the attribute
     */
    public abstract void renameAttribute (String name);

}
