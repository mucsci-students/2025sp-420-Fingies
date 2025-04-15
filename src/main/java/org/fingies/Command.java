package org.fingies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An object that represents a command that the user can make of the program.
 * 
 * Contains an enum representing the high-level action they want to take, and
 * a list of arguments given by the user.
 * @author Lincoln Craddock
 */
public class Command {
	
	public final Action action;
	public final String[] arguments;
	
	/**
	 * The maximum number of arguments Command.parseCommand() will read.
	 */
	public static final int maxNumArgs = 100;
	
	//TODO: Add changing field and parameter data type field.
	/**
	 * An array storing the keywords that represents each command.
	 */
	public static final String[] COMMANDS = {
		    "add class", "remove class", "rename class",
		    "add relationship", "remove relationship", "save",
		    "load", "list classes", "list class",
		    "list relationships", "help", "exit",
		    "add method", "remove method", "rename method",
		    "add field", "remove field", "rename field",
		    "add parameters", "remove parameters", "rename parameter",
		    "change relationship type", "change parameter type", "change field type",
			"change method type", "undo", "redo"
		};
	
	/**
	 * A shorter version of each keyword that can be used instead.
	 */
	public static final String[] COMMANDS_SHORTHAND = {
		    "addc", "rmc", "rnc",         	   // add class, remove class, rename class
		    "addr", "rmr", "sv",               // add relationship, remove relationship, save
		    "ld", "listcls", "listcl",         // load, list classes, list class
		    "listr", "h", "quit",              // list relationships, help, exit
		    "addm", "rmm", "rnm",              // add method, remove method, rename method
		    "addf", "rmf", "rnf",              // add field, remove field, rename field
		    "addp", "rmp", "rnp",              // add parameters, remove parameters, rename parameter
		    "crt", "cpt", "cft",			   // change relationship type, change parameter type, change field type
		    "cmt", "u", "r"                    // change method return type, undo, redo
		};
	
	/**
	 * The expected arguments for each command.
	 */
	public static final String[] COMMAND_ARGS = {
			"CLASS_NAME", //add class
			"CLASS_NAME", //remove class
			"CLASS_NAME NEW_NAME", //rename class
			"SRC_CLASS DEST_CLASS RELATIONSHIP_TYPE", //add relationship
			"SRC_CLASS DEST_CLASS", //remove relationship
			"[ FILE_PATH ]", //save
			"[ FILE_PATH ]", //load
			"", //list classes
			"CLASS_NAME", //list class
			"", //list relationships
			"[ COMMAND ]", //help
			"", //exit
			"CLASS_NAME METHOD_NAME RETURN_TYPE [ PARAMETER_TYPE PARAMETER ... ]", //add method
			"CLASS_NAME METHOD_NAME [ PARAMETER_TYPE ... ]", //remove method
			"CLASS_NAME METHOD_NAME [ PARAMETER_TYPE ... ] NEW_NAME", //rename method
			"CLASS_NAME FIELD_NAME TYPE", //add field
			"CLASS_NAME FIELD_NAME", //remove field
			"CLASS_NAME FIELD_NAME NEW_NAME", //rename field
			"CLASS_NAME METHOD_NAME [ PARAMETER_TYPE ... ] ; [ NEW_PARAMETER_TYPE NEW_PARAMETER ... ]", //add parameters
			"CLASS_NAME METHOD_NAME [ PARAMETER_TYPE ... ] ; [ OLD_PARAMETER ... ] ", //remove parameters
			"CLASS_NAME METHOD_NAME [ PARAMETER_TYPE ... ] PARAMETER_NAME NEW_NAME", //rename parameter
			"SRC_CLASS DEST_CLASS NEW_RELATIONSHIP_TYPE", //change relationship type
			"CLASS_NAME METHOD_NAME [ PARAMETER_TYPE ... ] PARAMETER_NAME NEW_TYPE", //change parameter type
			"CLASS_NAME FIELD_NAME NEW_TYPE", //change field type
			"CLASS_NAME METHOD_NAME [ PARAMETER_TYPE ... ] NEW_TYPE", //change method type
			"", //undo
			"", //redo
			"[ FILE_PATH ]" //export image
		};
	
