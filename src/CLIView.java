import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIView implements View
{

	// fields
    private Scanner sc = new Scanner(System.in);
    
    // color related fields, constants, etc
    public final String TOGGLE_COLOR_COMMAND = "toggle color";
    public final String TOGGLE_COLOR_SHORTHAND_COMMAND = "tc";
    
    private boolean color = false;
    private String failStyle = "";
    private String successStyle = "";
    private String displayStyle = "";
    private String stopStyle = "";
    
    private final String RED = "\u001B[31m";
    private final String GREEN = "\u001B[32m";
    private final String RESET = "\u001B[0m";
    private final String ITALLICS = "\u001B[3m";
    

    @Override
    public Command nextCommand() 
    {
    	String in = sc.nextLine();
        Command c = Command.parseCommand(in);
        while(c == null)
        {
        	if (in.equals("toggle color") || in.equals("tc"))
        		toggleColor();
        	else
        		System.out.println("Invalid Command");
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
                ans = promptForInput(checkMsg);
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
    
}
