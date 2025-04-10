import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import org.junit.Assert;
import org.junit.Test;
import org.fingies.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Lincoln Craddock
 */
public class ComplexUndoRedoTests {
	
	Controller controller;
	
	@Before
	public void setUp()
	{
		// dummy view
		View view = new View() {
		    @Override public void run() {}
		    @Override public String promptForSaveInput(String message) { return null; }
		    @Override public String promptForOpenInput(String message) { return null; }
		    @Override public String promptForInput(String message) { return null; }
		    @Override public List<String> promptForInput(List<String> messages) { return null; }
		    @Override public List<String> promptForInput(List<String> messages, List<InputCheck> checks) { return null; }
		    @Override public void notifySuccess() {}
		    @Override public void notifySuccess(String message) {}
		    @Override public void notifyFail(String message) { throw new IllegalArgumentException ("Notify fail was called: \n" + message); }
		    @Override public void display(String message) {}
		    @Override public void help() {}
		    @Override public void help(String command) {}
		    @Override public void setController(Controller c) {}
		};

        controller = new Controller(view, new JModel());
	    view.setController(controller);
	    
	}
	
	@After
	public void resetTest()
	{
		UMLClassHandler.reset();
		RelationshipHandler.reset();
	}
	
	// --------------------- TESTS ---------------------
	
	@Test
	public void addClassAddRelationshipChangeRelationshipType()
	{
		// add class
		
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		// add relationship
		
		controller.runHelper(Action.ADD_RELATIONSHIP, new String[] {"jerry", "jerry", "comp"});
		assertEquals("The RelationshipHandler should have the correct relationships after adding a composition relationship between \"jerry\" and itself.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after adding a composition relationship between \"jerry\" and itself.",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after undoing an Add Relationship command.",
				List.of(),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after undoing an Add Relationship command.",
				List.of(),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after redoing an Add Relationship command.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after redoing an Add Relationship command.",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		// change relationship type
		
		controller.runHelper(Action.CHANGE_RELATIONSHIP_TYPE, new String[] {"jerry", "jerry", "aggr"});
		assertEquals("The RelationshipHandler should have the correct relationships after changing the type of the relationship between \"jerry\" and itself.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after changing the type of the relationship between \"jerry\" and itself to aggregation.",
				List.of(RelationshipType.Aggregation.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after undoing a Change Relationship Type command.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after undoing a Change Relationship Type command.",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after redoing a Change Relationship Type command..",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after redoing a Change Relationship Type command.",
				List.of(RelationshipType.Aggregation.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		// a bunch of undos and redos
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after undoing a redone Change Relationship Type command.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after undoing redone a Change Relationship Type command.",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after undoing a redone Add Relationship command.",
				List.of(),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after undoing a redone Add Relationship command.",
				List.of(),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing a redone Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after redoing an Add Relationship command.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after redoing an Add Relationship command.",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The RelationshipHandler should have the correct relationships after redoing a Change Relationship Type command..",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after redoing a Change Relationship Type command.",
				List.of(RelationshipType.Aggregation.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
	}
}