	/**
	 * A description for each command.
	 */
	public static final String[] COMMAND_DESCRIPTION = {
			"Creates a new class.",
			"Removes a class from the diagram.",
			"Gives a class a new name.",
			"Creates a directed relationship from one class to another. \n"
			+ "Relationships can either be aggregation, composition, inheritance, or realization type.",
			"Removes the relationship going from one class to another.",
			"Saves the current diagram given a filepath (e.g. C:\\Users\\Zoppetti\\Demos\\Test.txt). \n"
			+ "A filepath doesn't need to be provided if the diagram has been saved before.",
			"Loads a diagram into the editor given a filepath (e.g. C:\\\\Users\\\\Zoppetti\\\\Demos\\\\Test.txt).",
			"Prints a list of all of the classes in the diagram.",
			"Prints all of the attributes in a class.",
			"Lists all of the relationships between classes in the diagram.",
			"Prints a list of commands and their shorthand versions. \n"
			+ "If the name of a command is supplied as an argument, prints a description of a single command.",
			"Exits the program.",
			"Adds a method to a class. \n"
			+ "Two methods in a class can have the same name, as long as they have a different arity.",
			"Removes a method from a class. \n",
			"Gives a method of a class a new name.",
			"Adds a field to a class.",
			"Removes a field from a class.",
			"Gives a field of a class a new name.",
			"Adds a set of parameters to a method of a class.",
			"Removes a set of parameters from a method of a class.",
			"Gives a parameter of a method a new name.",
			"Changes the type of a specified relationship.",
			"Changes the data type of a parameter.",
			"Changes the data type of a field.",
			"Changes the return data type of a method",
			"Undoes the last change made.",
			"Redoes the last undone change."
		};
	
	Command (Action a, String[] args)
	{
		action = a;
		arguments = args;
	}
	
	/**
	 * Returns the index of a specific command in the array of commands.
	 * 
	 * @param command The name of the command to get the index for.
	 * @return The index of the specified command, or -1 if the command doesn't exist.
	 */
	public static int indexOfCommand(String command)
	{
		for (int i = 0; i < COMMANDS.length; ++i)
			if (COMMANDS[i].equals(command))
				return i;
		
		for (int i = 0; i < COMMANDS_SHORTHAND.length; ++i)
			if (COMMANDS_SHORTHAND[i].equals(command))
				return i;
		
		return -1;
	}
	
	/**
	 * Parses the string argument as a Command.
	 * 
	 * Commands should be formatted 'command ARGS' where 'command' is the keyword or
	 * shorthand keyword for that command, and ARGS is the list of arguments for that
	 * command, separated by spaces. Arguments can be surrounded with double quotes
	 * so specify argument names with spaces in them (ex. 'add class "Hello World"').
	 * 
	 * 
	 * @param input
	 * @return null if invalid input was provided; otherwise, a Command representing the input command.
	 */
	public static Command parseCommand(String input)
	{
		// parse command
		input = input.trim();
		Action a = null;
		int cmdLen = 0;
		for (int i = 0; i < COMMANDS.length && a == null; ++i)
		{
			if (input.startsWith(COMMANDS[i]))
			{
				a = Action.values()[i];
				cmdLen = COMMANDS[i].length();
				
			}
		}
		for (int i = 0; i < COMMANDS_SHORTHAND.length && a == null; ++i)
		{
			if (input.startsWith(COMMANDS_SHORTHAND[i]))
			{
				a = Action.values()[i];
				cmdLen = COMMANDS_SHORTHAND[i].length();
			}
		}
		if (a == null)
			return null;
		
		// trim 'input' to be just the arguments
		if (input.length() > cmdLen)
		{
			if (input.charAt(cmdLen) != ' ')
				return null;
			input = input.substring(cmdLen + 1);
		}
		else
			input = "";
		
		// parse arguments
		boolean quote = false;
		String token = "";
		List<String> args = new ArrayList<String>();
		for (char c : input.toCharArray())
		{
			if (c == '\"')
			{
				quote = !quote;
				if (token.length() > 0)
				{
					args.add(token);
					if (args.size() > maxNumArgs)
						throw new IllegalArgumentException ("Commands can't have more than " + maxNumArgs + " arguments.");
					token = "";
				}
			}
			else if (c == ' ' && !quote)
			{
				if (token.length() > 0)
				{
					args.add(token);
					if (args.size() > maxNumArgs)
						throw new IllegalArgumentException ("Commands can't have more than " + maxNumArgs + " arguments.");
				}
				token = "";
			}
			else
			{
				token += c;
			}
		}
		if (token.length() > 0)
		{
			args.add(token);
			if (args.size() > maxNumArgs)
				throw new IllegalArgumentException ("Commands can't have more than " + maxNumArgs + " arguments.");
		}
		if (quote)
			throw new IllegalArgumentException ("Every quote should have an end-quote.");
		
		return new Command (a, args.toArray(new String[0]));
	}
	
	@Override
	public String toString()
	{
		return action + " " + Arrays.toString(arguments);
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (o == null || o.getClass() != getClass())
			return false;
		Command c = (Command) o;
		if (c.action != action || c.arguments.length != arguments.length)
			return false;
		for (int i = 0; i < arguments.length; ++i)
			if (!arguments[i].equals(c.arguments[i]))
				return false;
		return true;
	}
	
	@Override
	public int hashCode()
	{
		return action.hashCode() * 3 + arguments.hashCode() * 5;
	}

}