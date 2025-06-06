package ModelTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import org.fingies.Model.Relationship;
import org.fingies.Model.RelationshipHandler;
import org.fingies.Model.RelationshipType;
import org.fingies.Model.UMLClass;
import org.fingies.Model.UMLClassHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RelationshipHandlerTest {

     @Before
    public void setUp()
    {
        UMLClassHandler.reset();
        UMLClassHandler.createClass("A");
        UMLClassHandler.createClass("B");
        UMLClassHandler.createClass("C");
        UMLClassHandler.createClass("D");
    }

    @After
    public void resetTest()
    {
        RelationshipHandler.reset();
    }

    // --------------------- TESTS ---------------------

    @Test
    public void removeAllRelationshipsForClassname_ThenAllRelationshipsWithClassnameAreRemoved()
    {
        UMLClassHandler.createClass("1");
        UMLClassHandler.createClass("2");
        UMLClassHandler.createClass("3");
        RelationshipHandler.addRelationship("1", "2", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("1", "3", RelationshipType.Aggregation);
        assertTrue(RelationshipHandler.exists("1", "2"));
        assertTrue(RelationshipHandler.exists("1", "3"));
        RelationshipHandler.removeAllRelationshipsForClassname("1");
        assertFalse(RelationshipHandler.exists("1", "2"));
        assertFalse(RelationshipHandler.exists("1", "3"));
    }

    @Test
    public void addOneRelationship_ThenRelationshipIsAdded()
    {
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        assertTrue(RelationshipHandler.exists("A", "B"));
    }

    @Test
    public void addOneRelationshipThatExists_ThenIllegalArgumentExceptionThrown()
    {
    	RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        try
        {
            RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
            
            // shouldn't run
            assertTrue("Adding a relationship that already exists should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("This relationship already exists", e.getMessage());
        }
    }

    @Test
    public void removeOneRelationship_ThenRelationshipIsRemoved()
    {
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        assertTrue(RelationshipHandler.exists("A", "B"));
        RelationshipHandler.removeRelationship("A", "B");
        assertFalse(RelationshipHandler.exists("A", "B"));
    }

    @Test
    public void removeOneRelationshipThatDNE_ThenIllegalArgumentExceptionThrown()
    {
        try
        {
            RelationshipHandler.removeRelationship("A", "B");
            
            // shouldn't run
            assertTrue("Removing a relationship that doesn't exist should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("This relationship does not exist", e.getMessage());
        }
    }

    @Test
    public void changeOneRelationshipType_ThenRelationshipIsRemoved()
    {
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        assertTrue(RelationshipHandler.getRelationObjects().get(0).getType().equals(RelationshipType.Aggregation));
        RelationshipHandler.changeRelationshipType("A", "B", RelationshipType.Composition);
        assertTrue(RelationshipHandler.getRelationObjects().get(0).getType().equals(RelationshipType.Composition));
    }

    @Test
    public void changeOneRelationshipTypeThatDNE_ThenIllegalArgumentExceptionThrown()
    {
        try
        {
            RelationshipHandler.changeRelationshipType("A", "B", RelationshipType.Aggregation);
            
            // shouldn't run
            assertTrue("Changing the type of a relationship that doesn't exist should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("This relationship does not exist", e.getMessage());
        }
    }
    
    @Test
    public void relationshipHandlerListRelationships()
    {
    	// tests whether the resturned string at least contains the necessary information
    	// doesn't care what the particular format is
        assertEquals(RelationshipHandler.listRelationships(), "There are no current relationships");
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        String result = RelationshipHandler.listRelationships();
        assertTrue("The string returned by listRelationships() should contain the class name \"A\", but was: \n" + result, result.contains("A"));
        assertTrue("The string returned by listRelationships() should contain the class name \"B\", but was: \n" + result, result.contains("B"));
        assertTrue("The string returned by listRelationships() should contain the relationship type \"" + RelationshipType.Aggregation.getName() + "\", but was: \n" + result, result.contains(RelationshipType.Aggregation.getName()));
        
        RelationshipHandler.addRelationship("C", "D", RelationshipType.Composition);
        result = RelationshipHandler.listRelationships();
        assertTrue("The string returned by listRelationships() should contain the class name \"A\", but was: \n" + result, result.contains("A"));
        assertTrue("The string returned by listRelationships() should contain the class name \"B\", but was: \n" + result, result.contains("B"));
        assertTrue("The string returned by listRelationships() should contain the relationship type \"" + RelationshipType.Aggregation.getName() + "\", but was: \n" + result, result.contains(RelationshipType.Aggregation.getName()));
        assertTrue("The string returned by listRelationships() should contain the class name \"C\", but was: \n" + result, result.contains("C"));
        assertTrue("The string returned by listRelationships() should contain the class name \"D\", but was: \n" + result, result.contains("D"));
        assertTrue("The string returned by listRelationships() should contain the relationship type \"" + RelationshipType.Aggregation.getName() + "\", but was: \n" + result, result.contains(RelationshipType.Composition.getName()));
    }

    @Test
    public void testReplaceSrcAndDest() {
        RelationshipHandler.addRelationship("A", "A", RelationshipType.Aggregation);
        UMLClass classA = new UMLClass("A");
        UMLClass classB = new UMLClass("B");
        RelationshipHandler.replace(classA, classB);
        assertEquals(RelationshipHandler.listRelationships(), "B ----◇ B (Aggregation)");
    }

    @Test
    public void testGetAllRelationshipsByClassname() {
        RelationshipHandler.addRelationship("A", "A", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("C", "D", RelationshipType.Composition);
        List<Relationship> relA = RelationshipHandler.getAllRelationshipsForClassname("A");
        List<Relationship> relB = RelationshipHandler.getAllRelationshipsForClassname("C");
        assertNotNull(relA);
        assertNotNull(relB);
    }

    @Test
    public void testRemoveAllRelationshipsByClassname() {
        RelationshipHandler.addRelationship("A", "A", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("C", "D", RelationshipType.Composition);
        RelationshipHandler.removeAllRelationshipsForClassname("A");
        RelationshipHandler.removeAllRelationshipsForClassname("C");
        List<Relationship> relA = RelationshipHandler.getAllRelationshipsForClassname("A");
        List<Relationship> relB = RelationshipHandler.getAllRelationshipsForClassname("C");
        assertTrue(relA.isEmpty());
        assertTrue(relB.isEmpty());
    }

    @Test
    public void testRelationshipHandlerConstruction() {
        RelationshipHandler relHandler = new RelationshipHandler();
        assertNotNull(relHandler);
    }

    @Test
    public void testReplacingAllRelationshipsForClassName() {
        List<Relationship> relations = new ArrayList<>();
        UMLClass classA = new UMLClass("A");
        UMLClass classB = new UMLClass("B");
        Relationship relA = new Relationship(classA, classA , RelationshipType.Aggregation);
        Relationship relB = new Relationship(classA, classB, RelationshipType.Composition);
        relations.add(relA);
        relations.add(relB);
        RelationshipHandler.addRelationship("A", "A", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("C", "D", RelationshipType.Composition);
        RelationshipHandler.replaceAllRelationshipsForClassname("A", relations);
        assertEquals(RelationshipHandler.listRelationships(), "C ----◆ D (Composition)\nA ----◇ A (Aggregation)\nA ----◆ B (Composition)");
    }

    @Test
    public void testIndexOfNullObjects() {
        //We are testing the indexOf method for null objects by calling other classes
        try {
            RelationshipHandler.removeRelationship("A", "x");
            
            // shouldn't run
            assertTrue("Removing a relationship between any classes that don't exist should've thrown an exception.", false);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Class x does not exist");
        }
    }
}

    
