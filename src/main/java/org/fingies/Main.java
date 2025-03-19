package org.fingies;

import java.util.ArrayList;

/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {
  
	public static void main(String[] args) {
		//args = new String[] {"--cli"};
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
            // view.run();

            UMLClassHandler.createClass("ClassA");
            UMLClassHandler.createClass("ClassB");
            UMLClassHandler.createClass("ClassC");
//
//           // Add fields and methods (if needed)
            UMLClassHandler.getClass("ClassA").addField("FieldA", "String");
            UMLClassHandler.getClass("ClassA").addField("FieldB", "String");
            UMLClassHandler.getClass("ClassA").addField("FieldC", "String");
            UMLClassHandler.getClass("ClassA").addField("FieldD", "String");
            UMLClassHandler.getClass("ClassA").addField("FieldE", "String");
            UMLClassHandler.getClass("ClassA").addField("FieldF", "String");

           // UMLClassHandler.getClass("ClassB").addField("FieldA");
           // UMLClassHandler.getClass("ClassB").addField("FieldAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
           // Create a method in Class A (optional)
           ArrayList<String> params = new ArrayList<>();
           ArrayList<String> types = new ArrayList<>();
           params.add("Param1");
           params.add("Param2");
           params.add("Param3");
           types.add("String");
           types.add("String");
           types.add("String");
           UMLClassHandler.getClass("ClassB").addMethod("MethodB", "void", params, types);

           params.add("Param4");
           params.add("Param5");
           types.add("String");
           types.add("String");
           UMLClassHandler.getClass("ClassA").addMethod("MethodA", "void", params, types);
//
//            // Add the UML classes to the view
           view.addUMLClass("ClassA");
           view.addUMLClass("ClassB");
           view.addUMLClass("ClassC");
//
//            // Add relationships between classes to test arrows
           RelationshipHandler.addRelationship("ClassA", "ClassC", RelationshipType.Inheritance);
           RelationshipHandler.addRelationship("ClassC", "ClassA", RelationshipType.Composition);
           RelationshipHandler.addRelationship("ClassB", "ClassC", RelationshipType.Aggregation);
           RelationshipHandler.addRelationship("ClassA", "ClassA", RelationshipType.Realization);
           RelationshipHandler.addRelationship("ClassC", "ClassC", RelationshipType.Composition);
           RelationshipHandler.addRelationship("ClassC", "ClassB", RelationshipType.Realization);
//
//            // Add arrows for these relationships
//            view.addArrowForRelationship(RelationshipHandler.getRelationships().get(0));
//            view.addArrowForRelationship(RelationshipHandler.getRelationships().get(1));

            // RelationshipHandler.removeRelationship("ClassB", "ClassC");
            // //view.removeArrowForRelationship(RelationshipHandler.getRelationships().get(1));

            // // Test updating arrows (in case relationships are modified or removed)
            view.updateArrows();  // Call this to refresh and redraw arrows

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