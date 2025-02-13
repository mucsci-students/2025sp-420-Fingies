import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIView implements View
{

	// fields
    private Scanner sc = new Scanner(System.in);
    
    public String caret = ">";
    
    // termianl specific commands
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
	 * An array of strings describing the format for every CLI-specific command.
	 */
	public static final String[] COMMAND_FORMAT = {
			COMMANDS[0] + " \n" + COMMANDS_SHORTHAND[0] + " ",
		    COMMANDS[1] + " \n" + COMMANDS_SHORTHAND[1] + " " 
	};
    
    // color related fields, constants, etc
    private boolean color = false;
    private String failStyle = "";
    private String successStyle = "";
    private String displayStyle = "";
    private String stopStyle = "";
    
    private final String RED = "\u001B[91m";
    private final String GREEN = "\u001B[92m";
    private final String YELLOW = "\u001B[93m";
    private final String BLUE = "\u001B[94m";
    private final String MAGENTA = "\u001B[95m";
    private final String CYAN = "\u001B[96m";
    
    private final String[] RAINBOW = {RED, YELLOW, GREEN, CYAN, BLUE, MAGENTA};
    
    private final String ITALLICS = "\u001B[3m";
    
    private final String RESET = "\u001B[0m";
    
    private final String CLEAR = "\u001B[2J";
    

    @Override
    public Command nextCommand() 
    {
    	System.out.print(caret + " ");
    	String in = sc.nextLine();
        Command c = Command.parseCommand(in);
        while(c == null)
        {
        	if (in.equals(TOGGLE_COLOR_COMMAND) || in.equals(TOGGLE_COLOR_COMMAND_SHORTHAND))
        	{
        		toggleColor();
        		if(color)
        		{
        			notifySuccess();
        			System.out.println(rainbowify("Color mode activated"));
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
        		notifyFail("Invalid comamnd");
        	System.out.print(caret + " ");
        	in = sc.nextLine();
            c = Command.parseCommand(in);
        }
        return c;
    }

    @Override
    public String promptForInput(String message) 
    {
        System.out.println(message);
        return sc.nextLine();
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
                ans = promptForInput("");
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
	public int indexOfCommand(String command)
	{
		for (int i = 0; i < COMMANDS.length; ++i)
			if (COMMANDS[i].equals(command))
				return i;
		
		for (int i = 0; i < COMMANDS_SHORTHAND.length; ++i)
			if (COMMANDS_SHORTHAND[i].equals(command))
				return i;
		
		return -1;
	}
	
    @Override
	public String help()
	{
		String str = "";
		for (String s : COMMAND_FORMAT)
		{
			str += s + "\n\n";
		}
		return str.substring(0, str.length() - 2); // trim the last two \n off the string
	}
    
    @Override
	public String help(int index)
	{
		return COMMAND_FORMAT[index];
	}
    
    
    /**
     * Turns on/off color text, and other visual niceties in the terminal.
     */
    private void toggleColor()
    {
    	color = ! color;
    	if (color)
    	{
    		failStyle = RED + ITALLICS;
    		successStyle = GREEN + ITALLICS;
    		displayStyle = ITALLICS;
    		stopStyle = RESET;
    	}
    	else
    	{
    		failStyle = "";
    		successStyle = "";
    		displayStyle = "";
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
    private String rainbowify(String str)
    {
    	String result = "";
    	int idx = 0;
    	for (char c : str.toCharArray())
    	{
    		if (idx >= RAINBOW.length)
    			idx = 0;
    		result += RAINBOW[idx++] + c;
    	}
    	return result + RESET;
    }
    
}
