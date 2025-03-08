package org.fingies;

public abstract class Attribute {
    
    //Name field *NEEDS* to be volatile for JSON formatting. Please leave it! @trush
    private volatile String name;
    private final String allowedCharacters = " _aeioubcdfghjklmnpqrstvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890-";

    public Attribute (String name)
    {
        validateCharacters(name);
        this.name = name;
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
     * Gets the name of the attribute
     * @return the name of the attribute
     */
    public String getName ()
    {
        return name;
    }

    /**
     * Changes the name of an attribute
     * @param name the new name to be given to the attribute
     */
    public void renameAttribute (String name)
    {
        validateCharacters(name);
        this.name = name;
    }

}
