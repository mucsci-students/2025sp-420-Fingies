import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.fingies.Relationship;
import org.fingies.RelationshipHandler;
import org.fingies.RelationshipType;
import org.fingies.UMLClass;
import org.fingies.UMLClassHandler;
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
        try
        {
            RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
            RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
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
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("This relationship does not exist", e.getMessage());
        }
    }

    @Test
    public void relationshipHandlerListRelationships()
    {
        assertEquals(RelationshipHandler.listRelationships(), "There are no current relationships");
        RelationshipHandler.addRelationship("A", "B", RelationshipType.Aggregation);
        assertEquals(RelationshipHandler.listRelationships(), "A ----◇ B");
        RelationshipHandler.addRelationship("C", "D", RelationshipType.Composition);
        assertEquals(RelationshipHandler.listRelationships(), "A ----◇ B\nC ----◆ D");
    }

    @Test
    public void testReplaceSrcAndDest() {
        RelationshipHandler.addRelationship("A", "A", RelationshipType.Aggregation);
        UMLClass classA = new UMLClass("A");
        UMLClass classB = new UMLClass("B");
        RelationshipHandler.replace(classA, classB);
        assertEquals(RelationshipHandler.listRelationships(), "B ----◇ B");
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
        UMLClass classC = new UMLClass("C");
        UMLClass classD = new UMLClass("D");
        Relationship relA = new Relationship(classA, classA , RelationshipType.Aggregation);
        Relationship relB = new Relationship(classA, classB, RelationshipType.Composition);
        Relationship relC = new Relationship(classC, classD, RelationshipType.Inheritance);
        relations.add(relA);
        relations.add(relB);
        relations.add(relC);
        RelationshipHandler.replaceAllRelationshipsForClassname("A", relations);
        assertEquals(RelationshipHandler.listRelationships(), "A ----◇ A\nA ----◆ B\nC ----▷ D");
    }
}

    
