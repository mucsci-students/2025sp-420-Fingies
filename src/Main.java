
/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {

	public static void main(String[] args) {
		GUIView GUIview = new GUIView();
		Controller controller = new Controller(GUIview, new JModel());
		GUIview.setController(controller);
		GUIview.addUMLClass("Class 1");
		GUIview.addUMLClass("Class 2");
		GUIview.addUMLClass("Class 3");
		controller.run();
	}

}
