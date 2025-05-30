import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import javax.swing.JComponent;

import org.junit.Test;
import org.fingies.Controller.Action;
import org.fingies.Controller.UMLController;
import org.fingies.Model.JModel;
import org.fingies.Model.Method;
import org.fingies.Model.Position;
import org.fingies.Model.Relationship;
import org.fingies.Model.RelationshipHandler;
import org.fingies.Model.RelationshipType;
import org.fingies.Model.UMLClass;
import org.fingies.Model.UMLClassHandler;
import org.fingies.View.InputCheck;
import org.fingies.View.UMLView;
import org.junit.After;
import org.junit.Before;

public class RedoTest { // TODO: test what happens if we redo after making a change after undoing, or undo after redoing
	
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
	
	// --------------------- CLASSES ---------------------
	
	@Test
	public void addClass()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
	}
	
	@Test
	public void addAndRemoveClass()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.REMOVE_CLASS, new String[] {"jerry"});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after removing it.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after undoing a Remove Class command.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.REDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
	}

	@Test
	public void addAndRenameClass()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.RENAME_CLASS, new String[] {"jerry", "mike"});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after renaming it to \"mike\".", UMLClassHandler.exists("jerry"));
		assertTrue("The UMLClassHandler should have a class named \"mike\" after renaming \"jerry\" to it.", UMLClassHandler.exists("mike"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after undoing a Rename Class command.", UMLClassHandler.exists("jerry"));
		assertFalse("The UMLClassHandler shouldn't have a class named \"mike\" after undoing a Rename Class command.", UMLClassHandler.exists("mike"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.REDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		assertTrue("The UMLClassHandler should have a class named \"mike\" after redoing.", UMLClassHandler.exists("mike"));
	}

	// --------------------- RELATIONSHIPS ---------------------

	@Test
	public void addRelationship()
	{
		// add classes
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.ADD_CLASS, new String[] {"yoshi"});
		assertTrue("The UMLClassHandler should have a class named \"yoshi\" after adding one.", UMLClassHandler.exists("yoshi"));
		
		// test
		controller.runHelper(Action.ADD_RELATIONSHIP, new String[] {"jerry", "yoshi", "aggr"});
		Set<Relationship> expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Aggregation));
		Set<Relationship> actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
		controller.runHelper(Action.UNDO, new String[] {});
		assertTrue("The RelationshipHandler shouldn't have a relationship going from \"jerry\" to \"yoshi\" after undoing an Add Relationship command.", RelationshipHandler.getRelationObjects().isEmpty());
		
		// undo adding classes
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"yoshi\" after undoing an Add Class command.", UMLClassHandler.exists("yoshi"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		// redo
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"yoshi\" after redoing.", UMLClassHandler.exists("yoshi"));
		
		controller.runHelper(Action.REDO, new String[] {});
		expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Aggregation));
		actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
	}

	@Test
	public void addAndRemoveRelationship()
	{
		// add classes
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.ADD_CLASS, new String[] {"yoshi"});
		assertTrue("The UMLClassHandler should have a class named \"yoshi\" after adding one.", UMLClassHandler.exists("yoshi"));
		
		// test
		controller.runHelper(Action.ADD_RELATIONSHIP, new String[] {"jerry", "yoshi", "aggr"});
		Set<Relationship> expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Aggregation));
		Set<Relationship> actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
		controller.runHelper(Action.REMOVE_RELATIONSHIP, new String[] {"jerry", "yoshi"});
		assertTrue("The RelationshipHandler shouldn't have a relationship going from \"jerry\" to \"yoshi\" after removing it.", RelationshipHandler.getRelationObjects().isEmpty());
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
		controller.runHelper(Action.UNDO, new String[] {});
		assertTrue("The RelationshipHandler shouldn't have a relationship going from \"jerry\" to \"yoshi\" after undoing an Add Relationship command.", RelationshipHandler.getRelationObjects().isEmpty());
		
		// undo adding classes
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"yoshi\" after undoing an Add Class command.", UMLClassHandler.exists("yoshi"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		// redo
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"yoshi\" after redoing.", UMLClassHandler.exists("yoshi"));
		
		controller.runHelper(Action.REDO, new String[] {});
		expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Aggregation));
		actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The RelationshipHandler shouldn't have a relationship going from \"jerry\" to \"yoshi\" after redoing.", RelationshipHandler.getRelationObjects().isEmpty());
	}

	@Test
	public void addAndChangeRelationship() 
	{
		// add classes
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.ADD_CLASS, new String[] {"yoshi"});
		assertTrue("The UMLClassHandler should have a class named \"yoshi\" after adding one.", UMLClassHandler.exists("yoshi"));
		
		// test
		controller.runHelper(Action.ADD_RELATIONSHIP, new String[] {"jerry", "yoshi", "aggr"});
		Set<Relationship> expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Aggregation));
		Set<Relationship> actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
		controller.runHelper(Action.CHANGE_RELATIONSHIP_TYPE, new String[] {"jerry", "yoshi", "comp"});
		expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Composition));
		actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Composition);
		controller.runHelper(Action.UNDO, new String[] {});
		expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Aggregation));
		actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
		controller.runHelper(Action.UNDO, new String[] {});
		assertTrue("The RelationshipHandler shouldn't have a relationship going from \"jerry\" to \"yoshi\" after undoing an Add Relationship command.", RelationshipHandler.getRelationObjects().isEmpty());
		
		// undo adding classes
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"yoshi\" after undoing an Add Class command.", UMLClassHandler.exists("yoshi"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		// redo
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"yoshi\" after redoing.", UMLClassHandler.exists("yoshi"));
		
		controller.runHelper(Action.REDO, new String[] {});
		expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Aggregation));
		actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Aggregation);
		controller.runHelper(Action.REDO, new String[] {});
		expected = Set.of(new Relationship(UMLClassHandler.getClass("jerry"), UMLClassHandler.getClass("yoshi"), RelationshipType.Composition));
		actual = Set.copyOf(RelationshipHandler.getRelationObjects());
		assertEquals("The RelationshipHandler had the wrong relationships.", expected, actual);
		assertEquals("", RelationshipHandler.getRelationObjects().get(0).getType(), RelationshipType.Composition);
	}

	
	
	// --------------------- FIELDS ---------------------
	

	@Test
	public void addField() {
	    controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
	    assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

	    controller.runHelper(Action.ADD_FIELD, new String[] {"Person", "age", "int"});
	    UMLClass personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\".", personClass.fieldExists("age"));

	    controller.runHelper(Action.UNDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertFalse("The class \"Person\" shouldn't have a field named \"age\" after undoing an Add Field command.", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.UNDO, new String[] {});
	    assertFalse("The UMLClassHandler shouldn't have a class named \"Person\" after undoing an Add Class command", UMLClassHandler.exists("Person"));
	    
	    controller.runHelper(Action.REDO, new String[] {});
	    assertTrue("The UMLClassHandler should have a class named \"Person\" after redoing.", UMLClassHandler.exists("Person"));

	    controller.runHelper(Action.REDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\".", personClass.fieldExists("age"));
	}
	
	@Test
	public void addAndRemoveField() {
	    controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
	    assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

	    controller.runHelper(Action.ADD_FIELD, new String[] {"Person", "age", "int"});
	    UMLClass personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\".", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.REMOVE_FIELD, new String[] {"Person", "age"});
	    personClass = UMLClassHandler.getClass("Person");
	    assertFalse("The class \"Person\" shouldn't have a field named \"age\" after removing it.", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.UNDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\" after undoing a Remove Field command.", personClass.fieldExists("age"));

	    controller.runHelper(Action.UNDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertFalse("The class \"Person\" shouldn't have a field named \"age\" after undoing an Add Field command.", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.UNDO, new String[] {});
	    assertFalse("The UMLClassHandler shouldn't have a class named \"Person\" after undoing an Add Class command", UMLClassHandler.exists("Person"));
	    
	    controller.runHelper(Action.REDO, new String[] {});
	    assertTrue("The UMLClassHandler should have a class named \"Person\" after redoing.", UMLClassHandler.exists("Person"));

	    controller.runHelper(Action.REDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\".", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.REDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertFalse("The class \"Person\" shouldn't have a field named \"age\" after redoing.", personClass.fieldExists("age"));
	}

	@Test
	public void addAndRenameField() {
	    controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
	    assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

	    controller.runHelper(Action.ADD_FIELD, new String[] {"Person", "age", "int"});
	    UMLClass personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\".", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.RENAME_FIELD, new String[] {"Person", "age", "bodyCount"});
	    personClass = UMLClassHandler.getClass("Person");
	    assertFalse("The class \"Person\" shouldn't have a field named \"age\" after renaming it to \"bodyCount\".", personClass.fieldExists("age"));
	    assertTrue("The class \"Person\" should have a field named \"bodyCount\" after renaming \"age\" to it.", personClass.fieldExists("bodyCount"));
	    
	    controller.runHelper(Action.UNDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\" after undoing a Rename Field command.", personClass.fieldExists("age"));
	    assertFalse("The class \"Person\" shouldn't have a field named \"bodyCount\" after undoing a Rename Field command.", personClass.fieldExists("bodyCount"));

	    controller.runHelper(Action.UNDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertFalse("The class \"Person\" shouldn't have a field named \"age\" after undoing an Add Field command.", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.UNDO, new String[] {});
	    assertFalse("The UMLClassHandler shouldn't have a class named \"Person\" after undoing an Add Class command.", UMLClassHandler.exists("Person"));
	    
	    controller.runHelper(Action.REDO, new String[] {});
	    assertTrue("The UMLClassHandler should have a class named \"Person\" after redoing.", UMLClassHandler.exists("Person"));

	    controller.runHelper(Action.REDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertTrue("The class \"Person\" should have a field named \"age\".", personClass.fieldExists("age"));
	    
	    controller.runHelper(Action.REDO, new String[] {});
	    personClass = UMLClassHandler.getClass("Person");
	    assertFalse("The class \"Person\" shouldn't have a field named \"age\" after redoing.", personClass.fieldExists("age"));
	    assertTrue("The class \"Person\" should have a field named \"bodyCount\" after redoing.", personClass.fieldExists("bodyCount"));
	}
		
	@Test
    public void addAndChangeFieldType()
    {
        controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
        assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

        controller.runHelper(Action.ADD_FIELD, new String[] {"Person", "age", "int"});
        UMLClass personClass = UMLClassHandler.getClass("Person");
        assertTrue("The class \"Person\" should contain the field \"age\".", personClass.fieldExists("age"));

        controller.runHelper(Action.CHANGE_FIELD_TYPE, new String [] {personClass.getName(), "age", "String"});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should contain the field \"age\" with the type of \"String\".", "String", personClass.getField("age").getType());

        controller.runHelper(Action.UNDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should have the correct type.", "int", personClass.getField("age").getType());
        
        controller.runHelper(Action.UNDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should have no fields.", List.of(), personClass.getFields());
        
        controller.runHelper(Action.UNDO, new String[] {});
        assertFalse("The UMLClassHandler shouldn't have a class named \"Person\" after undoing an Add Class command.", UMLClassHandler.exists("Person"));
        
        controller.runHelper(Action.REDO, new String[] {});
        assertTrue("The UMLClassHandler should have a class named \"Person\" after redoing.", UMLClassHandler.exists("Person"));

        controller.runHelper(Action.REDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertTrue("The class \"Person\" should contain the field \"age\".", personClass.fieldExists("age"));

        controller.runHelper(Action.REDO, new String [] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should contain the field \"age\" with the type of \"String\".", "String", personClass.getField("age").getType());
    }
	
	// --------------------- METHODS ---------------------
	
	@Test
	public void addMethodWithNoParams()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.ADD_METHOD, new String[] {"jerry", "method1", "void"});
		assertTrue("The class \"jerry\" should have a method named \"method1\" with no parameters after adding one.", UMLClassHandler.getClass("jerry").methodExists("method1", List.of()));
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" shouldn't have any methods after undoing an Add Method command.", List.of(), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The class \"jerry\" should have a method named \"method1\" with no parameters after redoing.", UMLClassHandler.getClass("jerry").methodExists("method1", List.of()));
	}
	
	@Test
	public void addMethodWithSomeParams()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.ADD_METHOD, new String[] {"jerry", "method1", "void", "int", "param1", "long", "param2"});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" shouldn't have any methods after undoing an Add Method command.", List.of(), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
	}
	
	@Test
	public void  addMethodThenRemoveMethodNoParams()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.ADD_METHOD, new String[] {"jerry", "method1", "void"});
		assertTrue("The class \"jerry\" should have a method named \"method1\" with no parameters after adding one.", UMLClassHandler.getClass("jerry").methodExists("method1", List.of()));
		
		controller.runHelper(Action.REMOVE_METHOD, new String[] {"jerry", "method1"});
		assertEquals("The class \"jerry\" should have no methods.", 0, UMLClassHandler.getClass("jerry").getMethods().size());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertTrue("The class \"jerry\" should have a method named \"method1\" with no parameters after adding one.", UMLClassHandler.getClass("jerry").methodExists("method1", List.of()));
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" shouldn't have any methods after undoing an Add Method command.", List.of(), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The class \"jerry\" should have a method named \"method1\" with no parameters after redoing.", UMLClassHandler.getClass("jerry").methodExists("method1", List.of()));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The class \"jerry\" should have no methods.", 0, UMLClassHandler.getClass("jerry").getMethods().size());
	}
	
	@Test
	public void addMethodThenRemoveMethodSomeParams()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.ADD_METHOD, new String[] {"jerry", "method1", "void", "int", "param1", "long", "param2"});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.REMOVE_METHOD, new String[] {"jerry", "method1", "int", "long"});
		assertEquals("The class \"jerry\" should have no methods.", 0, UMLClassHandler.getClass("jerry").getMethods().size());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" shouldn't have any methods after undoing an Add Method command.", List.of(), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The class \"jerry\" should have no methods.", 0, UMLClassHandler.getClass("jerry").getMethods().size());
	}
	
	@Test
	public void addMethodThenChangeReturnTypeNoParams()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.ADD_METHOD, new String[] {"jerry", "method1", "void"});
		assertTrue("The class \"jerry\" should have a method named \"method1\" with no parameters after adding one.", UMLClassHandler.getClass("jerry").methodExists("method1", List.of()));
		
		controller.runHelper(Action.CHANGE_METHOD_RETURN_TYPE, new String[] {"jerry", "method1", "String"});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "String", List.of(), List.of())), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of(), List.of())), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" shouldn't have any methods after undoing an Add Method command.", List.of(), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The class \"jerry\" should have a method named \"method1\" with no parameters after redoing.", UMLClassHandler.getClass("jerry").methodExists("method1", List.of()));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "String", List.of(), List.of())), UMLClassHandler.getClass("jerry").getMethods());
	}
	
	@Test
	public void addMethodThenChangeReturnTypeSomeParams()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.ADD_METHOD, new String[] {"jerry", "method1", "void", "int", "param1", "long", "param2"});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.CHANGE_METHOD_RETURN_TYPE, new String[] {"jerry", "method1", "int", "long", "String"});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "String", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The class \"jerry\" shouldn't have any methods after undoing an Add Method command.", List.of(), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "void", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
		
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The class \"jerry\" should have the correct methods.", List.of(new Method("method1", "String", List.of("param1", "param2"), List.of("int", "long"))), UMLClassHandler.getClass("jerry").getMethods());
	}
	
	// --------------------- POSITION ---------------------
	
	@Test
	public void addClassThenMove()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		
		Position oldPos = new Position(UMLClassHandler.getClass("jerry").getPosition());
		
		controller.runHelper(Action.MOVE, new String[] {"jerry", "20", "30"});
		assertEquals("The position should be correct after moving the class.", new Position(20, 30), UMLClassHandler.getClass("jerry").getPosition());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertEquals("The position should be correct after undoing.", oldPos, UMLClassHandler.getClass("jerry").getPosition());
		
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
		
		controller.runHelper(Action.REDO, new String[] {});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after redoing.", UMLClassHandler.exists("jerry"));
				
		controller.runHelper(Action.REDO, new String[] {});
		assertEquals("The position should be correct after redoing.", new Position(20, 30), UMLClassHandler.getClass("jerry").getPosition());
	}
	
	// --------------------- PARAMETERS ---------------------
	
	@Test
    public void addParam()
    {
        controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
        assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

        controller.runHelper(Action.ADD_METHOD, new String[] {"Person", "getName", "String"});
        UMLClass personClass = UMLClassHandler.getClass("Person");
        assertTrue("The class \"Person\" should contain the method \"getName\".", personClass.methodExists("getName", List.of()));

        controller.runHelper(Action.ADD_PARAMETERS, new String[] {personClass.getName(), personClass.getMethods().get(0).getName(), ";", "num", "int"});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should have the correct method.", List.of(new Method("getName", "String", List.of("int"), List.of("num"))), personClass.getMethods());

        controller.runHelper(Action.UNDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should have the correct method.", List.of(new Method("getName", "String", List.of(), List.of())), personClass.getMethods());
        
        controller.runHelper(Action.REDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should have the correct method.", List.of(new Method("getName", "String", List.of("int"), List.of("num"))), personClass.getMethods());
    }
	
	@Test
    public void addAndRemoveParam()
    {
        controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
        assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

        controller.runHelper(Action.ADD_METHOD, new String[] {"Person", "getName", "String"});
        UMLClass personClass = UMLClassHandler.getClass("Person");
        assertTrue("The class \"Person\" should contain the method \"getName\".", personClass.methodExists("getName", List.of()));

        controller.runHelper(Action.ADD_PARAMETERS, new String[] {personClass.getName(), personClass.getMethods().get(0).getName(), ";", "int", "num"});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));

        controller.runHelper(Action.REMOVE_PARAMETERS, new String[] {personClass.getName(), "getName", "int", ";", "num"});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals(true, personClass.getMethods().get(0).getParameterTypes().isEmpty());
        assertEquals(true, personClass.getMethods().get(0).getParameterNames().isEmpty());

        controller.runHelper(Action.UNDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));
        
        controller.runHelper(Action.REDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals(true, personClass.getMethods().get(0).getParameterTypes().isEmpty());
        assertEquals(true, personClass.getMethods().get(0).getParameterNames().isEmpty());
    }
	
	@Test
    public void addAndRenameParam()
    {
        controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
        assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

        controller.runHelper(Action.ADD_METHOD, new String[] {"Person", "getName", "String"});
        UMLClass personClass = UMLClassHandler.getClass("Person");
        assertTrue("The class \"Person\" should contain the method \"getName\".", personClass.methodExists("getName", List.of()));

        controller.runHelper(Action.ADD_PARAMETERS, new String[] {personClass.getName(), personClass.getMethods().get(0).getName(), ";", "int", "num"});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));

        controller.runHelper(Action.RENAME_PARAMETER, new String[] {personClass.getName(), "getName", "int", "num", "bobby"});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
        assertEquals("bobby", personClass.getMethods().get(0).getParameterNames().get(0));

        controller.runHelper(Action.UNDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));
        
        controller.runHelper(Action.REDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
        assertEquals("bobby", personClass.getMethods().get(0).getParameterNames().get(0));
    }
	
	 @Test
	 public void addAndChangeParamType()
	 {
		 controller.runHelper(Action.ADD_CLASS, new String[] {"Person"});
	        assertTrue("The UMLClassHandler should have a class named \"Person\" after adding one.", UMLClassHandler.exists("Person"));

	        controller.runHelper(Action.ADD_METHOD, new String[] {"Person", "getName", "String"});
	        UMLClass personClass = UMLClassHandler.getClass("Person");
	        assertTrue("The class \"Person\" should contain the method \"getName\".", personClass.methodExists("getName", List.of()));

	        controller.runHelper(Action.ADD_PARAMETERS, new String[] {personClass.getName(), personClass.getMethods().get(0).getName(), ";", "int", "num"});
	        personClass = UMLClassHandler.getClass("Person");
	        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
	        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));
	        
	        controller.runHelper(Action.CHANGE_PARAMETER_TYPE, new String[] {"Person", "getName", "int", "num", "short"});
	        personClass = UMLClassHandler.getClass("Person");
	        assertEquals("short", personClass.getMethods().get(0).getParameterTypes().get(0));
	        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));
	        
	        controller.runHelper(Action.UNDO, new String[] {});
	        personClass = UMLClassHandler.getClass("Person");
	        assertEquals("int", personClass.getMethods().get(0).getParameterTypes().get(0));
	        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));
	        
	        controller.runHelper(Action.REDO, new String[] {});
	        personClass = UMLClassHandler.getClass("Person");
	        assertEquals("short", personClass.getMethods().get(0).getParameterTypes().get(0));
	        assertEquals("num", personClass.getMethods().get(0).getParameterNames().get(0));
	 }


}
