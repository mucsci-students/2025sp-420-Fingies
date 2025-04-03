import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.fingies.RelationshipHandler;
import org.fingies.RelationshipType;
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


}

    
