package org.fingies;

import java.util.ArrayList;

/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {
  
	public static void main(String[] args) {
        if (hasCLIFlag(args)) {
            CLIView view = new CLIView();
            Controller controller = new Controller(view, new JModel());
		    view.setController(controller);
            view.run();
        }
        else {
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

            UMLClassHandler.getClass("Class B").addField("FieldA");
            UMLClassHandler.getClass("Class B").addField("FieldAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            // Create a method in Class A (optional)
            ArrayList<String> arr = new ArrayList<>();
            arr.add("Param1");
            arr.add("Param2");
            arr.add("Param3");
            UMLClassHandler.getClass("Class B").addMethod("MethodB", arr);

            arr.add("Param4");
            arr.add("Param5");
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
            //view.removeArrowForRelationship(RelationshipHandler.getRelationships().get(1));

            // Test updating arrows (in case relationships are modified or removed)
            view.updateArrows();  // Call this to refresh and redraw arrows

            // Run the GUI
            view.run();
        }
        
		

	}

	public static boolean hasCLIFlag(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--cli")) {
				return true;
			}
		}
		return false;
	}
}