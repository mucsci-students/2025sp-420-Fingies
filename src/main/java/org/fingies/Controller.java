package org.fingies;

import java.util.List;
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
            return UMLClassHandler.createClass(className);
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
            RelationshipHandler.removeAllRelationshipsForClassname(className);
            return UMLClassHandler.removeClass(className);
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
            return UMLClassHandler.renameClass(className, newName);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doAddRelationship(String srcClass, String destClass, String type) 
    {
        try
        {
            RelationshipType rType = RelationshipType.fromString(type);
            return RelationshipHandler.addRelationship(srcClass, destClass, rType);
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
            return RelationshipHandler.removeRelationship(srcClass, destClass);
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
            return RelationshipHandler.changeRelationshipType(srcClass, destClass, rType);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doAddField(String srcClass, String type, String field) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).addField(field, type);
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
            return UMLClassHandler.getClass(srcClass).addMethod(methodName, returnType, parameterNames, parameterTypes);
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
            return UMLClassHandler.getClass(srcClass).removeField(field);
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
            UMLClassHandler.getClass(srcClass).getField(field).setType(newType);
            return true;
        }
        catch (Exception e) 
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doChangeMethodReturnType(String srcClass, String method, List<String> types, String newType)
    {
        try 
        {
            UMLClassHandler.getClass(srcClass).getMethod(method, types).setReturnType(newType);
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
            return UMLClassHandler.getClass(srcClass).removeMethod(methodName, parameterTypes);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRenameField(String srcClass, String oldField, String type, String newField) 
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).renameField(oldField, type, newField);
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
            return UMLClassHandler.getClass(srcClass).renameMethod(oldMethodName, parameterTypes, newMethodName);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }
    
    // class method type1 type2 ; newName1 newType1
    public boolean doAddParameters(String srcClass, String methodName, List<String> parameterTypes, List<String> newParameterNames, List<String> newParameterTypes)
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).getMethod(methodName, parameterTypes).addParameters(newParameterNames, newParameterTypes);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doRemoveParameters(String srcClass, String method, List<String> parameterTypes, List<String> parameterNamesToRemove)
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).getMethod(method, parameterTypes).removeParameters(parameterNamesToRemove);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }
    
    public boolean doRenameParameter(String srcClass, String method, List<String> parameterTypes, String oldParam, String newParam)
    {
        try
        {
            return UMLClassHandler.getClass(srcClass).getMethod(method, parameterTypes).renameParameter(oldParam, newParam);
        }
        catch (Exception e)
        {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }

    public boolean doChangeParameterDataType(String srcClass, String methodName, List<String> parameterTypes, String param, String newType) {
        try {
            UMLClassHandler.getClass(srcClass).getMethod(methodName, parameterTypes).getParameter(param).setType(newType);
            return true;
        }
        catch (Exception e) {
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

    // public UMLClassHandler doLoad(String filepath) 
    // {
    //     return model.loadData(filepath);
    // }

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

    /**
     * Executes the action with the commands arguments as inputs
     *
     * @param action action the user wishes to take
     * @param args command with arguments
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
                        view.notifySuccess("Successfully removed method " + args[1] + " with return type " + args[2] + " from class " + args[0]);
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
                if (args.length >= 5)
                {
                	List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 1);
                    if (doRenameMethod(args[0], args[1], paramTypes, args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully renamed method " + args[1] + " with return type " + args[2] + " to " + args[4] + " in class " + args[0]);
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
                if (args.length == 4)
                {
                    if (doRenameField(args[0], args[1], args[2], args[3]))
                    {
                        view.notifySuccess("Successfully renamed field " + args[1] + " to " + args[3] + " in class " + args[0]);
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
                	view.notifyFail("Rename field should have exactly 4 arguments.");
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
                if (args.length >= 6) {
                    try {
                        int idx = indexOfSymbol(args, ";");
                        if (idx == -1)
                        {
                        	view.notifyFail("Add Parameters should have a semicolon as an argument in between the method's signature and the new parameters\n"
                        			+ "ex. addp class_name method_name param_type1 param_type2 ; new_param_type new_param_name");
                        	return false;
                        }
                        List<String> oldParamTypes = getPartialListFromArray(args, 2, idx);
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
        	types.add(array[i]);
        	names.add(array[i + 1]);
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
 