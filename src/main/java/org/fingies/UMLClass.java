package org.fingies;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a class object in a UML Class Diagram
 */
public class UMLClass {
    private String name;
    private List<Field> fields;
    private List<Method> methods;
    private Position position;
    private final String allowedCharacters = "_aeioubcdfghjklmnpqrstvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

     /**
     * Constructs a new Class object with a name and empty set of attributes
     * @param name name the user wants to give a class
     */
    public UMLClass(String name) {
        validateCharacters(name);
        this.name = name;
        this.fields = new ArrayList<>();
        this.methods = new ArrayList<>();
        this.position = new Position();
    }
    /**
     * Copy constructor
     * @param clazz The class you want to copy
     */
    public UMLClass(UMLClass clazz)
    {
    	name = clazz.name;
    	fields = new ArrayList<Field>(clazz.fields);
    	methods = new ArrayList<Method> (clazz.methods);
    	position = new Position(clazz.position);
    }
    /**
     * Throws an IllegalArgumentException if the user provides a string that contains illegal characters
     * @param name name of the new class/attribute the user desires
     */
    public void validateCharacters(String name) {
        if (name.length() > 50) {
            throw new IllegalArgumentException("Class names must not be longer than 50 characters");
        }
        for (char c : name.toCharArray()) {
            if (allowedCharacters.indexOf(c) == -1) {
                throw new IllegalArgumentException("The name " + name + " contains invalid characters");
            }
        }
    }
    /**
     * Gets the name of the class
     * @return the name of the class
     */
    public String getName() {
        return name;
    }

    /**
     * Changes the name of the class
     * @param name name the user wants to change the class name to
     */
    public void renameClass(String name) {
        validateCharacters(name);
        this.name = name;
    }

    /**
     * Returns a field object specified by the field parameter
     * @param fieldName name of the field
     * @return the field object specified by the field parameter or null if the object doesn't exist
     */
    public Field getField(String fieldName) {
        return fields.stream().filter(f -> f.getName().equals(fieldName)).findFirst().orElse(null);
    }

    /**
     * Returns a method object specified by the method name and the method types
     * @param methodName name of the method
     * @param types the list of parameter types
     * @return the method object specified by the method name and the method types
     */ 
    public Method getMethod(String methodName, List<String> types) {
        return methods.stream()
                .filter(m -> m.getName().equals(methodName) && m.getParameterTypes().equals(types))
                .findFirst().orElse(null);
    }

     /**
     * Attempts to add a field to the list of fields
     * @param fieldName name of field the user wants to add
     * @param type data type for the field the user wants to add
     * @precondition field name is not the name of the class
     * @return true if the field was added to the set, false otherwise
     */
    public boolean addField(String fieldName, String type) {
        if (name.equals(fieldName))
            throw new IllegalArgumentException("A field must have a different name than its class");
        if (fieldExists(fieldName))
            throw new IllegalArgumentException(name + " already has a field called " + fieldName);
        return fields.add(new Field(fieldName, type));
    }

    /**
     * Attempts to add a method to the list of methods
     * @param methodName name of method the user wants to add
     * @param return_type return type of method the user wants to add
     * @param parameters parameters of the method the user wants to add
     * @return true if the method was added to the set, false otherwise
     */
    public boolean addMethod(String methodName, String returnType, List<String> parameters, List<String> types) {
        if (name.equals(methodName))
            throw new IllegalArgumentException("A method must have a different name than its class");
        if (methodExists(methodName, types))
            throw new IllegalArgumentException("A method with that name and types already exists");
        return methods.add(new Method(methodName, returnType, parameters, types));
    }

    /**
     * Attempts to remove a field from the list of fields
     * @param fieldName name of field the user wants to remove
     * @return true if the field was removed to the set, false otherwise
     */
    public boolean removeField(String fieldName) {
        Field field = getField(fieldName);
        if (field == null)
            throw new IllegalArgumentException("Field not found");
        return fields.remove(field);
    }

    /**
     * Attempts to remove a method from the list of methods with arity parameters
     * @param methodName name of method to be removed
     * @param types types the method to be removed
     * @return true if the method was arity parameters was removed, false otherwise
     */
    public boolean removeMethod(String methodName, List<String> types) {
        Method method = getMethod(methodName, types);
        if (method == null)
            throw new IllegalArgumentException("Method not found");
        return methods.remove(method);
    }

    /**
     * Attemps to rename an existing field to a new one
     * @param fieldName name of field to be changed
     * @param type data type of the field to be changed
     * @param newName new name of field to replace old name
     * @return true if the name of the field was replaced, false otherwise
     */
    public boolean renameField(String fieldName, String type, String newName) {
        validateCharacters(newName);
        Field field = getField(fieldName);
        if (field == null || fieldExists(newName) || name.equals(newName))
            throw new IllegalArgumentException("Invalid rename operation");
        getField(fieldName).renameAttribute(newName);
        return true;
        // removeField(fieldName);
        // return addField(newName, type);
    }

    /**
     * Attemps to rename an existing method with arity parameters to a new one
     * @param methodName name of method to be changed
     * @param types types of method to be changed
     * @param newName new name of method to replace old name
     * @return true if the name of the method was replaced based on types
     */
    public boolean renameMethod(String methodName, List<String> types, String newName) {
        validateCharacters(newName);
        Method method = getMethod(methodName, types);
        if (method == null || methodExists(newName, types))
            throw new IllegalArgumentException("Invalid rename operation");
        getMethod(methodName, types).renameAttribute(newName);
        return true;
        // removeMethod(methodName, types);
        // return addMethod(newName, method.getReturnType(), method.getParameterNames(), types);
    }

    /**
     * Returns whether a field with the given String name exists in fields
     * @param fieldName name of field
     * @return true if the field exists in fields, false otherwise
     */
    public boolean fieldExists(String fieldName) {
        return getField(fieldName) != null;
    }

    /**
     * Returns whether a method with arity parameters exists in methods or not
     * @param methodName name of method
     * @param types types of the method
     * @return true if the method with types does exist in methods, false otherwise
     */
    public boolean methodExists(String methodName, List<String> types) {
        return getMethod(methodName, types) != null;
    }

    /**
     * Returns the list of fields to the user
     * @return the list of fields to the user
     */
    public List<Field> getFields() {
        return new ArrayList<>(fields);
    }

     /**
     * Returns the list of methods to the user
     * @return the list of methods to the user
     */
    public List<Method> getMethods() {
        return new ArrayList<>(methods);
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
