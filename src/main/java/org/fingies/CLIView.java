package org.fingies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jline.reader.*;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

/**
 * Handles the Command Line Interface View
 * @author Nick Hayes, Lincoln Craddock
 */
public class CLIView implements View
{
	// fields
    
    public String caret = ">";

	private Terminal terminal;
    private LineReader lineReader;
    private Completer completer;

	private Controller controller;
    
    // terminal specific commands
    public static final String TOGGLE_COLOR_COMMAND = "toggle color";
    public static final String TOGGLE_COLOR_COMMAND_SHORTHAND = "tc";
    
    public static final String CLEAR_COMMAND = "clear";
    public static final String CLEAR_COMMAND_SHORTHAND = "clr";
    
    /**
	 * An array storing the keywords for the commands specific to the CLI.
	 */
	public static final String[] COMMANDS = {
			TOGGLE_COLOR_COMMAND, CLEAR_COMMAND
	};
	
	/**
	 * A shorter version of each command specific to the CLI.
	 */
	public static final String[] COMMANDS_SHORTHAND = {
			TOGGLE_COLOR_COMMAND_SHORTHAND, CLEAR_COMMAND_SHORTHAND
	};
	
	/**
	 * The expected arguments for each command.
	 */
	public static final String[] COMMAND_ARGS = {
			"",
			""
	};
	
	public static final String[] COMMAND_DESCRIPTION = {
			"Toggles the color mode to be either on or off.\n"
			+ "When the color mode is on, messages in the terminal are formatted in a way that's more pleasing to the eye.\n\n"
			+ "NOTE: Some terminals do not support color mode when it is activated.",
			"Clears the screen of all text."
	};
    
    // color related fields, constants, etc
    private boolean colorMode = true; // enabled by default (set this to false to disable by default)
    private String failStyle;
    private String successStyle;
    private String displayStyle;
    private String commandStyle;
    private String stopStyle;
    
    private final String RED = "\u001B[91m";
    private final String GREEN = "\u001B[92m";
    private final String YELLOW = "\u001B[93m";
    private final String BLUE = "\u001B[94m";
    private final String MAGENTA = "\u001B[95m";
    private final String CYAN = "\u001B[96m";
    
    private final String[] RAINBOW = {RED, YELLOW, GREEN, CYAN, BLUE, MAGENTA};
    
    private final String ITALLICS = "\u001B[3m";
    private final String BOLD = "\u001B[1m";
    
    private final String RESET = "\u001B[0m";
    
    private final String CLEAR = "\u001B[2J";
    
    
    public CLIView()
    {
    	setColorMode(colorMode); // Sets all of the text styles to their initial values.
    
		try {
			terminal = TerminalBuilder.builder().build();
		} catch (IOException e) {
			e.printStackTrace();
		}
        completer = new TabCompletion().getCompleter();
        lineReader = LineReaderBuilder.builder()
                .terminal(terminal)
                .completer(completer)
                .build();
	}

    @Override
    public void run() 
    {
		Command command;
		Action action;
    	do
    	{
    		String in;
    		
    		try
    		{
    			in = lineReader.readLine(caret + " ");
    		}
    		catch (UserInterruptException e)
    		{
    			action = Action.EXIT;
    			controller.runHelper(action, new String[] {});
    			continue;
    		}
    		catch (EndOfFileException e)
    		{
    			action = Action.EXIT;
    			controller.runHelper(action, new String[] {});
    			continue;
    		}
            command = Command.parseCommand(in);
            while (command == null)
            {
            	if (in.equals(TOGGLE_COLOR_COMMAND) || in.equals(TOGGLE_COLOR_COMMAND_SHORTHAND))
            	{
            		colorMode = !colorMode;
            		setColorMode(colorMode);
            		if(colorMode)
            		{
            			notifySuccess();
            			printRainbow("Color mode activated");
            		}
            		else
            			notifySuccess("Color mode disabled");
            	}
            	else if (in.equals(CLEAR_COMMAND) || in.equals(CLEAR_COMMAND_SHORTHAND))
            	{
            		clearScreen();
            		notifySuccess();
            	}
            	else
            		notifyFail("Invalid command");
					in = lineReader.readLine(caret + " ");
                command = Command.parseCommand(in);
            }
			
				action = command.action;
				controller.runHelper(action, command.arguments);
			
    	} while (!action.equals(Action.EXIT));
    }

    @Override
    public String promptForInput(String message) 
    {
        try
        {
        	return lineReader.readLine(message + " "); // appends an extra space to separate the message from the user's input
        }
        catch (UserInterruptException e)
		{
			return "";
		}
		catch (EndOfFileException e)
		{
			return "";
		}
    }

