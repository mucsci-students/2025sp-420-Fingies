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
	 * An array storing the keywords that represents each command.
	 */
	public static final String[] COMMANDS = {
		    "add class", "remove class", "rename class",
		    "add relationship", "remove relationship", "add attribute",
		    "remove attribute", "rename attribute", "save",
		    "load", "list classes", "list class",
		    "list relationships", "help", "exit"
		};
	
	/**
	 * A shorter version of each keyword that can be used instead.
	 */
	public static final String[] COMMANDS_SHORTHAND = {
		    "addc", "rmc", "rnc",         	   // add class, remove class, rename class
		    "addr", "rmr", "adda",             // add relationship, remove relationship, add attribute
		    "rma", "rna", "sv",                // remove attribute, rename attribute, save
		    "ld", "listcls", "listcl",         // load, list classes, list class
		    "listr", "h", "quit"               // list relationships, help, exit
		};
	
	/**
	 * The expected arguments for each command.
	 */
	public static final String[] COMMAND_ARGS = {
			"CLASS_NAME",
			"CLASS_NAME",
			"CLASS_NAME NEW_NAME",
			"SRC_CLASS DEST_CLASS",
			"SRC_CLASS DEST_CLASS",
			"CLASS_NAME ATTRIBUTE",
			"CLASS_NAME ATTRIBUTE",
			"CLASS_NAME ATTRIBUTE_NAME NEW_NAME",
			"FILE_PATH",
			"FILE_PATH",
			"",
			"CLASS_NAME",
			"",
			"[ COMMAND ]",
			""
		};
	
	/**
	 * A description for each command.
	 */
	public static final String[] COMMAND_DESCRIPTION = {
			"Creates a new class.",
			"Removes a class from the diagram.",
			"Gives a class a new name.",
			"Creates a directed relationship from one class to another.",
			"Removes the relationship going from one class to another.",
			"Adds an attribute to a class.",
			"Removes an attribute from a class.",
			"Gives an attribute of a class a new name.",
			"Saves the current diagram.",
			"Loads a diagram into the editor from your files.",
			"Prints a list of all of the classes in the diagram.",
			"Prints all of the attributes in a class.",
			"Lists all of the relationships between classes in the diagram.",
			"Prints a list of commands and their shorthand versions.\n"
			+ "If the name of a command is supplied as an argument, prints a description of a single command.",
			"Exits the program."
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
					token = "";
				}
			}
			else if (c == ' ' && !quote)
			{
				if (token.length() > 0)
					args.add(token);
				token = "";
			}
			else
			{
				token += c;
			}
		}
		if (token.length() > 0)
			args.add(token);
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