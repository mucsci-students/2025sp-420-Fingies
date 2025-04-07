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
 * @author Nick Hayes & Lincoln Craddock
 */
public class UndoTest {
	
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
	
	// --------------------- CLASSES ---------------------
	
	@Test
	public void addClass()
	{
		controller.runHelper(Action.ADD_CLASS, new String[] {"jerry"});
		assertTrue("The UMLClassHandler should have a class named \"jerry\" after adding one.", UMLClassHandler.exists("jerry"));
		controller.runHelper(Action.UNDO, new String[] {});
		assertFalse("The UMLClassHandler shouldn't have a class named \"jerry\" after undoing an Add Class command.", UMLClassHandler.exists("jerry"));
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
	}
	
	@Test
	public void randomClassCommands()
	{
		List<String> classes = new ArrayList<>();
		int classNum = 0;
		while (++classNum < 10) // add 10 classes
		{
			classes.add("class" + classNum);
			controller.runHelper(Action.ADD_CLASS, new String[] {"class" + classNum});
			assertTrue("The UMLClassHandler should have a class named \"class" + classNum + "\" after adding it.", UMLClassHandler.exists("class" + classNum));
		}
		
		Stack<List<String>> oldClasses = new Stack<List<String>>();
		Random rand = new Random(7); // seed can be changed to get a new set of random values
		for (int i = 0; i < 9999; ++i) // perform lots of random commands
		{
			int choice;
			if (classes.size() > 0)
				choice = rand.nextInt(0, 3); // add, remove, or rename
			else
				choice = 0; // add only
			
			if (oldClasses.size() > 0 && rand.nextInt(0, 2) == 1) // to undo or not undo
				choice = 3;
			
			switch (choice)
			{
				case 0: // add
					oldClasses.push(new ArrayList<>(classes));
					classes.add("class" + ++classNum);
					controller.runHelper(Action.ADD_CLASS, new String[] {"class" + classNum});
					assertTrue("The UMLClassHandler should have a class named \"class" + classNum + "\" after adding it.", UMLClassHandler.exists("class" + classNum));
					break;
				case 1: // remove
					oldClasses.push(new ArrayList<>(classes));
					String classToRemove = classes.get(rand.nextInt(classes.size()));
					classes.remove(classToRemove);
					controller.runHelper(Action.REMOVE_CLASS, new String[] {classToRemove});
					assertFalse("The UMLClassHandler shouldn't have a class named \"" + classToRemove + "\" after removing it.", UMLClassHandler.exists(classToRemove));
					break;
				case 2: // rename
					oldClasses.push(new ArrayList<>(classes));
					String classToRename = classes.get(rand.nextInt(classes.size()));
					classes.remove(classToRename);
					classes.add("class" + ++classNum);
					controller.runHelper(Action.RENAME_CLASS, new String[] {classToRename, "class" + classNum});
					assertFalse("The UMLClassHandler shouldn't have a class named \"" + classToRename + "\" after renaming it.", UMLClassHandler.exists(classToRename));
					assertTrue("The UMLClassHandler should have a class named \"class" + classNum + "\" after renaming \"" + classToRename + "\" to it.", UMLClassHandler.exists("class" + classNum));
					break;
				case 3: // undo
					classes = oldClasses.pop();
					controller.runHelper(Action.UNDO, new String[] {});
					Set<String> actualClasses = new HashSet<>(); // a set of the names of the classes in UMLClassHandler
					UMLClassHandler.getAllClasses().forEach(x -> actualClasses.add(x.getName()));
					assertEquals("UMLClassHandler has the wrong classes after undoing a command.", new HashSet<String>(classes), actualClasses);
					break;
				default:
					throw new IllegalStateException("How did we get here? The variable 'choice' is " + choice + " but should be between 0 and 3.");
			}
		}
		
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

        controller.runHelper(Action.ADD_PARAMETERS, new String[] {personClass.getName(), personClass.getMethods().get(0).getName(), ";", "int", "num"});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should have the correct method.", List.of(new Method("getName", "String", List.of("num"), List.of("int"))), personClass.getMethods());

        controller.runHelper(Action.UNDO, new String[] {});
        personClass = UMLClassHandler.getClass("Person");
        assertEquals("The class \"Person\" should have the correct method.", List.of(new Method("getName", "String", List.of(), List.of())), personClass.getMethods());
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
	 }


}
