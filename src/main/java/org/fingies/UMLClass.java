package org.fingies;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Represents a class object in a UML Class Diagram
 * @author Kevin Dichter, Lincoln Craddock, Tristan Rush
 */
public class UMLClass {
    private String name;
    private HashSet<Field> fields;
    private HashSet<Method> methods;
    private Position position;
    private final String allowedCharacters = "_aeioubcdfghjklmnpqrstvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    /**
     * Constructs a new Class object with a name and empty set of attributes
     * @param name name the user wants to give a class
     */
    public UMLClass(String name)
    {
        validateCharacters(name);
        this.name = name;
        fields = new HashSet<Field>();
        methods = new HashSet<Method>();
        position = new Position();
    }

    /**
     * Throws an IllegalArgumentException if the user provides a string that contains illegal characters
     * @param name name of the new class/attribute the user desires
     */
    public void validateCharacters(String name)
    {
        if (name.length() > 50)
        {
            throw new IllegalArgumentException("Class names must not be longer than 50 characters");
        }
        for (char c : name.toCharArray())
        {
            if (allowedCharacters.indexOf(c) == -1)
            {
                throw new IllegalArgumentException("The name " + name + " contains invalid characters");
            }
        }
    }

    /**
     * Gets the name of the class
     * @return the name of the class
     */
    public String getName()
    {
        return name;
    }

    /**
     * Changes the name of the class
     * @param name name the user wants to change the class name to
     */
    public void renameClass(String name)
    {
        validateCharacters(name);
        this.name = name;
    }

    /**
     * Returns a field object specified by the field parameter
     * @param field name of the field
     * @return the field object specified by the field parameter or null if the object doesn't exist
     */
    public Field getField (String field)
    {
        for (Field f : fields)
        {
            if (f.getName().equals(field))
                return f;
        }
        return null;
    }

    /**
     * Returns a method object specified by the method parameter and arity
     * @param method name of the method
     * @param return_type return type of the method
     * @param arity the number of parameters the method must contain
     * @return the method object specified by the method parameter and arity or null if the object doesn't exist
     */ 
    public Method getMethod (String method, String return_type, int arity)
    {
        for (Method m : methods)
        {
            if (m.getName().equals(method) && m.getReturnType().equals(return_type) && m.getParameters().size() == arity)
            {
                return m;
            }
        }
        return null;
    }

    /**
     * Attempts to add a field to the list of fields
     * @param field name of field the user wants to add
     * @param type data type for the field the user wants to add
     * @precondition field name is not the name of the class
     * @return true if the field was added to the set, false otherwise
     */
    public boolean addField (String field, String type)
    {
    	if (name.equals(field))
            throw new IllegalArgumentException("A field must have a different name than its class");
    	if (!fields.add(new Field(field, type)))
        	throw new IllegalArgumentException(name + " already has a field called " + field);
    	return true;
    }

    /**
     * Attempts to add a method to the list of methods
     * @param method name of method the user wants to add
     * @param return_type return type of method the user wants to add
     * @param parameters parameters of the method the user wants to add
     * @return true if the method was added to the set, false otherwise
     */
    public boolean addMethod (String method, String return_type, Map<String, String>parameters)
    {
        if (name.equals(method))
        	throw new IllegalArgumentException("A method must have a different name than its class");
        if (methodExists(method, return_type, parameters.size()))
        	throw new IllegalArgumentException("A method with that name and arity already exists");
        return methods.add(new Method(method, return_type, parameters));
    }

    /**
     * Attempts to atomically add a list of parameters to a method
     * @param method name of method to add to
     * @param return_type return type of method to add to
     * @param arity specified arity of method to add to
     * @param parameters list of parameters to add to method
     * @return true if all parameters were added, false if any single parameter couldn't be added
     */
    public boolean addParameters(String method, String return_type, int arity, Map<String,String> parameters)
    {
        Method m = getMethod(method, return_type, arity);
        if (m == null)
        	throw new IllegalArgumentException("Class " + name + " doesn't have a method with the name " + method + " and arity " + arity);
        if (methodExists(method, return_type, arity + parameters.size()))
        	throw new IllegalArgumentException("Class " + name + " already has a method with the name " + method + " and arity " + arity);
        else
        {
            return m.addParameters(parameters);
        }
    }

