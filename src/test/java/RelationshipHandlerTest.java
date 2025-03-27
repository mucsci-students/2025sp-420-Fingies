import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.fingies.Relationship;
import org.fingies.RelationshipHandler;
import org.fingies.RelationshipType;
import org.fingies.UMLClassHandler;
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
            assertEquals("This relationship already exists.", e.getMessage());
        }
    }

    
}

    
