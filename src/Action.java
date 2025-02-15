/**
	 * An enum that represents an action the user can take with the program.
	 * 
	 * Contains a method for parsing user input into a corresponding Action.
	 * @author Lincoln Craddock
	 */
	public enum Action {
			
		/** Creates a new class. */
		ADD_CLASS,
		/** Removes a class that exists. */
		REMOVE_CLASS,
		/** Gives a class a new name. */
		RENAME_CLASS,
		/** Creates a relationship between two classes. */
		ADD_RELATIONSHIP,
		/** Removes a relationship. */
		REMOVE_RELATIONSHIP,
		/** Creates a new class. */
		ADD_ATTRIBUTE,
		/** Removes a class that exists. */
		REMOVE_ATTRIVUTE,
		/** Gives a class a new name. */
		RENAME_ATTRIBUTE,
		/** Creates a relationship between two classes. */
		SAVE,
		/** Removes a relationship. */
		LOAD,
		/** Displays all of the classes and their contents. */
		LIST_CLASSES,
		/** Lists all of the contents of a particular class. */
		LIST_CLASS,
		/** Displays a list of all of the relationships between classes. */
		LIST_RELATIONSHIPS,
		/** Gives the user instructions on how to use the program, or help with a specific command. */
		HELP,
		/** Closes the program. */
		EXIT;
			
	}