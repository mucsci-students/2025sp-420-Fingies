import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * An object that represents a command that the user can make of the program.
 * 
 * Contains an enum representing the high-level action they want to take, and
 * a list of arguments given by the user.
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
		    "list relationships", "help", "exit",
		};
	
	/**
	 * A shorter version of each keyword that can be used instead.
	 */
	public static final String[] COMMANDS_SHORTHAND = {
		    "addc", "rmc", "rnc",         	   // add class, remove class, rename class
		    "addr", "rmr", "adda",             // add relationship, remove relationship, add attribute
		    "rma", "rna", "sv",                // remove attribute, rename attribute, save
		    "ld", "listcls", "listcl",         // load, list classes, list class
		    "listr", "h", "quit",              // list relationships, help, exit
		};
	
	/**
	 * An array of strings describing the format for every command.
	 */
	public static final String[] COMMAND_FORMAT = {
		    COMMANDS[0] + " CLASS_NAME \n" + COMMANDS_SHORTHAND[0] + " CLASS_NAME ",
		    COMMANDS[1] + " CLASS_NAME \n" + COMMANDS_SHORTHAND[1] + " CLASS_NAME ",
		    COMMANDS[2] + " CLASS_NAME NEW_NAME \n" + COMMANDS_SHORTHAND[2] + " CLASS_NAME NEW_NAME ",
		    COMMANDS[3] + " SRC_CLASS DEST_CLASS \n" + COMMANDS_SHORTHAND[3] + " SRC_CLASS DEST_CLASS ",
		    COMMANDS[4] + " SRC_CLASS DEST_CLASS \n" + COMMANDS_SHORTHAND[4] + " SRC_CLASS DEST_CLASS ",
		    COMMANDS[5] + " CLASS_NAME ATTRIBUTE \n" + COMMANDS_SHORTHAND[5] + " CLASS_NAME ATTRIBUTE ",
		    COMMANDS[6] + " CLASS_NAME ATTRIBUTE \n" + COMMANDS_SHORTHAND[6] + " CLASS_NAME ATTRIBUTE ",
		    COMMANDS[7] + " CLASS_NAME ATTRIBUTE_NAME NEW_NAME \n" + COMMANDS_SHORTHAND[7] + " CLASS_NAME ATTRIBUTE_NAME NEW_NAME ",
		    COMMANDS[8] + " \n" + COMMANDS_SHORTHAND[8] + " ",
		    COMMANDS[9] + " FILE_PATH \n" + COMMANDS_SHORTHAND[9] + " FILE_PATH ",
		    COMMANDS[10] + " \n" + COMMANDS_SHORTHAND[10] + " ",
		    COMMANDS[11] + " CLASS_NAME \n" + COMMANDS_SHORTHAND[11] + " CLASS_NAME ",
		    COMMANDS[12] + " \n" + COMMANDS_SHORTHAND[12] + " ",
		    COMMANDS[13] + " [ COMMAND ] \n" + COMMANDS_SHORTHAND[13] + " [ COMMAND ] ",
		    COMMANDS[14] + " \n" + COMMANDS_SHORTHAND[14] + " ",
		};
	
	/**
	 * An array storing the keywords for the commands specific to the CLI.
	 */
	public static final String[] CLI_COMMANDS = {
			"toggle color", "clear"
	};
	
	/**
	 * A shorter version of each command specific to the CLI.
	 */
	public static final String[] CLI_COMMANDS_SHORTHAND = {
			"tc", "clr"
	};
	
	/**
	 * An array of strings describing the format for every CLI-specific command.
	 */
	public static final String[] CLI_COMMAND_FORMAT = {
			CLI_COMMANDS[0] + " \n" + CLI_COMMANDS_SHORTHAND[0] + " ",
		    CLI_COMMANDS[1] + " \n" + CLI_COMMANDS_SHORTHAND[1] + " " 
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
	 * Returns a string containing a description for the format of every command.
	 * 
	 * @return A string containing a description of the format for every command.
	 */
	public static String help()
	{
		String str = "";
		for (String s : COMMAND_FORMAT)
		{
			str += s + "\n\n";
		}
		return str.substring(0, str.length() - 2); // trim the last two \n off the string
	}
	
	/**
	 * Returns a string containing a description of the format for a specific command.
	 * 
	 * @param index The index of the command.
	 * @return A string containing a description of the format for a specific command.
	 */
	public static String help(int index)
	{
		return COMMAND_FORMAT[index];
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