<<<<<<< HEAD
=======
import java.util.ArrayList;

>>>>>>> 1faecf0 (created lines to be drawn between classes to represent relationships)
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

		UMLClassHandler.createClass("Class A");
        UMLClassHandler.createClass("Class B");
        UMLClassHandler.createClass("Class C");

        // Add fields and methods (if needed)
        UMLClassHandler.getClass("Class A").addField("FieldA");
        UMLClassHandler.getClass("Class A").addField("FieldB");
        UMLClassHandler.getClass("Class A").addField("FieldC");
        UMLClassHandler.getClass("Class A").addField("FieldD");
        UMLClassHandler.getClass("Class A").addField("FieldE");
        UMLClassHandler.getClass("Class A").addField("FieldF");

        // Create a method in Class A (optional)
        ArrayList<String> arr = new ArrayList<>();
        arr.add("Param1");
        arr.add("Param2");
        arr.add("Param3");
        UMLClassHandler.getClass("Class A").addMethod("MethodA", arr);

        // Add the UML classes to the view
        view.addUMLClass("Class A");
        view.addUMLClass("Class B");
        view.addUMLClass("Class C");

        // Add relationships between classes to test arrows
        RelationshipHandler.addRelationship("Class A", "Class B", RelationshipType.AGGREGATION);
        RelationshipHandler.addRelationship("Class B", "Class C", RelationshipType.COMPOSITION);
		RelationshipHandler.addRelationship("Class A", "Class A", RelationshipType.AGGREGATION);

        // Add arrows for these relationships
        view.addArrowForRelationship(RelationshipHandler.getRelationships().get(0));
        view.addArrowForRelationship(RelationshipHandler.getRelationships().get(1));

		RelationshipHandler.removeRelationship("Class B", "Class C");
		// view.removeArrowForRelationship(RelationshipHandler.getRelationships().get(1));

        // Test updating arrows (in case relationships are modified or removed)
        view.updateArrows();  // Call this to refresh and redraw arrows

        

        // Run the GUI
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