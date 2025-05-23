package org.fingies.Controller;

import java.util.List;
import java.util.Stack;

import org.fingies.Change;
import org.fingies.Model.JModel;
import org.fingies.Model.Method;
import org.fingies.Model.Parameter;
import org.fingies.Model.RelationshipHandler;
import org.fingies.Model.RelationshipType;
import org.fingies.Model.UMLClass;
import org.fingies.Model.UMLClassHandler;
import org.fingies.Model.Field;
import org.fingies.View.UMLView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Controller for UML Editor, handles user input
 * @author kdichter and friends
 */
public class UMLController {
    private UMLView view;
    private JModel model;
    private boolean madeChange;
    private boolean hasSaved;
    private Change topOfUndoStackLastSave;
    
    private Stack<Change> undoStack = new Stack<Change>();
    private Stack<Change> redoStack = new Stack<Change>();

    public UMLController (UMLView view, JModel model)
    {
        this.view = view;
        this.model = model;
        madeChange = false;
        hasSaved = false;
        topOfUndoStackLastSave = null;
    }
    
    
    //////////////////////////////////////////// MODEL-RELATED DO METHODS ////////////////////////////////////////////
    
    /*
     * Each of these methods try to perform their titular action on the model, returning true if successful, and false otheriwse.
     * They notify the view if the action failed.
     * 
     * They also push a Change memento onto the Undo stack for later use.
     */

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
            UMLClassHandler.removeClass(className);
            change.setCurrClass(null);
            change.setCurrRelationships(null);
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

