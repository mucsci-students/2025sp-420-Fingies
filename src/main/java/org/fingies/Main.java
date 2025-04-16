package org.fingies;

import java.util.ArrayList;

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
            // view.run();

            String class1 = "ClassA";
            String class2 = "ClassB";
            String class3 = "zzzz";

            UMLClassHandler.createClass(class1);
            UMLClassHandler.createClass(class2);
            UMLClassHandler.createClass(class3);

            // Add fields and methods (if needed)
            UMLClassHandler.getClass(class1).addField("FieldA", "String");
            UMLClassHandler.getClass(class1).addField("FieldB", "String");
            UMLClassHandler.getClass(class1).addField("FieldC", "String");
            UMLClassHandler.getClass(class1).addField("FieldD", "String");
            UMLClassHandler.getClass(class1).addField("FieldE", "String");
            UMLClassHandler.getClass(class1).addField("FieldF", "String");

           // UMLClassHandler.getClass(class2).addField("FieldA");
           // UMLClassHandler.getClass(class2).addField("FieldAaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
           // Create a method in Class A (optional)
           ArrayList<String> params = new ArrayList<>();
           ArrayList<String> types = new ArrayList<>();
           
           UMLClassHandler.getClass(class3).addMethod("MethodC", "void", params, types);

           params.add("Param1");
           params.add("Param2");
           params.add("Param3");
           types.add("String");
           types.add("String");
           types.add("String");
           UMLClassHandler.getClass(class2).addMethod("MethodB", "void", params, types);

           params.add("Param4");
           params.add("Param5");
           types.add("String");
           types.add("String");
           UMLClassHandler.getClass(class1).addMethod("MethodA", "void", params, types);

           // Add the UML classes to the view
           view.addUMLClass(class1);
           view.addUMLClass(class2);
           view.addUMLClass(class3);

           // Add relationships between classes to test arrows
           RelationshipHandler.addRelationship(class1, class3, RelationshipType.Inheritance);
           RelationshipHandler.addRelationship(class3, class1, RelationshipType.Composition);
           RelationshipHandler.addRelationship(class2, class3, RelationshipType.Aggregation);
           RelationshipHandler.addRelationship(class1, class1, RelationshipType.Realization);
           RelationshipHandler.addRelationship(class3, class3, RelationshipType.Composition);
           RelationshipHandler.addRelationship(class3, class2, RelationshipType.Realization);

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
