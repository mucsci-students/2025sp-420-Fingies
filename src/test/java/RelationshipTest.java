import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.fingies.Relationship;
import org.fingies.RelationshipType;
import org.fingies.UMLClassHandler;
import org.junit.Before;
import org.junit.Test;

/**
 * A bunch of test cases to make sure the Relationship class works.
 * 
 * @author Lincoln Craddock & Nick Hayes
 */
public class RelationshipTest {

    Relationship r;
    Relationship r2;

    @Before
    public void setUp()
    {
        UMLClassHandler.reset();
        UMLClassHandler.createClass("Class1");
        UMLClassHandler.createClass("Class2");
        UMLClassHandler.createClass("Jeremy");
        UMLClassHandler.createClass("Jane");
        r = new Relationship(UMLClassHandler.getClass("Class1"),UMLClassHandler.getClass("Class2"), RelationshipType.Composition);
        r2 = new Relationship(UMLClassHandler.getClass("Jeremy"), UMLClassHandler.getClass("Jane"), RelationshipType.Inheritance);
    }
    
    @Test
    public void RelationshipHasCorrectSrc()
    {
        assertEquals("The name of the src should be \"Class1\"", "Class1", r.getSrc().getName());
    }
    
    @Test
    public void RelationshipHasCorrectSrc2()
    {
        assertEquals("The name of the src should be \"Jeremy\"", "Jeremy", r2.getSrc().getName());
    }
    
    @Test
    public void RelationshipHasCorrectDest()
    {
        assertEquals("The name of the dest should be \"Class2\"", "Class2", r.getDest().getName());
    }
    
    @Test
    public void RelationshipHasCorrectDest2()
    {
        assertEquals("The name of the dest should be \"Jane\"", "Jane", r2.getDest().getName());
    }
    
    @Test
    public void RelationshipsAreEqual()
    {
        Relationship rCopy = new Relationship(UMLClassHandler.getClass("Class1"),UMLClassHandler.getClass("Class2"), RelationshipType.Composition);
        assertTrue("Two relationships between Class1 and Class2 should be equal.", r.equals(rCopy));
    }
    
    @Test
    public void RelationshipsAreEqual2()
    {
        Relationship rCopy = new Relationship(UMLClassHandler.getClass("Class1"),UMLClassHandler.getClass("Class2"), RelationshipType.Composition);
        assertTrue("Two relationships between Class1 and Class2 should be equal.", rCopy.equals(r));
    }
    
    @Test
    public void RelationshipsAreNotEqual()
    {
        assertFalse(r + " should not equal " + r2, r.equals(r2));
    }
    
    @Test
    public void RelationshipsAreNotEqual2()
    {
        assertFalse(r2 + " should not equal " + r, r2.equals(r));
    }
    
    @Test
    public void RelationshipToStringWorks()
    {
        assertEquals("The string representation should be \"Class1 " + RelationshipType.Composition + " Class2\"", "Class1 " + RelationshipType.Composition + " Class2", r.toString());
    }
    
    @Test
    public void RelationshipToStringWorks2()
    {
        assertEquals("The string representation should be \"Jeremy " + RelationshipType.Inheritance + " Jane\"", "Jeremy " + RelationshipType.Inheritance + " Jane", r2.toString());
    }
    
    @Test
    public void RelationshipTypeWorks()
    {
    	assertEquals(r + " should be the type " + RelationshipType.Composition, RelationshipType.Composition, r.getType());
    }
    
    @Test
    public void RelationshipTypeWorks2()
    {
    	r.setType(RelationshipType.Aggregation);
    	assertEquals(r + " should be the type " + RelationshipType.Aggregation, RelationshipType.Aggregation, r.getType());
    	r.setType(RelationshipType.Composition);
    	assertEquals(r + " should be the type " + RelationshipType.Composition, RelationshipType.Composition, r.getType());
    }  

    // --------------------- RELATIONSHIP TYPE ---------------------

    @Test
    public void createNullRelationshipType()
    {
        RelationshipType r = RelationshipType.fromString("b");
        assertNull(r);
    }

    @Test
    public void relatonshipTypeToString()
    {
        RelationshipType r = RelationshipType.fromString("a");
        assertEquals("----◇", r.toString());
        r = RelationshipType.fromString("c");
        assertEquals("----◆", r.toString());
        r = RelationshipType.fromString("i");
        assertEquals("----▷", r.toString());
        r = RelationshipType.fromString("r");
        assertEquals("- - ▷", r.toString());
    }
}