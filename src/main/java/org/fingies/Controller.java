package org.fingies;

import java.util.List;
import java.util.Stack;

import java.util.ArrayList;
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
    
    private Stack<Change> undoStack = new Stack<Change>();
    private Stack<Change> redoStack = new Stack<Change>();

    public Controller (View view, JModel model)
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
        	Change change = new Change (null, null);
        	boolean result = UMLClassHandler.createClass(className);
        	change.setCurrClass(UMLClassHandler.getClass(className));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(className));
        	undoStack.push(change);
        	redoStack.clear();
            return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRemoveClass(String className) 
    {
        try
        {
        	Change change = new Change (UMLClassHandler.getClass(className), RelationshipHandler.getAllRelationshipsForClassname(className));
            RelationshipHandler.removeAllRelationshipsForClassname(className);
            boolean result = UMLClassHandler.removeClass(className);
            change.setCurrClass(null);
            change.setCurrRelationships(null);
            undoStack.push(change);
            redoStack.clear();
            return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRenameClass(String className, String newName) 
    {
        try
        {
        	Change change = new Change (UMLClassHandler.getClass(className), RelationshipHandler.getAllRelationshipsForClassname(className));
        	boolean result = UMLClassHandler.renameClass(className, newName);
        	change.setCurrClass(UMLClassHandler.getClass(newName));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(newName));
        	undoStack.push(change);
        	redoStack.clear();
            return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doAddRelationship(String srcClass, String destClass, String type) // TODO: implement rest of methods with undo stack
    {
        try
        {
        	// We store a change to src class, but not dest class. Because only one is needed
        	RelationshipType rType = RelationshipType.fromString(type);
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = RelationshipHandler.addRelationship(srcClass, destClass, rType);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRemoveRelationship(String srcClass, String destClass) 
    {
        try
        {
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = RelationshipHandler.removeRelationship(srcClass, destClass);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doChangeRelationshipType(String srcClass, String destClass, String newType)
    {
        try
        {
            RelationshipType rType = RelationshipType.fromString(newType);
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = RelationshipHandler.changeRelationshipType(srcClass, destClass, rType);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doAddField(String srcClass, String field, String type) 
    {
        try
        {
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = UMLClassHandler.getClass(srcClass).addField(field, type);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result; 
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doAddMethod(String srcClass, String methodName, String returnType, List<String> parameterNames, List<String> parameterTypes) 
    {
        try
        {
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = UMLClassHandler.getClass(srcClass).addMethod(methodName, returnType, parameterNames, parameterTypes);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result; 
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRemoveField(String srcClass, String field) 
    {
        try
        {
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = UMLClassHandler.getClass(srcClass).removeField(field);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result; 
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doChangeFieldDataType(String srcClass, String field, String newType)
    {
        try 
        {
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	UMLClassHandler.getClass(srcClass).getField(field).setType(newType);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return true;
        }
        catch (Exception e) 
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doChangeMethodReturnType(String srcClass, String methodName, List<String> parameterTypes, String newType)
    {
        try 
        {
            ArrayList <String> empty = new ArrayList<>();
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
            	UMLClassHandler.getClass(srcClass).getMethod(methodName, empty).setReturnType(newType);
            }
            else
            {
            	UMLClassHandler.getClass(srcClass).getMethod(methodName, parameterTypes).setReturnType(newType); 
            }
            change.setCurrClass(UMLClassHandler.getClass(srcClass));
            change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
        	return true;
        }
        catch (Exception e) 
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRemoveMethod(String srcClass, String methodName, List<String> parameterTypes) 
    {
        try
        {
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            ArrayList <String> empty = new ArrayList<>();
            boolean result;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                result = UMLClassHandler.getClass(srcClass).removeMethod(methodName, empty);
            }
            else
            {
                result = UMLClassHandler.getClass(srcClass).removeMethod(methodName, parameterTypes);
            }
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result; 
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRenameField(String srcClass, String oldField, String newField) 
    {
        try
        {
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = UMLClassHandler.getClass(srcClass).renameField(oldField, newField);
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
            return result; 
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRenameMethod(String srcClass, String oldMethodName, List<String> parameterTypes, String newMethodName) 
    {
        try
        {
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            ArrayList <String> empty = new ArrayList<>();
            boolean result;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                return UMLClassHandler.getClass(srcClass).renameMethod(oldMethodName, empty, newMethodName);
            } 
            else
            {
                result = UMLClassHandler.getClass(srcClass).renameMethod(oldMethodName, parameterTypes, newMethodName);
            }
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
        	return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }
    
    // expected format: class method type1 type2 ; newName1 newType1
    public boolean doAddParameters(String srcClass, String methodName, List<String> parameterTypes, List<String> newParameterNames, List<String> newParameterTypes)
    {
        try
        {
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            boolean result;
            ArrayList <String> empty = new ArrayList<>();
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                result = UMLClassHandler.getClass(srcClass).getMethod(methodName, empty).addParameters(newParameterNames, newParameterTypes);
            }   
            else
            {
                result = UMLClassHandler.getClass(srcClass).getMethod(methodName, parameterTypes).addParameters(newParameterNames, newParameterTypes);
            }
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
        	return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRemoveParameters(String srcClass, String methodName, List<String> parameterTypes, List<String> parameterNamesToRemove)
    {
        try
        {
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            boolean result;
            ArrayList <String> empty = new ArrayList<>();
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                result = UMLClassHandler.getClass(srcClass).getMethod(methodName, empty).removeParameters(parameterNamesToRemove);
            } 
            else
            {
                result = UMLClassHandler.getClass(srcClass).getMethod(methodName, parameterTypes).removeParameters(parameterNamesToRemove);
            }  
            change.setCurrClass(UMLClassHandler.getClass(srcClass));
            change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
        	return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }
    
    public boolean doRenameParameter(String srcClass, String methodName, List<String> parameterTypes, String oldParam, String newParam)
    {
        try
        {
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            boolean result;
            ArrayList <String> empty = new ArrayList<>();
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                result = UMLClassHandler.getClass(srcClass).getMethod(methodName, empty).renameParameter(oldParam, newParam);
            }   
            else
            {
                result = UMLClassHandler.getClass(srcClass).getMethod(methodName, parameterTypes).renameParameter(oldParam, newParam);
            }
            change.setCurrClass(UMLClassHandler.getClass(srcClass));
            change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
        	return result;
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doChangeParameterDataType(String srcClass, String methodName, List<String> parameterTypes, String param, String newType) {
        try 
        {
        	Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            ArrayList <String> empty = new ArrayList<>();
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                UMLClassHandler.getClass(srcClass).getMethod(methodName, empty).getParameter(param).setType(newType);
            }   
            else
            {
                UMLClassHandler.getClass(srcClass).getMethod(methodName, parameterTypes).getParameter(param).setType(newType);
            }
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
        	return true;
        }
        catch (Exception e) {
        	System.out.println(e.getMessage());
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

    public JModel.Model doLoad(String filepath) 
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
            view.notifyFail(e.getMessage());
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
            view.notifyFail(e.getMessage());
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
            String input = view.promptForSaveInput("Enter a valid filepath to save to or type EXIT to quit the program.");
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
    	if (filepath == null)
    		return false;
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
        return true;
        
    }
    
    public boolean doUndo()
    {
    	if (undoStack.isEmpty())
    	{
    		// nothing to undo, don't bother giving an error message
    		return false;
    	}
    	else
    	{
    		Change change = undoStack.pop();
    		redoStack.push(change);
    		UMLClassHandler.replace(change.getCurrClass(), change.getOldClass());
    		RelationshipHandler.replace(change.getCurrClass(), change.getOldClass());
    		if (change.getOldClass() != null)
    			RelationshipHandler.replaceAllRelationshipsForClassname(change.getOldClass().getName(), change.getOldRelationships());
    		return true;
    	}
    }
    
    public boolean doRedo()
    {
    	if (redoStack.isEmpty())
    	{
    		// nothing to redo, don't bother giving an error message
    		return false;
    	}
    	else
    	{
    		Change change = redoStack.pop();
    		undoStack.push(change);
    		UMLClassHandler.replace(change.getOldClass(), change.getCurrClass());
    		RelationshipHandler.replace(change.getOldClass(), change.getCurrClass());
    		if (change.getCurrClass() != null)
    			RelationshipHandler.replaceAllRelationshipsForClassname(change.getCurrClass().getName(), change.getCurrRelationships());
    		return true;
    	}
    }
    
    public boolean doMove(String className, String newX, String newY)
    {
    	try
    	{
    		UMLClass umlClass = UMLClassHandler.getClass(className);
        	Change change = new Change(umlClass, RelationshipHandler.getAllRelationshipsForClassname(className));
        	umlClass.setPosition(Integer.valueOf(newX), Integer.valueOf(newY));
        	change.setCurrClass(umlClass);
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(className));
        	undoStack.push(change);
        	redoStack.clear();
        	return true;
    	}
    	catch (Exception e)
    	{
    		model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
    	}
    }

    /**
     * Executes the action with the commands arguments as inputs
     *
     * @param action action the user wishes to take
     * @param args command with arguments
     */
    public boolean runHelper(Action action, String[] args)
    {
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
                        //view.notifyFail("Failed to add class " + args[0]);
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
                        //view.notifyFail("Failed to remove class " + args[0]);
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
                        //view.notifyFail("Failed to rename class " + args[0] + " to " + args[1]);
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
                        view.notifySuccess("Successfully added relationship " + args[0] + " " + RelationshipType.fromString(args[2]) + " " + args[1]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        //view.notifyFail("Failed to add relationship " + args[0] + " --> " + args[1] + " of type " + args[2]);
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
                        view.notifySuccess("Successfully removed relationship between " + args[0] + " and " + args[1]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        //view.notifyFail("Failed to remove relationship " + args[0] + " --> " + args[1]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Remove relationship should have exactly 2 arguments.");
                    return false;
                }
            case ADD_METHOD:
                if (args.length >= 3)
                {
                    try {
                    	List<String> parameterNames = new ArrayList<String>();
                    	List<String> parameterTypes = new ArrayList<String>();
                    	boolean result = getTwoListsFromArray(args, 3, args.length, parameterNames, parameterTypes);
                    	if (!result)
                    	{
                    		view.notifyFail("Add Method should have exactly 1 type for every parameter name.");
                    		return false;
                    	}
                        if (doAddMethod(args[0], args[1], args[2], parameterNames, parameterTypes))
                        {
                            view.notifySuccess("Successfully added method " + args[1] + " with argument(s) " + getPartialListFromArray(args, 3, args.length) + " to class " + args[0]);
                            madeChange = true;
                            return true;
                        }
                        else
                        {
                            //view.notifyFail("Method couldn't be added.");
                            return false;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        //view.notifyFail("Each parameter name must have a type")
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Add Method should have 3 or more arguments.");
                    return false;
                }
            case REMOVE_METHOD:
                if (args.length >= 2)
                {
                    List<String> paramTypes = getPartialListFromArray(args, 2, args.length);
                    if (doRemoveMethod(args[0], args[1], paramTypes))
                    {
                        view.notifySuccess("Successfully removed method " + args[1] + " from class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        //view.notifyFail("Failed to remove method " + args[1] + " from class " + args[0]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Remove method should have 2 or more arguments.");
                    return false;
                }
            case RENAME_METHOD:
                if (args.length >= 4)
                {
                	List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 1);
                    if (doRenameMethod(args[0], args[1], paramTypes, args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully renamed method " + args[1] + " to " + args[args.length - 1] + " in class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        //view.notifyFail("Failed to rename method " + args[1] + " to " + args[3] + " in class " + args[0]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Rename method should have exactly 5 arguments.");
                    return false;
                }
            case ADD_FIELD:
                if (args.length == 3)
                {
                    if (doAddField(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully added field " + args[2] + " with type " + args[1] + " to class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        //view.notifyFail("Field couldn't be added.");
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Add field should have exactly 3 arguments.");
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
                        //view.notifyFail("Failed to remove field " + args[1] + " from class " + args[0]);
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
                        //view.notifyFail("Failed to rename field " + args[1] + " to " + args[2] + " in class " + args[0]);
                        return false;
                    }
                }
                else
                {
                	view.notifyFail("Rename field should have exactly 3 arguments.");
                    return false;
                }
            case CHANGE_FIELD_TYPE: 
                if (args.length == 3)
                {
                    if (doChangeFieldDataType(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully changed field " + args[1] + "'s data type to " + args[2] + " in class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else {
                    view.notifyFail("Changing field data type should have exactly 3 arguments.");
                    return false;
                }
            case ADD_PARAMETERS:
                if (args.length >= 5) {
                    try {
                        int idx = indexOfSymbol(args, ";");
                        if (idx == -1)
                        {
                        	view.notifyFail("Add Parameters should have a semicolon as an argument in between the method's signature and the new parameters\n"
                        			+ "ex. addp class_name method_name param_type1 param_type2 ; new_param_type new_param_name");
                        	return false;
                        }
                        List<String> oldParamTypes = getPartialListFromArray(args, 2, idx);
                        if (idx == 3) {
                            oldParamTypes = new ArrayList<String>();
                        }
                        List<String> newParamNames = new ArrayList<String>();
                        List<String> newParamTypes = new ArrayList<String>();
                        getTwoListsFromArray(args, idx + 1, args.length, newParamNames, newParamTypes);
                        if (doAddParameters(args[0], args[1], oldParamTypes, newParamNames, newParamTypes))
                        {
                            view.notifySuccess("Succesfully added parameter(s): " + newParamNames + " to method " + args[1] + " from class " + args[0]);
                            madeChange = true;
                            return true;
                        }
                        else
                        {
                            //view.notifyFail("Failed to add parameter(s): " + params + " to method " + args[1] + " from class " + args[0]);
                            return false;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Add Parameters should have 5 or more parameters.");
                    return false;
                }
            case REMOVE_PARAMETERS:
                if (args.length >= 5) {
                    int idx = indexOfSymbol(args, ";");
                    if (idx == -1)
                    {
                    	view.notifyFail("Remove Parameters should have a semicolon as an argument in between the method's signature and the parameters to remove\n"
                    			+ "ex. rmp class_name method_name param_type1 param_type2 ; param_type param_name");
                    	return false;
                    }
                    List<String> paramTypes = getPartialListFromArray(args, 2, idx);
                    List<String> paramNames = getPartialListFromArray(args, idx + 1, args.length);
                    if (doRemoveParameters(args[0], args[1], paramTypes, paramNames))
                    {
                        view.notifySuccess("Succesfully removed parameter(s): " + paramNames + " from method " + args[1] + " from class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        //view.notifyFail("Failed to remove parameter(s): " + params + " from method " + args[1] + " from class " + args[0]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Remove Parameters should have 5 or more parameters.");
                    return false;
                }
            case RENAME_PARAMETER:
                if (args.length >= 5) {
                	List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 2); 
                    if (doRenameParameter(args[0], args[1], paramTypes, args[args.length - 2], args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully renamed parameter " + args[args.length - 2] + " of method " + args[1] + " of class " + args[0] + " to " + args[args.length - 1]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        //view.notifyFail("Failed to rename parameter " + args[3] + " of method " + args[1] + " of class " + args[0] + " to " + args[4]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Parameters should have 5 or more arguments.");
                    return false;
                }
            case CHANGE_PARAMETER_TYPE:
                if (args.length >= 5) 
                {
                	List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 2); 
                    if (doChangeParameterDataType(args[0], args[1], paramTypes, args[args.length - 2], args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully changed parameter " + args[args.length - 2] + "'s data type to " + args[args.length - 1] + " of method " + args[1] + " of class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else 
                    {
                        return false;
                    }
                }
                else 
                {
                    view.notifyFail("Changing Parameter type should have exactly 6 arguments.");
                    return false;
                }
            case CHANGE_METHOD_RETURN_TYPE:
             
                if (args.length >= 3)
                {
                    List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 1);
                    if (doChangeMethodReturnType(args[0], args[1], paramTypes, args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully changed method " + args[1] + "'s return type to " + args[args.length - 1] + " in class " + args[0]);
                        madeChange = true;
                        return true;
                    }  
                    else
                    {
                        //view.notifyFail("Failed to change relationship type of " + args[0] + " --> " + args[1] + " to " + args[2]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Change method type should have at least 3 arguments.");
                    return false;
                }
            case CHANGE_RELATIONSHIP_TYPE:
                if (args.length == 3)
                {
                    if (doChangeRelationshipType(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully changed relationship type to " + args[0] + " " + args[2] + " " + args[1]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        //view.notifyFail("Failed to change relationship type of " + args[0] + " --> " + args[1] + " to " + args[2]);
                        return false;
                    }
                }
                else
                {
                    view.notifyFail("Change Relationship Type should have exactly 3 arguments.");
                    return false;
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
                        if (args[0] == null)
                        	return false;
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
                if (args.length == 0)
                {
                	String path = view.promptForOpenInput("Please designate a filepath to open");
                	args = new String[] {path}; // make it so the if() below will run
                }
                
                if (args.length == 1)
                {
                    if (madeChange)
                    {
                        String result = view.promptForInput("Are you sure that you want to load without saving? Type Y for yes or any other key to save before loading");
                        if (result.toLowerCase().equals("y"))
                        {
                             return loadCheck(args[0]);
                        }
                        else
                        {
                            saveLoop();
                            return loadCheck(args[0]);
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
                	view.notifyFail("Load should have either 0 or 1 arguments.");
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
                    return false;
                }
            case EXIT:
                if (args.length != 0) {
                    //view.notifyFail("Failed to exit program.");
                    return false;
                }
                else if (madeChange)
                {
                    String result = view.promptForInput("Are you sure that you want to exit without saving? Type Y for yes or any other key to save before exiting");
                    if (result == null)
                    	return false;
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
            case UNDO:
            	if (args.length != 0)
            	{
            		view.notifyFail("Undo shouldn't have any arguments.");
            		return false;
            	}
            	else
            	{
            		return doUndo();
            	}
            case REDO:
            	if (args.length != 0)
            	{
            		view.notifyFail("Redo shouldn't have any arguments.");
            		return false;
            	}
            	else
            	{
            		return doRedo();
            	}
            case MOVE:
            	if (args.length != 3)
            	{
            		view.notifyFail("Changing field data type should have exactly 3 arguments.");
            		return false;
            	}
            	else
            	{
            		return doMove(args[0], args[1], args[2]);
            	}
        }
        return false;
    }

    public List<String> getPartialListFromArray(String[] array, int start, int end)
    {
        return Arrays.asList(Arrays.copyOfRange(array, start, end));
    }

    // ex. e1 e1 e2 e2 e3 e3
    public boolean getTwoListsFromArray(String[] array, int start, int end, List<String> names, List<String> types) {
        // System.out.println("end: " + end + "   " + "start: " + start);
    	if ((end - start) % 2 == 1)
            return false;
        for (int i = start; i < end; i += 2)
        {
        	names.add(array[i]);
        	types.add(array[i + 1]);
        }
    	return true;
    }
    
    public int indexOfSymbol (String[] array, String symbol)
    {
    	for (int i = 0; i < array.length; ++i)
    		if (array[i].equals(symbol))
    			return i;
    	return -1;
    }
 }
 