    public boolean doAddRelationship(String srcClass, String destClass, String type)
    {
        try
        {
        	RelationshipType rType = RelationshipType.fromString(type);
        	if (rType == null)
        		throw new IllegalArgumentException(type + " is not a valid relationship type"); // exception is immediately caught
        	// We store a change to src class, but not dest class. Because only one is needed
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
        	RelationshipHandler.removeRelationship(srcClass, destClass);
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
        	UMLClass umlSrcClass = UMLClassHandler.getClass(srcClass);
        	Change change = new Change (umlSrcClass, RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = umlSrcClass.addField(field, type);
        	change.setCurrClass(umlSrcClass);
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
        	UMLClass srcUMLClass = UMLClassHandler.getClass(srcClass);
        	Change change = new Change (srcUMLClass, RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = srcUMLClass.addMethod(methodName, returnType, parameterNames, parameterTypes);
        	change.setCurrClass(srcUMLClass);
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
        	UMLClass umlSrcClass = UMLClassHandler.getClass(srcClass);
        	Change change = new Change (umlSrcClass, RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = umlSrcClass.removeField(field);
        	change.setCurrClass(umlSrcClass);
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
        	UMLClass umlSrcClass = UMLClassHandler.getClass(srcClass);
        	Change change = new Change (umlSrcClass, RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	Field umlField = umlSrcClass.getField(field);
        	if (umlField == null)
        		throw new IllegalArgumentException("Class " + srcClass + " doesn't have a field named " + field);
        	umlField.setType(newType);
        	change.setCurrClass(umlSrcClass);
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
        	
            UMLClass srcUMLClass = UMLClassHandler.getClass(srcClass);
            Method method;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
            	method = srcUMLClass.getMethod(methodName, empty);
            } 
            else
            {
            	method = srcUMLClass.getMethod(methodName, parameterTypes);
            }
            
            if (method == null) // immediately enter the catch{} if the method doesn't exist
            {
            	throw new IllegalArgumentException(srcClass + " doesn't have a method named " + methodName + " with the parameter types " + parameterTypes);
            }
            
            method.setReturnType(newType);
            
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
        	UMLClass umlSrcClass = UMLClassHandler.getClass(srcClass);
            Change change = new Change (umlSrcClass, RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            ArrayList <String> empty = new ArrayList<>();
            boolean result;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                result = umlSrcClass.removeMethod(methodName, empty);
            }
            else
            {
                result = umlSrcClass.removeMethod(methodName, parameterTypes);
            }
        	change.setCurrClass(umlSrcClass);
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
        	UMLClass umlSrcClass = UMLClassHandler.getClass(srcClass);
        	Change change = new Change (umlSrcClass, RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	boolean result = umlSrcClass.renameField(oldField, newField);
        	change.setCurrClass(umlSrcClass);
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
        	UMLClass umlSrcClass = UMLClassHandler.getClass(srcClass);
            Change change = new Change (umlSrcClass, RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            ArrayList <String> empty = new ArrayList<>();
            boolean result;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
                return umlSrcClass.renameMethod(oldMethodName, empty, newMethodName);
            } 
            else
            {
                result = umlSrcClass.renameMethod(oldMethodName, parameterTypes, newMethodName);
            }
        	change.setCurrClass(umlSrcClass);
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
            
            UMLClass srcUMLClass = UMLClassHandler.getClass(srcClass);
            Method method;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
            	method = srcUMLClass.getMethod(methodName, empty);
            } 
            else
            {
            	method = srcUMLClass.getMethod(methodName, parameterTypes);
            }
            
            if (method == null) // immediately enter the catch{} if the method doesn't exist
            {
            	throw new IllegalArgumentException(srcClass + " doesn't have a method named " + methodName + " with the parameter types " + parameterTypes);
            }
            
            result = method.addParameters(newParameterNames, newParameterTypes);
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
            
            UMLClass srcUMLClass = UMLClassHandler.getClass(srcClass);
            Method method;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
            	method = srcUMLClass.getMethod(methodName, empty);
            } 
            else
            {
            	method = srcUMLClass.getMethod(methodName, parameterTypes);
            }
            
            if (method == null) // immediately enter the catch{} if the method doesn't exist
            {
            	throw new IllegalArgumentException(srcClass + " doesn't have a method named " + methodName + " with the parameter types " + parameterTypes);
            }
            
            result = method.removeParameters(parameterNamesToRemove);
    		
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
    
    // overloaded variant for removing all parameters
    public boolean doRemoveParameters(String srcClass, String methodName, List<String> parameterTypes)
    {
        try
        {
            Change change = new Change (UMLClassHandler.getClass(srcClass), RelationshipHandler.getAllRelationshipsForClassname(srcClass));
            boolean result;
            ArrayList <String> empty = new ArrayList<>();
            
            UMLClass srcUMLClass = UMLClassHandler.getClass(srcClass);
            Method method;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
            	method = srcUMLClass.getMethod(methodName, empty);
            } 
            else
            {
            	method = srcUMLClass.getMethod(methodName, parameterTypes);
            }
            
            if (method == null) // immediately enter the catch{} if the method doesn't exist
            {
            	throw new IllegalArgumentException(srcClass + " doesn't have a method named " + methodName + " with the parameter types " + parameterTypes);
            }
            
            result = method.clearParameters();
    		
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
            
            UMLClass srcUMLClass = UMLClassHandler.getClass(srcClass);
            Method method;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
            	method = srcUMLClass.getMethod(methodName, empty);
            } 
            else
            {
            	method = srcUMLClass.getMethod(methodName, parameterTypes);
            }
            
            if (method == null) // immediately enter the catch{} if the method doesn't exist
            {
            	throw new IllegalArgumentException(srcClass + " doesn't have a method named " + methodName + " with the parameter types " + parameterTypes);
            }
            
            result = method.renameParameter(oldParam, newParam);
            
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
            
            UMLClass srcUMLClass = UMLClassHandler.getClass(srcClass);
            Method method;
            if (!parameterTypes.isEmpty() && parameterTypes.get(0).equals("")) // without this, parameterTypes ends up with 1 item of an empty String
            {
            	method = srcUMLClass.getMethod(methodName, empty);
            } 
            else
            {
            	method = srcUMLClass.getMethod(methodName, parameterTypes);
            }
            
            if (method == null) // immediately enter the catch{} if the method doesn't exist
            {
            	throw new IllegalArgumentException(srcClass + " doesn't have a method named " + methodName + " with the parameter types " + parameterTypes);
            }
            
            Parameter parameter = method.getParameter(param);
            
            if (parameter == null)
            {
            	throw new IllegalArgumentException("The method " + methodName + " with the parameter types " + parameterTypes + " does not have a parameter named " + param);
            }
            
            parameter.setType(newType);
            
        	change.setCurrClass(UMLClassHandler.getClass(srcClass));
        	change.setCurrRelationships(RelationshipHandler.getAllRelationshipsForClassname(srcClass));
        	undoStack.push(change);
        	redoStack.clear();
        	return true;
        }
        catch (Exception e) {
            model.writeToLog(e.getMessage());
            view.notifyFail(e.getMessage());
            return false;
        }
    }
    
    public boolean doMove(String className, String newX, String newY)
    {
    	try
    	{
    		UMLClass umlClass = UMLClassHandler.getClass(className);
        	Change change = new Change(umlClass, RelationshipHandler.getAllRelationshipsForClassname(className));
        	umlClass.setPosition(Integer.parseInt(newX), Integer.parseInt(newY));
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
     * Pops the top change off the undo stack and reverts it in the model.
     * Then, pushes that change to the redo stack.
     * 
     * @return true If the undo was successful.
     */
    public boolean doUndo()
    {
    	if (undoStack.isEmpty())
    	{
    		return false;
    	}
    	else
    	{
    		Change change = undoStack.pop();
    		redoStack.push(change);
    		UMLClass newClass = change.getOldClass(); // its important that UMLClassHandler & RelationshipHandler recieve links to the same UMLClass object
    		UMLClassHandler.replace(change.getCurrClass(), newClass);
    		RelationshipHandler.replace(change.getCurrClass(), newClass);
    		if (newClass != null)
    			RelationshipHandler.replaceAllRelationshipsForClassname(newClass.getName(), change.getOldRelationshipsUsingLink(newClass));
    		return true;
    	}
    }
    
    /**
     * Pops the top change off the redo stack and re-applies it in the model.
     * Then, pushes that change to the undo stack.
     * 
     * @return true If the redo was successful.
     */
    public boolean doRedo()
    {
    	if (redoStack.isEmpty())
    	{
    		return false;
    	}
    	else
    	{
    		madeChange = true;
    		Change change = redoStack.pop();
    		undoStack.push(change);
    		UMLClass newClass = change.getCurrClass();
    		UMLClassHandler.replace(change.getOldClass(), newClass);
    		RelationshipHandler.replace(change.getOldClass(), newClass);
    		if (newClass != null)
    			RelationshipHandler.replaceAllRelationshipsForClassname(newClass.getName(), change.getCurrRelationshipsUsingLink(newClass));
    		return true;
    	}
    }
    
    
    //////////////////////////////////////////// OTHER DO METHODS ////////////////////////////////////////////
    
    /*
     * These methods perform other actions, such as saving, loading, and listing classes.
     * They notify the view of a fail, if appropriate.
     */

    public boolean doSave() 
    {
        return model.saveData();
    }

    public boolean doSave(String filepath)
    {
        return model.saveData(filepath);
    }

    public JModel.UMLModel doLoad(String filepath) 
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
    
    public boolean doExportImage(String filepath)
    {
    	return model.exportImage(filepath, view.getJComponentRepresentation());
    }
    
    
    //////////////////////////////////////////// OTHER METHODS ////////////////////////////////////////////

    /**
     * Calls doSave() and prints the appropriate message
     * 
     * @param filepath The file to save to.
     * @return True if the file was successfully saved, false otherwise.
     */
    public boolean saveCheck(String filepath)
    {
        if (doSave(filepath))
        {
            view.notifySuccess("Successfully loaded " + filepath);
            return true;
        }
        else
        {
            view.notifyFail("Invalid filepath provided");
            return false;
        }
    }

    /**
     * Calls doLoad() and prints the appropriate message
     * 
     * @param filepath The file to load.
     * @return True if the file was successfully loaded, false otherwise.
     */
    public boolean loadCheck(String filepath)
    {
    	if (filepath == null)
    		return false;
        if (doLoad(filepath) != null)
        {
            view.notifySuccess("Successfully loaded " + filepath);
            return true;
        }
        else
        {
            view.notifyFail("Invalid filepath provided");
            return false;
        }
    }
    
    //////////////////////////////////////////// RUN HELPER ////////////////////////////////////////////

    // runHelper() calls the above methods ^^^ based on which action is supplied
    
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
                        return false;
                    }
                        
                }
                else
                {
                	int idx = Action.ADD_CLASS.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 1 argument \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                        return false;
                    }
                        
                }
                else
                {
                	int idx = Action.REMOVE_CLASS.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 1 argument \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                        return false;
                    }
                }
                else
                {
                	int idx = Action.RENAME_CLASS.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 2 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case ADD_RELATIONSHIP:
                if (args.length == 3)
                {

                    if (doAddRelationship(args[0], args[1], args[2]))
                    {
                    	RelationshipType r = RelationshipType.fromString(args[2]);
                        view.notifySuccess("Successfully added relationship " + args[0] + " " + r + " " + args[1] + " (" + r.getName() + ")");
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
                	int idx = Action.ADD_RELATIONSHIP.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                        return false;
                    }
                }
                else
                {
                	int idx = Action.REMOVE_RELATIONSHIP.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 2 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                        	int idx = Action.ADD_PARAMETERS.ordinal();
                    		view.notifyFail("Every parameter name should have 1 type \n"
                    				+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    		return false;
                        }
                        if (doAddMethod(args[0], args[1], args[2], parameterNames, parameterTypes))
                        {
                            view.notifySuccess("Successfully added method " + UMLClassHandler.getClass(args[0]).getMethod(args[1], parameterTypes) + " to class " + args[0]);
                            madeChange = true;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        return false;
                    }
                }
                else
                {
                	int idx = Action.ADD_METHOD.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have at least 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case REMOVE_METHOD:
                if (args.length >= 2)
                {
                    List<String> paramTypes = getPartialListFromArray(args, 2, args.length);
                    if (doRemoveMethod(args[0], args[1], paramTypes))
                    {
                        view.notifySuccess("Successfully removed method " + undoStack.peek().getOldClass().getMethod(args[1], paramTypes) + " from class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else {
                        return false;
                    }
                }
                else
                {
                	int idx = Action.REMOVE_METHOD.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have at least 2 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case RENAME_METHOD:
                if (args.length >= 3)
                {
                	List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 1);
                    if (doRenameMethod(args[0], args[1], paramTypes, args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully renamed method " + undoStack.peek().getOldClass().getMethod(args[1], paramTypes) + " to " + args[args.length - 1] + " in class " + args[0]);
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
                	int idx = Action.RENAME_METHOD.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have at least 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case ADD_FIELD:
                if (args.length == 3)
                {
                    if (doAddField(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully added field " + args[1] + " with type " + args[2] + " to class " + args[0]);
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
                	int idx = Action.ADD_FIELD.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                        return false;
                    }
                }
                else
                {
                	int idx = Action.REMOVE_FIELD.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 2 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                        return false;
                    }
                }
                else
                {
                	int idx = Action.RENAME_FIELD.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case CHANGE_FIELD_TYPE: 
                if (args.length == 3)
                {
                    if (doChangeFieldDataType(args[0], args[1], args[2]))
                    {
                        view.notifySuccess("Successfully changed the data type of field " + args[1] + " to " + args[2] + " in class " + args[0]);
                        madeChange = true;
                        return true;
                    }
                    else
                    {
                        return false;
                    }
                }
                else {
                	int idx = Action.CHANGE_FIELD_TYPE.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case ADD_PARAMETERS:
                if (args.length >= 5) {
                    try {
                        int index = indexOfSymbol(args, ";");
                        if (index == -1)
                        {
                        	int idx = Action.ADD_PARAMETERS.ordinal();
                        	view.notifyFail(Command.COMMANDS[idx] + " should have a semicolon as an argument in between the method's signature and the new parameters \n"
                        			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                        	return false;
                        }
                        List<String> oldParamTypes = getPartialListFromArray(args, 2, index);
                        List<String> newParamNames = new ArrayList<String>();
                        List<String> newParamTypes = new ArrayList<String>();
                        boolean result = getTwoListsFromArray(args, index + 1, args.length, newParamNames, newParamTypes);
                        if (!result)
                        {
                        	int idx = Action.ADD_PARAMETERS.ordinal();
                    		view.notifyFail("Every parameter name should have 1 type \n"
                    				+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    		return false;
                        }
                        if (doAddParameters(args[0], args[1], oldParamTypes, newParamNames, newParamTypes))
                        {
                            view.notifySuccess("Succesfully added parameter" + (newParamNames.size() > 1 ? "s: " + newParamNames : " " + newParamNames.get(0)) + " to method " + undoStack.peek().getOldClass().getMethod(args[1], oldParamTypes) + " from class " + args[0]);
                            madeChange = true;
                            return true;
                        }
                        else
                        {
                            return false;
                        }
                    }
                    catch (IllegalArgumentException e) {
                        return false;
                    }
                }
                else
                {
                	int idx = Action.ADD_PARAMETERS.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have 5 or more arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case REMOVE_PARAMETERS:
                if (args.length >= 2) {
                    int index = indexOfSymbol(args, ";");
                    if (index == -1)
                    {
                    	List<String> paramTypes = getPartialListFromArray(args, 2, args.length);
                    	if (doRemoveParameters(args[0], args[1], paramTypes))
                        {
                            view.notifySuccess("Succesfully removed all parameters from method " + undoStack.peek().getOldClass().getMethod(args[1], paramTypes) + " from class " + args[0]);
                            madeChange = true;
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                    else
                    {
                    	List<String> paramTypes = getPartialListFromArray(args, 2, index);
                        List<String> paramNames = getPartialListFromArray(args, index + 1, args.length);
                        if (doRemoveParameters(args[0], args[1], paramTypes, paramNames))
                        {
                            view.notifySuccess("Succesfully removed parameter" + (paramNames.size() > 1 ? "s: " + paramNames : " " + paramNames.get(0)) + " to method " + undoStack.peek().getOldClass().getMethod(args[1], paramTypes) + " from class " + args[0]);
                            madeChange = true;
                            return true;
                        }
                        else {
                            return false;
                        }
                    }
                }
                else
                {
                	int idx = Action.REMOVE_PARAMETERS.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have at least 2 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case RENAME_PARAMETER:
                if (args.length >= 5) {
                	List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 2); 
                    if (doRenameParameter(args[0], args[1], paramTypes, args[args.length - 2], args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully renamed parameter " + args[args.length - 2] + " of method " + undoStack.peek().getOldClass().getMethod(args[1], paramTypes) + " to " + args[args.length - 1] + " in class " + args[0]);
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
                	int idx = Action.RENAME_PARAMETER.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have at least 5 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case CHANGE_PARAMETER_TYPE:
                if (args.length >= 5) 
                {
                	List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 2); 
                    if (doChangeParameterDataType(args[0], args[1], paramTypes, args[args.length - 2], args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully changed the data type of parameter " + args[args.length - 2] + " of method " + undoStack.peek().getOldClass().getMethod(args[1], paramTypes) + " to " + args[args.length - 1] + " in class " + args[0]);
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
                	int idx = Action.CHANGE_PARAMETER_TYPE.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have at least 5 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case CHANGE_METHOD_RETURN_TYPE:
             
                if (args.length >= 3)
                {
                    List<String> paramTypes = getPartialListFromArray(args, 2, args.length - 1);
                    if (doChangeMethodReturnType(args[0], args[1], paramTypes, args[args.length - 1]))
                    {
                        view.notifySuccess("Successfully changed the return type of method " + UMLClassHandler.getClass(args[0]).getMethod(args[1], paramTypes) + " to " + args[args.length - 1] + " in class " + args[0]);
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
                	int idx = Action.CHANGE_METHOD_RETURN_TYPE.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have at least 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case CHANGE_RELATIONSHIP_TYPE:
                if (args.length == 3)
                {
                    if (doChangeRelationshipType(args[0], args[1], args[2]))
                    {   
                        RelationshipType r = RelationshipType.fromString(args[2]);
                        view.notifySuccess("Successfully changed the type of relationship between " + args[0] + " and " + args[1] + "to " + r.getName());
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
                	int idx = Action.CHANGE_RELATIONSHIP_TYPE.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 3 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case SAVE:
                if (args.length == 0)
                {
                    if (hasSaved)
                    {
                        doSave();
                        madeChange = false;
                        if (undoStack.isEmpty())
                        {
                        	topOfUndoStackLastSave = null;
                        }
                        else
                        {
                        	topOfUndoStackLastSave = undoStack.peek();
                        }
                        view.notifySuccess("Successfully saved to " + model.getFilepath());
                        return true;
                    }
                    else
                    {
                    	String path = view.promptForSaveInput("Please designate a filepath to save to");
                    	args = new String[] { path };
                    	if (args[0] == null)
                    	{
                    		return false; // canceled saving
                    	}
                    }
                }
                
                if (args.length == 1)
                {
                	boolean result = saveCheck(args[0]);
                	madeChange = false;
                	if (undoStack.isEmpty())
                    {
                    	topOfUndoStackLastSave = null;
                    }
                    else
                    {
                    	topOfUndoStackLastSave = undoStack.peek();
                    }
                	hasSaved = true;
                	return result;
                }
                else
                {
                	int idx = Action.SAVE.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have either 0 or 1 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case LOAD:     
                if (args.length == 0)
                {
                	String path = view.promptForOpenInput("Please designate a filepath to open");
                	args = new String[] { path };
                	if (args[0] == null)
                	{
                        return false; // canceled loading
                	}
                }
                
                if (args.length == 1)
                {
                    if (madeChange)
                    {
                        int ans = view.promptForYesNoInput("Would you like to save before loading a file?", "Warning");
                        if (ans == 1)
                        {
                        	boolean result = loadCheck(args[0]);
                        	madeChange = false;
                        	topOfUndoStackLastSave = null;
                        	undoStack.clear();
                        	redoStack.clear();
                        	hasSaved = true;
                        	return result;
                        }
                        else if(ans == 0)
                        {
                            boolean saveResult = saveCheck(view.promptForSaveInput("Please designate a filepath to save to"));
                            if(saveResult)
                            {
                            	boolean result = loadCheck(args[0]);
                            	madeChange = false;
                            	topOfUndoStackLastSave = null;
                            	undoStack.clear();
                            	redoStack.clear();
                            	hasSaved = true;
                            	return result;
                            }
                            else
                            {
                            	return false;
                            }
                        }
                        else
                        {
                        	return false;
                        }
                    }
                    else
                    {
                        if (model.fileExist(args[0]))
                        {
                            doLoad(args[0]);
                        	madeChange = false;
                        	topOfUndoStackLastSave = null;
                        	undoStack.clear();
                        	redoStack.clear();
                        	hasSaved = true;
                            view.notifySuccess("Successfully loaded " + args[0]);
                            return true;
                        }
                        else
                        {
                            view.notifyFail("Invalid filepath");
                            return false;
                        }
                    }
                }
                else
                {
                	int idx = Action.LOAD.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have either 0 or 1 arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                	int idx = Action.LIST_CLASSES.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " shouldn't have any arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                	int idx = Action.LIST_CLASS.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have exactly 1 argument \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                	int idx = Action.LIST_RELATIONSHIPS.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " shouldn't have any arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
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
                	int idx = Action.HELP.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " should have either 0 or 1 arguments \n"
                			+ "Arguments with spaces require quotes \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
            case EXIT:
                if (args.length != 0) {
                	int idx = Action.EXIT.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " shouldn't have any arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
                }
                else if (madeChange)
                {
                    int result = view.promptForYesNoInput("Do you want to save before exiting?", "Warning");
                    if (result == 2)
                    	return false;
                    if (result == 0)
                    {
                        if (!hasSaved)
                        {
                            boolean saveResult = saveCheck(view.promptForSaveInput("Please designate a filepath to save to"));
                            return saveResult;
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
            		int idx = Action.UNDO.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " shouldn't have any arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
            	}
            	else
            	{
            		if (doUndo())
            		{
            			if (undoStack.empty())
                		{
                			if (topOfUndoStackLastSave == null)
                				madeChange = false;
                			else
                				madeChange = true;
                		}
                		else
                		{
                			if (topOfUndoStackLastSave == undoStack.peek()) // do the links match
                				madeChange = false;
                			else
                				madeChange = true;
                		}
            			return true;
            		}
            		else
            		{
            			return false;
            		}
            	}
            case REDO:
            	if (args.length != 0)
            	{
            		int idx = Action.UNDO.ordinal();
                	view.notifyFail(Command.COMMANDS[idx] + " shouldn't have any arguments \n"
                			+ "Usage: " + Command.COMMANDS[idx] + " " + Command.COMMAND_ARGS[idx]);
                    return false;
            	}
            	else
            	{
            		if (doRedo())
            		{
            			if (undoStack.empty())
                		{
                			if (topOfUndoStackLastSave == null)
                				madeChange = false;
                			else
                				madeChange = true;
                		}
                		else
                		{
                			if (topOfUndoStackLastSave == undoStack.peek()) // do the links match
                				madeChange = false;
                			else
                				madeChange = true;
                		}
            			return true;
            		}
            		else
            		{
            			return false;
            		}
            	}
            case EXPORT:
            	
            	if (args.length == 0)
            	{
            		String input = view.promptForSaveInput("Please designate a filepath to export to");
                	args = new String[] { input };
                	if (args[0] == null)
                		return false;
            	}
            	
            	if (args.length == 1)
            	{
                	if (!args[0].endsWith(".png"))
                		args[0] += ".png";
            		if (doExportImage(args[0]))
                    {
                        view.notifySuccess("Successfully exported the diagram");
                        return true;
                    }
                    else
                    {
                	    view.notifyFail("Invalid filepath provided");
                        return false;
                    }
            	}
            	else
            	{
            		int idx = Action.EXPORT.ordinal();
                	view.notifyFail("export image should have either 0 or 1 arguments \n"
                			+ "Usage: \n" + "Export Image " + Command.COMMAND_ARGS[idx]);
                    return false;
            	}
            case MOVE:
            	if (args.length != 3)
            	{
            		int idx = Action.MOVE.ordinal();
                	view.notifyFail("move should have exactly 3 arguments \n"
                			+ "Usage: \n" + "Move " + Command.COMMAND_ARGS[idx]);
                    return false;
            	}
            	else
            	{
            		if (doMove(args[0], args[1], args[2]))
            		{
            			madeChange = true;
            			return true;
            		}
            		else
            		{
            			return false;
            		}
            	}
        }
        return false;
    }

    /**
     * Returns a list containing a subarray, given an array and start & end points.
     * 
     * @param array The array to get a partial list from.
     * @param start The start of the subarray to convert into a list.
     * @param end The end of the subarray to convert into a list.
     * @return A list containing all of the elements in the array between start (inclusive) and end (exclusive)
     */
    public List<String> getPartialListFromArray(String[] array, int start, int end)
    {
        return Arrays.asList(Arrays.copyOfRange(array, start, end));
    }

    // ex. e1 e1 e2 e2 e3 e3
    /**
     * Converts a subarray into two lists, where the first list contains all of the elements with even indices in the subarray, and the
     * second list contains all of the odd elements.
     * 
     * @param array The array to get the elements from.
     * @param start The first index to get elements in the array from.
     * @param end The index of one element past the last element to get.
     * @param names The list to put all of the even indexed elements into.
     * @param types The list to put all of the odd indexed elements into.
     * @return true If the conversion was successful.
     */
    public boolean getTwoListsFromArray(String[] array, int start, int end, List<String> names, List<String> types) {
    	if ((end - start) % 2 == 1)
            return false;
        for (int i = start; i < end; i += 2)
        {
        	types.add(array[i]);
        	names.add(array[i + 1]);
        }
    	return true;
    }
    
    /**
     * Finds the index of the first instance of a particular string in a string array.
     * 
     * @param array The array to search through.
     * @param symbol The string to look for.
     * @return The index of the first instance of the string in the array, or -1 if it does not exist.
     */
    public int indexOfSymbol (String[] array, String symbol)
    {
    	for (int i = 0; i < array.length; ++i)
    		if (array[i].equals(symbol))
    			return i;
    	return -1;
    }
 }
 