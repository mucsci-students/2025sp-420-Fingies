
/**
 * Controller for UML Editor, handles user input
 * @author kdichter
 */
public class Controller {
    private CLIView view;
    private JModel model;
    private boolean madeChange;
    private boolean hasSaved;

    Controller (CLIView view, JModel model)
    {
        this.view = view;
        this.model = model;
        madeChange = false;
        hasSaved = false;
    }

    public boolean doAddClass(String className) 
    {
        try
        {
            return UMLClassHandler.createClass(className);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRemoveClass(String className) 
    {
        try
        {
            return UMLClassHandler.removeClass(className);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRenameClass(String className, String newName) 
    {
        try
        {
            return UMLClassHandler.renameClass(className, newName);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doAddRelationship(String srcClass, String destClass) 
    {
        try
        {
            return UMLClassHandler.addRelationship(srcClass, destClass);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRemoveRelationship(String srcClass, String destClass) 
    {
        try
        {
            return UMLClassHandler.removeRelationship(srcClass, destClass);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doAddAttribute(String srcClass, String attribute) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).addAttribute(attribute);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRemoveAttribute(String srcClass, String attribute) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).removeAttribute(attribute);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRenameAttribute(String srcClass, String oldAttribute, String newAttribute) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).renameAttribute(oldAttribute, newAttribute);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doSave() 
    {
        return model.saveData();
    }

    public boolean doSave(String filepath) 
    {
        return model.saveData(filepath);
    }

    public UMLClassHandler doLoad(String filepath) 
    {
        return model.loadData(filepath);
    }

    public void doListClasses() 
    {
        try
        {
            String lst = UMLClassHandler.listClasses();
            view.display(lst);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
        }
    }
    public void doListClass(String className) 
    {
        try
        {
        	UMLClass c = UMLClassHandler.getClass(className);
        	String lst = UMLClassHandler.listClass(c);
        	view.display(lst);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
        }
    }
    public void doListRelationships() 
    {
    	String lst = UMLClassHandler.listRelationships();
        view.display(lst);
    }
    
    public void doHelp() 
    {
        view.help();
    }
    
    public void doSpecificCommandHelp(String command)
    {
    	view.help(command);
    }

    public void saveLoop()
    {
        while (true)
        {
            String input = view.promptForInput("Enter a valid filepath to save to or type EXIT to quit the program.");
            if (input.toUpperCase().equals("EXIT"))
                break;
            if (doSave(input))
            {
                madeChange = false;
                hasSaved = true;
                view.notifySuccess("Successfully loaded your file.");
                break;
            }
            else
            {
                view.notifyFail("Invalid filepath. Filepath should look something like this:");
                view.notifySuccess("(C:\\Users\\Zoppetti\\Demos\\Test.txt)");
            }
        }
    }

    public void loadLoop()
    {
        while (true)
        {
            String input = view.promptForInput("Enter a valid filepath to save to or type EXIT to quit the program.");
            if (input.toUpperCase().equals("EXIT"))
                break;
            if (doSave(input))
            {
                madeChange = false;
                hasSaved = true;
                view.notifySuccess("Successfully loaded your file.");
                break;
            }
            else
            {
                view.notifyFail("Invalid filepath. Filepath should look something like this:");
                view.notifySuccess("(C:\\Users\\Zoppetti\\Demos\\Test.txt)");
            }
        }
    }

    public boolean loadCheck(String filepath)
    {
        if (doLoad(filepath) != null)
        {
            hasSaved = true;
            madeChange = false;
            view.notifySuccess("Successfully loaded your file");
            return true;
        }
        else
        {
            view.notifyFail("Invalid filepath provided. Filepath should look something like this:");
            return false;
        }
    }

    /**
     * Promts the user to either load in an existing JSON file with data or create a new one
     */
    public boolean getData()
    {
        String result = view.promptForInput("Do you want to load a JSON file for storing your UML diagram? Type Y for yes or any other key to make a new JSON file instead");
        if (result.equals("Y") || result.equals("y"))
        {
            while (true)
            {
                String filepath = view.promptForInput("Enter a valid filepath");
                if (loadCheck(filepath))
                {
                    return true; 
                }
                view.notifySuccess("(C:\\Users\\Zoppetti\\Demos\\Test.txt)");
                String again = view.promptForInput("Type T to try again, E to exit, or any other key to make a new JSON file instead");
                if (again.equals("E") && !again.equals("e"))
                    return false;
                else if (!again.equals("T") && !again.equals("t"))
                    break;
            }
        }
        //data = new UMLClassHandler();
        return true;
        
    }

    /**
     * Runs the program by infinitely looping and executing commands until the user types EXIT
     */
    public void run ()
    {
        if(!getData())
            return;
        Command command;
        Action action;
        do {
            command = view.nextCommand();
            action = command.action;   
            runHelper (action, command.arguments);
        } while (!action.equals(Action.EXIT));
    }

    /**
     * Executes the action with the commands arguments as inputs
     * 
     * @param command command with arguments
     * @param action action the user wishes to take
     */
    public void runHelper(Action action, String[] args)
    {
        switch(action) {
            case ADD_CLASS:
                if (args.length == 1)
                {
                    if (doAddClass(args[0]))
                    {
                        view.notifySuccess("Successfully added class " + args[0]);
                        madeChange = true;
                    }
                    else {
                        view.notifyFail("Failed to add class " + args[0]);
                    }
                        
                }
                else
                {
                	view.notifyFail("Add class should have exactly 1 argument.");
                }
                break;
            case REMOVE_CLASS:
                if (args.length == 1)
                {
                    if (doRemoveClass(args[0]))
                    {
                        view.notifySuccess("Successfully removed class " + args[0]);
                        madeChange = true;
                    }
                    else {
                        view.notifyFail("Failed to remove class " + args[0]);
                    }
                        
                }
                else
                {
                	view.notifyFail("Remove class should have exactly 1 argument.");
                }
                break;
            case RENAME_CLASS:
                if (args.length == 2)
                {
                    if (doRenameClass(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully renamed class " + args[0] + " to " + args[1]);
                        madeChange = true;
                    }
                    else
                    {
                        view.notifyFail("Failed to rename class " + args[0] + " to " + args[1]);
                    }
                }
                else
                {
                	view.notifyFail("Rename class should have exactly 2 arguments.");
                }
                break;
            case ADD_RELATIONSHIP:
                if (args.length == 2)
                {
                    if (doAddRelationship(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully added relationship " + args[0] + " --> " + args[1]);
                        madeChange = true;
                    }
                }
                else
                {
                	view.notifyFail("Add relationship should have exactly 2 arguments.");
                }
                break;
            case REMOVE_RELATIONSHIP:
                if (args.length == 2)
                {
                    if (doRemoveRelationship(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully removed relationship " + args[0] + " --> " + args[1]);
                        madeChange = true;
                    }
                    else
                    {
                        view.notifyFail("Failed to remove relationship " + args[0] + " --> " + args[1]);
                    }
                }
                else
                {
                	view.notifyFail("Remove relationship should have exactly 2 arguments.");
                }
                break;
            case ADD_ATTRIBUTE:
                if (args.length == 2)
                {
                    if (doAddAttribute(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully added attribute " + args[1] + " to class " + args[0]);
                        madeChange = true;
                    }
                    else
                    {
                        view.notifyFail("Attribute couldn't be added.");
                    }
                }
                else
                {
                	view.notifyFail("Add attribute should have exactly 2 arguments.");
                }
                break;
            case REMOVE_ATTRIBUTE:
                if (args.length == 2)
                {
                    if (doRemoveAttribute(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully removed attribute " + args[1] + " from class " + args[0]);
                        madeChange = true;
                    }
                    else {
                        view.notifyFail("Failed to remove attribute " + args[1] + " from class " + args[0]);
                    }
                }
                else
                {
                	view.notifyFail("Remove attribute should have exactly 2 arguments.");
                }
                break;
            case RENAME_ATTRIBUTE:
                if (args.length == 3)
                {
                    if (doRenameAttribute(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully renamed attribute " + args[1] + " to " + args[2] + " in class " + args[0]);
                        madeChange = true;
                    }
                    else
                    {
                        view.notifyFail("Failed to rename attribute " + args[1] + " to " + args[2] + " in class " + args[0]);
                    }
                }
                else
                {
                	view.notifyFail("Rename attribute should have exactly 3 arguments.");
                }
                break;
            case SAVE:
                if (args.length == 0)
                {
                    if (hasSaved)
                    {
                        doSave();
                        madeChange = false;
                        view.notifySuccess("Successfully saved your file");
                    }
                    else
                    {
                        view.notifyFail("Cannot save a file with no previous filepath provided.");
                    }
                }
                else if (args.length == 1)
                {
                    if (doSave(args[0]))
                    {
                        hasSaved = true;
                        madeChange = false;
                        view.notifySuccess("Successfully saved your file");
                    }
                    else
                    {
                	    view.notifyFail("Invalid filepath provided.");
                    }
                }
                else
                {
                	view.notifyFail("Save should have either 0 or 1 arguments.");
                }
                break;
            case LOAD:
                if (args.length == 1)
                {
                    if (madeChange)
                    {
                        String result = view.promptForInput("Are you sure that you want to load without saving? Type Y for yes or any other key to save before loading");
                        if (result.toLowerCase().equals("y"))
                        {
                            loadCheck(args[0]);

                            // doLoad(args[0]);
                            // hasSaved = true;
                            // madeChange = false;
                            // view.notifySuccess("Successfully loaded your file");
                        }
                        else
                        {
                            saveLoop();
                            loadCheck(args[0]);
                            // doLoad(args[0]);
                            // view.notifySuccess("Successfully loaded your file.");
                        }
                    }
                    else
                    {
                        if (model.fileExist(args[0]))
                        {
                            doLoad(args[0]);
                            hasSaved = true;
                            view.notifySuccess("Successfully loaded your file.");
                        }
                        else
                        {
                            view.notifyFail("Invalid filepath.");
                        }
                    }
                }
                else
                {
                	view.notifyFail("Load should have exactly 1 argument.");
                }
                break;
            case LIST_CLASSES:
                if (args.length == 0)
                {
                    doListClasses();
                }
                else
                {
                	view.notifyFail("List classes shouldn't have any arguments.");
                }
                break;
            case LIST_CLASS:
                if (args.length == 1)
                {
                    doListClass(args[0]);
                }
                else
                {
                	view.notifyFail("List class should have exactly 1 argument.");
                }
                break;
            case LIST_RELATIONSHIPS:
                if (args.length == 0)
                {
                    doListRelationships();
                }
                else
                {
                	view.notifyFail("List relationships shouldn't have any arguments.");
                }
                break;
            case HELP:
                if (args.length == 0)
                {
                    doHelp();
                }
                else if (args.length == 1)
                {
                	doSpecificCommandHelp(args[0]);
                }
                else
                {
                	view.notifyFail("Too many arguments. Arguments with spaces require quotes.");
                }
                break;
            case EXIT:
                if (madeChange)
                {
                    String result = view.promptForInput("Are you sure that you want to exit without saving? Type Y for yes or any other key to save before exiting");
                    if (!result.toLowerCase().equals("y"))
                    {
                        //needs to be changed for overload with 0 arguments ... could prompt for a new path
                        if (!hasSaved)
                        {
                            saveLoop();
                        }
                        else
                        {
                            doSave();
                        }
                    }
                }
                break;
        }
    }
 }
 