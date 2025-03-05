import java.util.List;
import java.util.Arrays;

/**
 * Controller for UML Editor, handles user input
 * @author kdichter
 */
public class Controller {
    private View view;
    private JModel model;
    private boolean madeChange;
    private boolean hasSaved;

    Controller (View view, JModel model)
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

    public boolean doAddRelationship(String srcClass, String destClass, String type) 
    {
        try
        {
            RelationshipType rType = RelationshipType.valueOf(type);
            return RelationshipHandler.addRelationship(srcClass, destClass, rType);
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
            return RelationshipHandler.removeRelationship(srcClass, destClass);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doChangeRelationshipType(String srcClass, String destClass, String newType)
    {
        try
        {
            RelationshipType rType = RelationshipType.valueOf(newType);
            return RelationshipHandler.changeRelationshipType(srcClass, destClass, rType);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doAddField(String srcClass, String field) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).addField(field);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doAddMethod(String srcClass, String method, List<String> parameters) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).addMethod(method, parameters);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRemoveField(String srcClass, String field) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).removeField(field);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRemoveMethod(String srcClass, String method, String paramNum) 
    {
        try
        {
            int arity = Integer.parseInt(paramNum);
            return UMLClassHandler.getClass(srcClass).removeMethod(method, arity);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRenameField(String srcClass, String oldField, String newField) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).renameField(oldField, newField);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRenameMethod(String srcClass, String oldMethod, String paramNum, String newMethod) 
    {
        try
        {
            int arity = Integer.parseInt(paramNum);
            return UMLClassHandler.getClass(srcClass).renameMethod(oldMethod, arity, newMethod);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }
    
    public boolean doAddParameters(String srcClass, String method, String paramNum, List<String> params)
    {
        try
        {
            int arity = Integer.parseInt(paramNum);
            return UMLClassHandler.getClass(srcClass).addParameters(method, arity, params);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }

    public boolean doRemoveParameters(String srcClass, String method, String paramNum, List<String> params)
    {
        try
        {
            int arity = Integer.parseInt(paramNum);
            return UMLClassHandler.getClass(srcClass).removeParameters(method, arity, params);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.toString());
            return false;
        }
    }
    
    public boolean doRenameParameter(String srcClass, String method, String paramNum, String oldParam, String newParam)
    {
        try
        {
            int arity = Integer.parseInt(paramNum);
            return UMLClassHandler.getClass(srcClass).getMethod(method, arity).renameParameter(oldParam, newParam);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
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
    	String lst = RelationshipHandler.listRelationships();
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
     * Prompts the user to either load in an existing JSON file with data or create a new one
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
    // public void run ()
    // {
    // 	//TODO Probably remove this method as we are adding functionality into the Views
    //     if(!getData())
    //         return;
    //     Command command;
    //     Action action;
    //     do {
    //         command = view.nextCommand();
    //         action = command.action;   
    //         runHelper (action, command.arguments);
    //     } while (!action.equals(Action.EXIT));
    // }

    /**
     * Executes the action with the commands arguments as inputs
     * 
     * @param command command with arguments
     * @param action action the user wishes to take
     */
    public boolean runHelper(Action action, String[] args)
    {
    	//TODO Refactor attribute cases and update with fields, and methods
        switch(action) {
            case ADD_CLASS:
                if (args.length == 1)
                {
                    if (doAddClass(args[0]))
                    {
                        view.notifySuccess("Successfully added class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        view.notifyFail("Failed to add class " + args[0]);
                        return false;
                    }
                        
                }
                else
                {
                	view.notifyFail("Add class should have exactly 1 argument.");
                    return false;
                }
            case REMOVE_CLASS:
                if (args.length == 1)
                {
                    if (doRemoveClass(args[0]))
                    {
                        view.notifySuccess("Successfully removed class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        view.notifyFail("Failed to remove class " + args[0]);
                        return false;
                    }
                        
                }
                else
                {
                	view.notifyFail("Remove class should have exactly 1 argument.");
                    return false;
                }
            case RENAME_CLASS:
                if (args.length == 2)
                {
                    if (doRenameClass(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully renamed class " + args[0] + " to " + args[1]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Failed to rename class " + args[0] + " to " + args[1]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Rename class should have exactly 2 arguments.");
                    return false;
                }
            case ADD_RELATIONSHIP:
                if (args.length == 3)
                {

                    if (doAddRelationship(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully added relationship " + args[0] + " --> " + args[1] + " of type " + args[3]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Failed to add relationship " + args[0] + " --> " + args[1] + " of type " + args[3]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Add relationship should have exactly 3 arguments.");
                    return false;
                }
            case REMOVE_RELATIONSHIP:
                if (args.length == 2)
                {
                    if (doRemoveRelationship(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully removed relationship " + args[0] + " --> " + args[1]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Failed to remove relationship " + args[0] + " --> " + args[1]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Remove relationship should have exactly 2 arguments.");
                    return false;
                }
            case ADD_METHOD:
                if (args.length >= 2)
                {
                    if (doAddMethod(args[0], args[1], getPartialListFromArray(args, 2, args.length)))
                    {
                        view.notifySuccess("Successfully added method " + args[1] + " with argument(s) " + getPartialListFromArray(args, 2, args.length) + " to class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Method couldn't be added.");
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Add Method should have 2 or more arguments.");
                    return false;
                }
            case REMOVE_METHOD:
                if (args.length == 3)
                {
                    if (doRemoveMethod(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully removed method " + args[1] + " with arity " + args[2] + " from class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        view.notifyFail("Failed to remove method " + args[1] + " with arity " + args[2] + " from class " + args[0]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Remove method should have exactly 3 arguments.");
                    return false;
                }
            case RENAME_METHOD:
                if (args.length == 4)
                {
                    if (doRenameMethod(args[0], args[1], args[2], args[3]))
                    {
                        view.notifySuccess("Successfully renamed method " + args[1] + " with arity " + args[2] + " to " + args[3] + " in class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Failed to rename method " + args[1] + " with arity " + args[2] + " to " + args[3] + " in class " + args[0]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Rename method should have exactly 4 arguments.");
                    return false;
                }
            case ADD_FIELD:
                if (args.length == 2)
                {
                    if (doAddField(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully added field " + args[1] + " to class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Field couldn't be added.");
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Add field should have exactly 2 arguments.");
                    return false;
                }
            case REMOVE_FIELD:
                if (args.length == 2)
                {
                    if (doRemoveField(args[0], args[1]))
                    {
                        view.notifySuccess("Successfully removed field " + args[1] + " from class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        view.notifyFail("Failed to remove field " + args[1] + " from class " + args[0]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Remove field should have exactly 2 arguments.");
                    return false;
                }
            case RENAME_FIELD:
                if (args.length == 3)
                {
                    if (doRenameField(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully renamed field " + args[1] + " to " + args[2] + " in class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Failed to rename field " + args[1] + " to " + args[2] + " in class " + args[0]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Rename field should have exactly 3 arguments.");
                    return false;
                }
            case ADD_PARAMETERS:
                if (args.length >= 4) {
                    List<String> params = getPartialListFromArray(args, 3, args.length);
                    if (doAddParameters(args[0], args[1], args[2], params))
                    {
                        view.notifySuccess("Succesfully added parameter(s): " + params + " to method " + args[1] + " with arity " + args[2] + "from class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        view.notifyFail("Failed to remove parameter(s): " + params + " to method " + args[1] + " with arity " + args[2] + "from class " + args[0]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Add Parameters should have 4 or more parameters.");
                    return false;
                }
            case REMOVE_PARAMETERS:
                if (args.length >= 4) {
                    List<String> params = getPartialListFromArray(args, 3, args.length);
                    if (doRemoveParameters(args[0], args[1], args[2], params))
                    {
                        view.notifySuccess("Succesfully removed parameter(s): " + params + " from method " + args[1] + " with arity " + args[2] + "from class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        view.notifyFail("Failed to remove parameter(s): " + params + " from method " + args[1] + " with arity " + args[2] + "from class " + args[0]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Remove Parameters should have 4 or more parameters.");
                    return false;
                }
            // this should be called RENAME_PARAMETER
            case CHANGE_PARAMETER:
                if (args.length == 5) {
                    if (doRenameParameter(args[0], args[1], args[2], args[3], args[4]))
                    {
                        view.notifySuccess("Successfully renamed parameter " + args[3] + " with arity " + args[4] + " of method " + args[1] + " of class " + args[0] + " to " + args[4]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        view.notifyFail("Failed to rename parameter " + args[3] + " with arity " + args[4] + " of method " + args[1] + " of class " + args[0] + " to " + args[4]);
                        return false;
                    }
                }
            case SAVE:
                if (args.length == 0)
                {
                    if (hasSaved)
                    {
                        doSave();
                        madeChange = false;
                        view.notifySuccess("Successfully saved your file");
                        return true;
                    }
                    else
                    {
                        args = new String[] {view.promptForSaveInput("Please designate a filepath to save to")};
                        if (doSave(args[0]))
                        {
                            hasSaved = true;
                            madeChange = false;
                            view.notifySuccess("Successfully saved your file");
                            return true;
                        }
                        else
                        {
                    	    view.notifyFail("Invalid filepath provided.");
                            return false;
                        }
                    }
                }
                else if (args.length == 1)
                {
                    if (doSave(args[0]))
                    {
                        hasSaved = true;
                        madeChange = false;
                        view.notifySuccess("Successfully saved your file");
                        return true;
                    }
                    else
                    {
                	    view.notifyFail("Invalid filepath provided.");
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Save should have either 0 or 1 arguments.");
                    return false;
                }
            case LOAD:
                if (args.length == 1)
                {
                    if (madeChange)
                    {
                        String result = view.promptForInput("Are you sure that you want to load without saving? Type Y for yes or any other key to save before loading");
                        if (result.toLowerCase().equals("y"))
                        {
                             return loadCheck(args[0]);

                            // doLoad(args[0]);
                            // hasSaved = true;
                            // madeChange = false;
                            // view.notifySuccess("Successfully loaded your file");
                        }
                        else
                        {
                            saveLoop();
                            return loadCheck(args[0]);
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
                            return true;
                        }
                        else
                        {
                            view.notifyFail("Invalid filepath.");
                            return false;
                        }
                    }
                }
                else
                {
                	view.notifyFail("Load should have exactly 1 argument.");
                    return false;
                }
            case LIST_CLASSES:
                if (args.length == 0)
                {
                    doListClasses();
                    return true;
                }
                else
                {
                	view.notifyFail("List classes shouldn't have any arguments.");
                    return false;
                }
            case LIST_CLASS:
                if (args.length == 1)
                {
                    doListClass(args[0]);
                    return true;
                }
                else
                {
                	view.notifyFail("List class should have exactly 1 argument.");
                    return false;
                }
            case LIST_RELATIONSHIPS:
                if (args.length == 0)
                {
                    doListRelationships();
                    return true;
                }
                else
                {
                	view.notifyFail("List relationships shouldn't have any arguments.");
                    return false;
                }
            case HELP:
                if (args.length == 0)
                {
                    doHelp();
                    return true;
                }
                else if (args.length == 1)
                {
                	doSpecificCommandHelp(args[0]);
                    return true;
                }
                else
                {
                	view.notifyFail("Too many arguments. Arguments with spaces require quotes.");
                    return true;
                }
            case EXIT:
                if (args.length != 0) {
                    view.notifyFail("Failed to exit program.");
                    return false;
                }
                else if (madeChange)
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
                        return true;
                    }
                    
                }
                return true;
        }
        return false;
    }

    public List<String> getPartialListFromArray(String[] array, int start, int end)
    {
        return Arrays.asList(Arrays.copyOfRange(array, start, end));
    }

 }
 