    /**
     * Attempts to remove a field from the list of fields
     * @param field name of field the user wants to remove
     * @return true if the field was removed to the set, false otherwise
     */
    public boolean removeField (String field)
    {
    	boolean result = fields.remove(getField(field));
    	if (!result)
    		throw new IllegalArgumentException("Class " + name + " doesn't have a field named " + field);
        return true;
    }

    /**
     * Attempts to remove a method from the list of methods with arity parameters
     * @param method name of method to be removed
     * @param return_type return type of method to be removed
     * @param arity number of parameters of the desired method to remove
     * @return true if the method was arity parameters was removed, false otherwise
     */
    public boolean removeMethod (String method, String return_type, int arity)
    {
    	boolean result = methods.remove(getMethod(method, return_type, arity));
    	if (!result)
    		throw new IllegalArgumentException("Class " + name + " doesn't have a method named " + method + " with the arity " + arity);
    	return true;
    }

    /**
     * Attempts to remove a list of parameters from a method with specified name and arity
     * from list of all methods
     * @param method name of method to remove from
     * @param return_type return type of method to remove from
     * @param arity arity of method to remove from
     * @param parameters list of parameters to remove from specified method
     * @return true if the list of parameters were successfully removed.
     */
    public boolean removeParameters(String method, String return_type, int arity, List<String> parameters)
    {
        Method m = getMethod(method, return_type, arity);
        if (m == null)
        	throw new IllegalArgumentException("Class " + name + " does not exist");
        else
        {
            return m.removeParameters(parameters);
        }
    }

    /**
     * Attemps to rename an existing field to a new one
     * @param field name of field to be changed
     * @param type data type of the field to be changed;
     * @param newName new name of field to replace old name
     * @return true if the name of the field was replaced, false otherwise
     */
    public boolean renameField (String field, String type, String newName)
    {
        validateCharacters(newName);
        Field f = getField(field);
        Field newF = getField(newName);

        if (field.equals(newName))
        	throw new IllegalArgumentException("Bro seriously? Why would you rename a field to be the same name bro.");
        if (f == null)
        	throw new IllegalArgumentException("Class " + name + " doesn't have a field named " + field);
        if (newF != null)
        	throw new IllegalArgumentException("Class " + name + " already has a field named " + newName);
        if (name.equals(newName))
        	throw new IllegalArgumentException("Fields must have different names than their classes");
        fields.remove(f);
        fields.add(new Field(newName, type));
        return true;
    }

    /**
     * Attemps to rename an existing method with arity parameters to a new one
     * @param method name of method to be changed
     * @param return_type the return type of the method to be changed
     * @param arity number of parameters
     * @param newName new name of method to replace old name
     * @return true if the name of the method was replaced based on arity, false otherwise
     */
    public boolean renameMethod (String method, String return_type, int arity, String newName)
    {
        validateCharacters(newName);
        Method m = getMethod (method, return_type, arity);
        Method newM = getMethod (newName, return_type, arity);

        if (m == null)
        	throw new IllegalArgumentException("Class " + name + " doesn't have a method called " + method + " with the arity " + arity);
        if (newM != null)
    	throw new IllegalArgumentException("Class " + name + " already has a method called " + newName + " with " + arity + " parameters");
        methods.remove(m);
        Method newMethod = new Method(newName, return_type, m.getParameters());
        methods.add(newMethod);
        return true;
    }

    /**
     * Returns whether a field with the given String name exists in fields
     * @param field name of field
     * @return true if the field exists in fields, false otherwise
     */
    public boolean fieldExists(String field)
    {
        return fields.contains(getField(field));
    }

    /**
     * Returns whether a method with arity parameters exists in methods or not
     * @param method name of method
     * @param return_type return type of method
     * @param arity number of parameters
     * @return true if the method with arity parameters does exist in methods, false otherwise
     */
    public boolean methodExists(String method, String return_type, int arity)
    {
        return methods.contains(getMethod(method, return_type, arity));
    }

    /**
     * Returns the list of fields to the user
     * @return the list of fields to the user
     */
    public HashSet<Field> getFields()
    {
        return fields;
    }

    /**
     * Returns the list of methods to the user
     * @return the list of methods to the user
     */
    public HashSet<Method> getMethods()
    {
        return methods;
    }

    /**
     * Gets the position of the UML Class
     * @return the current position
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Sets the position of the UML Class
     * @param x the x position to set the class to
     * @param y the y position to set the class to.
     */
    public void setPosition(int x, int y) {
        position.setX(x);
        position.setY(y);
    }
}
