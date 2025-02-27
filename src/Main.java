
/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {

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

}