    @Override
    public List<String> promptForInput(List<String> messages) 
    {
        List<String> result = new ArrayList<String>();
        for(String m : messages)
        {
            result.add(promptForInput(m));
        }
        return result;
    }

   
    @Override
    public List<String> promptForInput(List<String> messages, List<InputCheck> checks) 
    {
        List<String> result = new ArrayList<String>();
        for(int i = 0; i < messages.size(); ++i)
        {
            String ans = promptForInput(messages.get(i));
            String checkMsg = checks.get(i).check(ans); // This will either be "" or "message"
            while(!checkMsg.equals("")) // This loop will keep prompting the user until they input something that satisfies the check
            {
            	notifyFail(checkMsg);
                ans = promptForInput(messages.get(i));
                checkMsg = checks.get(i).check(ans);
            }
            result.add(ans);
        }
        return result;
    }

    @Override
    public void notifySuccess() 
    {
        // System.out.println("Successful command"); // don't print anything
    }

    @Override
    public void notifySuccess(String message) 
    {
        System.out.println(successStyle + message + stopStyle);
    }

    @Override
    public void notifyFail(String message) 
    {
        System.out.println(failStyle + message + stopStyle);
    }

    @Override
    public void display(String message) {
        System.out.println(displayStyle + message + stopStyle);
    }
	
    @Override
	public void help()
	{		
    	// print all commands from Command class
		for (int i = 0; i < Command.COMMANDS.length; ++i)
		{
			System.out.println(commandStyle + Command.COMMANDS[i] + stopStyle + " " + Command.COMMAND_ARGS[i] + "\n"
					+ commandStyle + Command.COMMANDS_SHORTHAND[i] + stopStyle + " " + Command.COMMAND_ARGS[i] + "\n");
		}
		// print all commands from CLIView
		for (int i = 0; i < COMMANDS.length; ++i)
		{
			System.out.println(commandStyle + COMMANDS[i] + stopStyle + " " + COMMAND_ARGS[i] + "\n"
					+ commandStyle + COMMANDS_SHORTHAND[i] + stopStyle + " " + COMMAND_ARGS[i] + (i == COMMANDS.length - 1 ? "" : "\n"));
		}
	}
    
    @Override
	public void help(String command)
	{
    	int idx = indexOfCommand(command);
		if (idx != -1) // then print this command from the CLIView
			System.out.println(commandStyle + COMMANDS[idx] + stopStyle + " " + COMMAND_ARGS[idx] + "\n"
					+ commandStyle + COMMANDS_SHORTHAND[idx] + stopStyle + " " + COMMAND_ARGS[idx] + "\n\n"
							+ COMMAND_DESCRIPTION[idx]);
		else
		{
			idx = Command.indexOfCommand(command);
			if (idx != -1) // then print this command from the Command class
				System.out.println(commandStyle + Command.COMMANDS[idx] + stopStyle + " " + Command.COMMAND_ARGS[idx] + "\n"
						+ commandStyle + Command.COMMANDS_SHORTHAND[idx] + stopStyle + " " + Command.COMMAND_ARGS[idx] + "\n\n"
								+ Command.COMMAND_DESCRIPTION[idx]);
			else // command doesn't exist
				notifyFail("The command \"" + command + "\" does not exist.");
		}
	}
    
    private int indexOfCommand(String command)
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
     * Updates the text styles to the current color mode.
     * 
     * @param m Whether color mode is enabled or not.
     */
    private void setColorMode(boolean m)
    {
    	if (m)
    	{
    		failStyle = RED + ITALLICS;
    		successStyle = GREEN + ITALLICS;
    		displayStyle = ITALLICS;
    		commandStyle = CYAN + BOLD;
    		stopStyle = RESET;
    	}
    	else
    	{
    		failStyle = "";
    		successStyle = "";
    		displayStyle = "";
    		commandStyle = "";
    		stopStyle = "";
    	}
    }
    
    /**
     * Clears the screen of all text.
     */
    private void clearScreen()
    {
    	System.out.println(CLEAR);
    }
    
    /**
     * Colors a string of text rainbow.
     * 
     * @param str The text to color.
     * @return The same string but colored rainbow.
     */
    private void printRainbow(String str)
    {
    	String result = BOLD;
    	int idx = 0;
    	for (char c : str.toCharArray())
    	{
    		if (idx >= RAINBOW.length)
    			idx = 0;
    		result += RAINBOW[idx++] + c;
    	}
    	System.out.println(result + RESET);
    }

    @Override
	public void setController(Controller c)
	{
		controller = c;
	}
	
	@Override
	public String promptForSaveInput(String message) 
	{
		return promptForInput(message);
	}

	@Override
	public String promptForOpenInput(String message) {
		return promptForInput(message);
	}
    
}