public class Controller {
    private CLIView view;
    private JModel model;

    Controller (CLIView view, JModel model)
    {
        this.view = view;
        this.model = model;
    }

    public boolean doAddClass(String className) 
    {
        try
        {
            return UMLClassHandler.createClass(className);
        }
        catch (Exception e)
        {
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
            view.notifyFail(e.toString());
            return false;
        }
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
        UMLClassHandler.listClasses();
    }
    public void doListClass(String className) 
    {
        try
        {
            UMLClassHandler.listClass(className);
        }
        catch (Exception e)
        {
            view.notifyFail(e.toString());
        }
    }
    public void doListRelationships() 
    {
        UMLClassHandler.listRelationships();
    }
    public void doHelp() 
    {
        Command.help();
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
                if (filepath != null)
                {
                    //data = doLoad(filepath);
                    doLoad(filepath);
                    return true; 
                }
                String again = view.promptForInput("Invalid filepath. Type T to try again, E to exit, or any other key to make a new JSON file instead");
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
     * @param command command with arguments
     * @param action action the user wishes to take
     */
    public void runHelper(Action action, String[] args)
    {
        switch(action) {
            case ADD_CLASS:
                if (args.length == 1)
                    if (doAddClass(args[0]))
                        view.notifySuccess();
                break;
            case REMOVE_CLASS:
                if (args.length == 1)
                    if (doRemoveClass(args[0]))
                        view.notifySuccess();
                break;
            case RENAME_CLASS:
                if (args.length == 2)
                    if (doRenameClass(args[0], args[1]))
                        view.notifySuccess();
                break;
            case ADD_RELATIONSHIP:
                if (args.length == 2)
                    if (doAddRelationship(args[0], args[1]))
                        view.notifySuccess();
                break;
            case REMOVE_RELATIONSHIP:
                if (args.length == 2)
                    if (doRemoveRelationship(args[0], args[1]))
                        view.notifySuccess();
                break;
            case ADD_ATTRIBUTE:
                if (args.length == 2)
                    if (doAddAttribute(args[0], args[1]))
                        view.notifySuccess();
                break;
            case REMOVE_ATTRIVUTE:
                if (args.length == 2)
                    if (doAddAttribute(args[0], args[1]))
                        view.notifySuccess();
                break;
            case RENAME_ATTRIBUTE:
                if (args.length == 3)
                    if (doRenameAttribute(args[0], args[1], args[2]))
                        view.notifySuccess();
                break;
            case SAVE:
                if (args.length == 1)
                {
                    String result = view.promptForInput("Are you sure that you want to save? Y/N");
                    if (result.equals("Y") || result.equals("y"))
                    {
                        doSave(args[0]);
                        view.notifySuccess();
                    }   
                }
                break;
            case LOAD:
                if (args.length == 1)
                {
                    String result = view.promptForInput("Are you sure that you want to load? Y/N");
                    if (result.equals("Y") || result.equals("y"))
                    {
                        doLoad(args[0]);;
                        view.notifySuccess();
                    }
                }
                break;
            case LIST_CLASSES:
                if (args.length == 0)
                    doListClasses();
                break;
            case LIST_CLASS:
                if (args.length == 1)
                    doListClass(args[0]);
                break;
            case LIST_RELATIONSHIPS:
                if (args.length == 0)
                    doListRelationships();
                break;
            case HELP:
                if (args.length == 0)
                    doHelp();
                break;
            case EXIT:
                break;
        }
    }
 }
 