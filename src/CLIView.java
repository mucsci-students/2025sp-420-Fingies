import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLIView implements View
{

    private Scanner sc = new Scanner(System.in);

    @Override
    public Command nextCommand() 
    {
        Command c = Command.parseCommand(sc.nextLine());
        while(c == null)
        {
            System.out.println("Invalid Command");
            c = Command.parseCommand(sc.nextLine());
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
            }
            result.add(ans);
        }
        return result;
    }

    @Override
    public void notifySuccess() 
    {
        System.out.println("Successful command");
    }

    @Override
    public void notifySuccess(String message) 
    {
        System.out.println(message);
    }

    @Override
    public void notifyFail(String message) 
    {
        System.out.println(message);
    }

    @Override
    public void display(String message) {
        System.out.println(message);
    }
    
}
