package org.fingies;

import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {
  
	public static void main(String[] args) {
		// args = new String[] {"--cli"};
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

            UMLClassHandler.createClass("ClassA");
            UMLClassHandler.createClass("ClassB");
            UMLClassHandler.createClass("ClassC");

            // Add fields and methods (if needed)
            UMLClassHandler.getClass("ClassA").addField("FieldA");
            UMLClassHandler.getClass("ClassA").addField("FieldB");
            UMLClassHandler.getClass("ClassA").addField("FieldC");
            UMLClassHandler.getClass("ClassA").addField("FieldD");
            UMLClassHandler.getClass("ClassA").addField("FieldE");
            UMLClassHandler.getClass("ClassA").addField("FieldF");

            UMLClassHandler.getClass("ClassB").addField("FieldA");
            // UMLClassHandler.getClass("ClassB").addField("FieldAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            // Create a method in Class A (optional)
            ArrayList<String> arr = new ArrayList<>();
            arr.add("Param1");
            arr.add("Param2");
            arr.add("Param3");
            UMLClassHandler.getClass("ClassA").addMethod("abc", arr);
            UMLClassHandler.getClass("ClassB").addMethod("MethodB", arr);

            arr.add("Param4");
            arr.add("Param5");
            arr.add("Param6");
            arr.add("Param7");
            arr.add("Param8");
            arr.add("Param9");
            UMLClassHandler.getClass("ClassA").addMethod("a", arr);

            // Add the UML classes to the view
            view.addUMLClass("ClassA");
            view.addUMLClass("ClassB");
            view.addUMLClass("ClassC");

            // Add relationships between classes to test arrows
            RelationshipHandler.addRelationship("ClassA", "ClassC", RelationshipType.Inheritance);
            RelationshipHandler.addRelationship("ClassC", "ClassA", RelationshipType.Composition);
            RelationshipHandler.addRelationship("ClassB", "ClassC", RelationshipType.Aggregation);
            RelationshipHandler.addRelationship("ClassA", "ClassA", RelationshipType.Realization);
            RelationshipHandler.addRelationship("ClassC", "ClassC", RelationshipType.Composition);

            // Add arrows for these relationships
            view.addArrowForRelationship(RelationshipHandler.getRelationships().get(0));
            view.addArrowForRelationship(RelationshipHandler.getRelationships().get(1));

            RelationshipHandler.removeRelationship("ClassB", "ClassC");
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