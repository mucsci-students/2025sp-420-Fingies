package org.fingies;

/**
 * An enum that represents an action the user can take with the program.
 * <p>
 * Contains a method for parsing user input into a corresponding Action.
 *
 * @author Lincoln Craddock
 */
public enum Action {

    /**
     * Creates a new class.
     */
    ADD_CLASS,
    /**
     * Removes a class that exists.
     */
    REMOVE_CLASS,
    /**
     * Gives a class a new name.
     */
    RENAME_CLASS,
    /**
     * Creates a relationship between two classes.
     */
    ADD_RELATIONSHIP,
    /**
     * Removes a relationship.
     */
    REMOVE_RELATIONSHIP,
    /**
     * Creates a relationship between two classes.
     */
    SAVE,
    /**
     * Removes a relationship.
     */
    LOAD,
    /**
     * Displays all of the classes and their contents.
     */
    LIST_CLASSES,
    /**
     * Lists all of the contents of a particular class.
     */
    LIST_CLASS,
    /**
     * Displays a list of all of the relationships between classes.
     */
    LIST_RELATIONSHIPS,
    /**
     * Gives the user instructions on how to use the program, or help with a specific command.
     */
    HELP,
    /**
     * Closes the program.
     */
    EXIT,
    /**
     * Adds a new method to a class.
     */
    ADD_METHOD,
    /**
     * Removes a method from a class.
     */
    REMOVE_METHOD,
    /**
     * Gives a method a new name.
     */
    RENAME_METHOD,
    /**
     * Adds a new field to a class.
     */
    ADD_FIELD,
    /**
     * Removes a field from a class.
     */
    REMOVE_FIELD,
    /**
     * Gives a field a new name.
     */
    RENAME_FIELD,
    /**
     * Changes the data type of a field.
     */
    CHANGE_FIELD_TYPE,
    /**
     * Adds a list of new parameters to a method
     */
    ADD_PARAMETERS,
    /**
     * Removes a set of parameters from a method
     */
    REMOVE_PARAMETERS,
    /**
     * Changes the name of a parameter to a new one
     */
    RENAME_PARAMETER,
    /**
     * Changes the data type of a parameter to a new one
     */
    CHANGE_PARAMETER_TYPE,
    /**
     * Changes a relationship type
     */
    CHANGE_RELATIONSHIP_TYPE;
}