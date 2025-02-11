import java.util.HashSet;

public class Controller {
    private CLIView view;
    private JModel model;
    UMLClassHandler data;

    Controller (CLIView view, JModel model)
    {
        this.view = view;
        this.model = model;
        data = null;
    }

    public boolean doAddClass(String className) 
    {
        return UMLClassHandler.createClass(className);
    }

    public boolean doRemoveClass(String className) 
    {
        return UMLClassHandler.removeClass(className);
    }

    public boolean doRenameClass(String className, String newName) 
    {
        return UMLClassHandler.renameClass(className, newName);
    }

    public void doAddRelationship(String srcClass, String destClass) 
    {
        UMLClassHandler.addRelationship(srcClass, destClass);
    }

    public void doRemoveRelationship(String srcClass, String destClass) 
    {
        UMLClassHandler.removeRelationship(srcClass, destClass);
    }

    public boolean doAddAttribute(String srcClass, String attribute) 
    {
        return UMLClassHandler.getClass(srcClass).addAttribute(attribute);
    }

    public boolean doRemoveAttribute(String srcClass, String attribute) 
    {
        return UMLClassHandler.getClass(srcClass).removeAttribute(attribute);
    }

    public boolean doRenameAttribute(String srcClass, String oldAttribute, String newAttribute) 
    {
        return UMLClassHandler.getClass(srcClass).renameAttribute(oldAttribute, newAttribute);
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
        UMLClassHandler.listClass(className);
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
        String result = view.promptForInput("Do you want to load a JSON file for storing your UML diagram? Y/N");
        if (result.equals("Y") || result.equals("y"))
        {
            while (true)
            {
                String filepath = view.promptForInput("Enter a valid filepath");
                if (filepath != null)
                {
                    data = doLoad(filepath);
                    return true; 
                }
                String again = view.promptForInput("Invalid filepath. Type \'T\' to try again, \'E\' to exit, or any other key to make a new JSON file instead");
                if (again.equals("E") && !again.equals("e"))
                    return false;
                else if (!again.equals("T") && !again.equals("t"))
                    break;
            }
        }
        data = new UMLClassHandler();
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
            switch(action) {
                case ADD_CLASS:
                    if (command.arguments.length == 1)
                        if (doAddClass(command.arguments[0]))
                            view.notifySuccess();
                    break;
                case REMOVE_CLASS:
                    if (command.arguments.length == 1)
                        if (doRemoveClass(command.arguments[0]))
                            view.notifySuccess();
                    break;
                case RENAME_CLASS:
                    if (command.arguments.length == 2)
                        if (doRenameClass(command.arguments[0], command.arguments[1]))
                            view.notifySuccess();
                    break;
                case ADD_RELATIONSHIP:
                    if (command.arguments.length == 2)
                        doAddRelationship(command.arguments[0], command.arguments[1]);
                    break;
                case REMOVE_RELATIONSHIP:
                    if (command.arguments.length == 2)
                        doRemoveRelationship(command.arguments[0], command.arguments[1]);
                    break;
                case ADD_ATTRIBUTE:
                    if (command.arguments.length == 2)
                        if (doAddAttribute(command.arguments[0], command.arguments[1]))
                            view.notifySuccess();
                    break;
                case REMOVE_ATTRIVUTE:
                    if (command.arguments.length == 2)
                        if (doAddAttribute(command.arguments[0], command.arguments[1]))
                            view.notifySuccess();
                    break;
                case RENAME_ATTRIBUTE:
                    if (command.arguments.length == 3)
                        if (doRenameAttribute(command.arguments[0], command.arguments[1], command.arguments[2]))
                            view.notifySuccess();
                    break;
                case SAVE:
                    if (command.arguments.length == 1)
                    {
                        String result = view.promptForInput("Are you sure that you want to save? Y/N");
                        if (result.equals("Y") || result.equals("y"))
                            doSave(command.arguments[0]);
                    }
                    break;
                case LOAD:
                    if (command.arguments.length == 1)
                    {
                        String result = view.promptForInput("Are you sure that you want to load? Y/N");
                        if (result.equals("Y") || result.equals("y"))
                        data = doLoad(command.arguments[0]);
                    }
                    break;
                case LIST_CLASSES:
                    if (command.arguments.length == 0)
                        doListClasses();
                    break;
                case LIST_CLASS:
                    if (command.arguments.length == 1)
                        doListClass(command.arguments[0]);
                    break;
                case LIST_RELATIONSHIPS:
                    if (command.arguments.length == 0)
                        doListRelationships();
                    break;
                case HELP:
                    if (command.arguments.length == 0)
                        doHelp();
                    break;
                case EXIT:
                    break;
            }
        } while (!action.equals(Action.EXIT));
    }
 }
 