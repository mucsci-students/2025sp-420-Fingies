public class Controller {
    private View view;
    private JModel model;

    Controller (View view, JModel model)
    {
        this.view = view;
        this.model = model;
    }

    public boolean doAddClass(String className) 
    {
        return UMLClassHandler.createClass(className);
    }

    public boolean doDeleteClass(String className) 
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

    public void doDeleteRelationship(String srcClass, String destClass) 
    {
        UMLClassHandler.removeRelationship(srcClass, destClass);
    }

    public boolean doAddAttribute(String srcClass, String attribute) 
    {
        return UMLClassHandler.getClass(srcClass).addAttribute(attribute);
    }

    public boolean doDeleteAttribute(String srcClass, String attribute) 
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

    public void doListClasses() {}
    public void doListClass() {}
    public void doListRelationships() {}
    public void doHelp() {}

    /**
     * Starts program and checks/follows commands until exit.
     */
    public void run ()
    {
        Action action = null;
        do {
            //acquire command from view
            switch(action) {
                case ADD_CLASS:
                    //do stuff
                    break;
                case REMOVE_CLASS:
                    //do stuff
                    break;
                case RENAME_CLASS:
                    //do stuff
                    break;
                case ADD_RELATIONSHIP:
                    //do stuff
                    break;
                case REMOVE_RELATIONSHIP:
                    //do stuff
                    break;
                case ADD_ATTRIBUTE:
                    //do stuff
                    break;
                case REMOVE_ATTRIVUTE:
                    //do stuff
                    break;
                case RENAME_ATTRIBUTE:
                    //do stuff
                    break;
                case SAVE:
                    //do stuff
                    break;
                case LOAD:
                    //do stuff
                    break;
                case LIST_CLASSES:
                    //do stuff
                    break;
                case LIST_CLASS:
                    //do stuff
                    break;
                case LIST_RELATIONSHIPS:
                    //do stuff
                    break;
                case HELP:
                    //do stuff
                    break;
                case EXIT:
                    break;
            }
        } while (!action.equals(action.EXIT));
    }
 }
 