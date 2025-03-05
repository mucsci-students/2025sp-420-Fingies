/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {

<<<<<<< HEAD
    public static void main(String[] args) {
        if (hasCLIFlag(args)){
            Controller controller = new Controller(new CLIView(), new JModel());
            controller.run();
        } else {
			// NEEDS TO BE UPDATED TO LAUNCH GUI AND NOT CLI
            Controller controller = new Controller(new CLIView(), new JModel());
            controller.run();
        }
    }
=======
	public static void main(String[] args) {
		GUIView view = new GUIView();
		Controller controller = new Controller(view, new JModel());
		view.setController(controller);
		view.addUMLClass("Class 1");
		view.addUMLClass("Class 2");
		view.addUMLClass("Class 3");

		view.removeUMLClass("Class 2");

		view.renameUMLClass("Class 1", "ClassA");
		view.run();
	}
>>>>>>> f472cba (added methods to remove a class and rename a class in the GUI and added randomly generated unique colors for each class)

	public static boolean hasCLIFlag(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--cli")) {
				return true;
			}
		}
		return false;
	}
}