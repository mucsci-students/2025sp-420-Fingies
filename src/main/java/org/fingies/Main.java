package org.fingies;

import java.util.ArrayList;

import org.fingies.Controller.UMLController;
import org.fingies.Model.JModel;
import org.fingies.Model.RelationshipHandler;
import org.fingies.Model.RelationshipType;
import org.fingies.Model.UMLClassHandler;
import org.fingies.View.CLIView;
import org.fingies.View.GUIView;

/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {
  
	public static void main(String[] args) {
		args = new String[] {"--cli"};
        if (hasCLIFlag(args)) {
            CLIView view = new CLIView();
            UMLController controller = new UMLController(view, new JModel());
		    view.setController(controller);
            view.run();
        }
        else {
            GUIView view = new GUIView();
            UMLController controller = new UMLController(view, new JModel());
            view.setController(controller);

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
