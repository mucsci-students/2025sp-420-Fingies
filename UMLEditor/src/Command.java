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
	
	Action action;
	String[] arguments;
	
	public static final String[] COMMANDS = {
		    "add class", "remove class", "rename class",
		    "add relationship", "remove relationship", "add attribute",
		    "remove attribute", "rename attribute", "save",
		    "load", "list classes", "list class",
		    "list relationships", "help", "exit"
		};
	
	public static final String[] COMMANDS_SHORTHAND = {
		    "addc", "rmc", "rnc",         	   // add class, remove class, rename class
		    "addr", "rmr", "adda",             // add relationship, remove relationship, add attribute
		    "rma", "rna", "sv",                // remove attribute, rename attribute, save
		    "ld", "listcls", "listcl",         // load, list classes, list class
		    "listr", "h", "quit"               // list relationships, help, exit
		};
	
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
		    COMMANDS[14] + " \n" + COMMANDS_SHORTHAND[14] + " "
		};
	
	Command (Action a, String[] args)
	{
		action = a;
		arguments = args;
	}
	
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
		
		// parse arguments
		if (input.length() > cmdLen)
			input = input.substring(cmdLen + 1);
		else
			input = "";
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
		return action + ": " + Arrays.toString(arguments);
	}

}