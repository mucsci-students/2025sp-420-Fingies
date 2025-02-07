public class Controller {

    private View view;
    // todo: model field

    Controller (View view)
    {
        this.view = view;
    }

    public void doAddClass() {}
    public void doDeleteClass() {}
    public void doRenameClass() {}
    public void doAddRelationship() {}
    public void doDeleteRelationship() {}
    public void doAddAttribute() {}
    public void doDeleteAttribute() {}
    public void doRenameAttribute() {}
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
        Command command = null;
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
