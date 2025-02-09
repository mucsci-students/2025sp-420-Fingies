public class Controller {

    private View view;
    // todo: model field

    Controller (View view)
    {
        this.view = view;
    }

    public boolean doAddClass(String className) {
        return UMLClassHandler.createClass(className);
    }
    public boolean doDeleteClass(String className) {
        return UMLClassHandler.removeClass(className);
    }
    public boolean doRenameClass(String className, String newName) {
        return UMLClassHandler.renameClass(className, newName);
    }
    public void doAddRelationship(String srcClass, String destClass) {
        UMLClassHandler.addRelationship(srcClass, destClass);
    }
    public void doDeleteRelationship(String srcClass, String destClass) {
        UMLClassHandler.removeRelationship(srcClass, destClass);
    }
    public boolean doAddAttribute(String srcClass, String attribute) {
        return UMLClassHandler.getClass(srcClass).addAttribute(attribute);
    }
    public boolean doDeleteAttribute(String srcClass, String attribute) {
        return UMLClassHandler.getClass(srcClass).removeAttribute(attribute);
    }
    public boolean doRenameAttribute(String srcClass, String oldAttribute, String newAttribute) {
        return UMLClassHandler.getClass(srcClass).renameAttribute(oldAttribute, newAttribute);
    }
    public void doSave() {}
    public void doLoad() {}
    public void doListClasses() {}
    public void doListClass() {}
    public void doListRelationships() {}
    public void doHelp() {}

    /**
     * Starts program and checks/follows commands until exit.
     */
    public void run () 
    {
        OutdatedCommand command = null;
        do {
            //acquire command from view
            switch(command) {
                case ADDCLASS:
                    //do stuff
                    break;
                case DELETECLASS:
                    //do stuff
                    break;
                case RENAMECLASS:
                    //do stuff
                    break;
                case ADDRELATIONSHIP:
                    //do stuff
                    break;
                case DELETERELATIONSHIP:
                    //do stuff
                    break;
                case ADDATTRIBUTE:
                    //do stuff
                    break;
                case DELETEATTRIBUTE:
                    //do stuff
                    break;
                case RENAMEATTRIBUTE:
                    //do stuff
                    break;
                case SAVE:
                    //do stuff
                    break;
                case LOAD:
                    //do stuff
                    break;
                case LISTCLASSES:
                    //do stuff
                    break;
                case LISTCLASS:
                    //do stuff
                    break;
                case LISTRELATIONSHIPS:
                    //do stuff
                    break;
                case HELP:
                    //do stuff
                    break;
                case EXIT:
                    break;
            }
        } while (!command.equals(command.EXIT));
    }
}
