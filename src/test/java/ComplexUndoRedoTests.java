import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.swing.JComponent;

import org.junit.Test;
import org.fingies.Controller.Action;
import org.fingies.Controller.UMLController;
import org.fingies.Model.JModel;
import org.fingies.Model.Relationship;
import org.fingies.Model.RelationshipHandler;
import org.fingies.Model.RelationshipType;
import org.fingies.Model.UMLClass;
import org.fingies.Model.UMLClassHandler;
import org.fingies.View.InputCheck;
import org.fingies.View.UMLView;
import org.junit.After;
import org.junit.Before;

/**
 * @author Lincoln Craddock
 */
public class ComplexUndoRedoTests {
	
	UMLController controller;
	
	@Before
	public void setUp()
	{
		// dummy view
		UMLView view = new UMLView() {
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
		    @Override public void setController(UMLController c) {}
			@Override public int promptForYesNoInput(String message, String title) { return 2; }
			@Override public JComponent getJComponentRepresentation() { return null; }
		};

        controller = new UMLController(view, new JModel());
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
	
	@Test
	public void test2()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.ADD_RELATIONSHIP, new String[] {"jerry", "jerry", "comp"});
		assertEquals("The RelationshipHandler should have the correct relationships after adding a composition relationship between \"jerry\" and itself.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after adding a composition relationship between \"jerry\" and itself.",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.RENAME_CLASS, new String[] {"jerry", "jerry2"});
		assertTrue("The UMLClassHandler should have a class named \"jerry2\" after renaming \"jerry\" to it.", UMLClassHandler.exists("jerry2"));
		assertEquals("The RelationshipHandler should have the correct relationships after renaming \"jerry\" to \"jerry2\".",
				List.of(new Relationship(new UMLClass("jerry2"), new UMLClass("jerry2"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after renaming \\\"jerry\\\" to \"jerry2\".",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after undoing a Rename Class action.", UMLClassHandler.exists("jerry"));
		assertEquals("The RelationshipHandler should have the correct relationships after undoing a Rename Class action.",
				List.of(new Relationship(new UMLClass("jerry"), new UMLClass("jerry"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after undoing a Rename Class action.",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
		
		controller.runHelper(Action.RENAME_CLASS, new String[] {"jerry", "jerry2"});
		assertTrue("The UMLClassHandler should have a class named \"jerry2\" after renaming \"jerry\" to it.", UMLClassHandler.exists("jerry2"));
		assertEquals("The RelationshipHandler should have the correct relationships after renaming \"jerry\" to \"jerry2\".",
				List.of(new Relationship(new UMLClass("jerry2"), new UMLClass("jerry2"), RelationshipType.DEFAULT)),
				RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler should have the correct relationship types for all of its relationships after renaming \\\"jerry\\\" to \"jerry2\".",
				List.of(RelationshipType.Composition.getName()),
				RelationshipHandler.getRelationObjects().stream().map(x -> x.getType().getName()).toList());
	}
	
	
}


