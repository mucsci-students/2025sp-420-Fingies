import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * A bunch of test cases to make sure the Relationship class works.
 * 
 * @author Lincoln Craddock
 */
public class RelationshipTester {

    Relationship r;
    Relationship r2;

    @Before
    public void setUp()
    {
        UMLClassHandler.createClass("Class1");
        UMLClassHandler.createClass("Class2");
        UMLClassHandler.createClass("Jeremy");
        UMLClassHandler.createClass("Jane");
        r = new Relationship("Class1", "Class2");
        r2 = new Relationship("Jeremy", "Jane");
    }
    
    @Test
    public void RelationshipHasCorrectSrc()
    {
        assertEquals("The name of the src should be \"Class1\"", "Class1", r.getSrc());
    }
    
    @Test
    public void RelationshipHasCorrectSrc2()
    {
        assertEquals("The name of the src should be \"Jeremy\"", "Jeremy", r2.getSrc());
    }
    
    @Test
    public void RelationshipHasCorrectDest()
    {
        assertEquals("The name of the dest should be \"Class2\"", "Class2", r.getDest());
    }
    
    @Test
    public void RelationshipHasCorrectDest2()
    {
        assertEquals("The name of the dest should be \"Jane\"", "Jane", r2.getDest());
    }
    
    @Test
    public void RelationshipsAreEqual()
    {
        Relationship rCopy = new Relationship("Class1", "Class2");
        assertTrue("Two relationships between Class1 and Class2 should be equal.", r.equals(rCopy));
    }
    
    @Test
    public void RelationshipsAreEqual2()
    {
        Relationship rCopy = new Relationship("Class1", "Class2");
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
        assertEquals("The string representation should be \"Class1 --> Class2\"", "Class1 --> Class2", r.toString());
    }
    
    @Test
    public void RelationshipToStringWorks2()
    {
        assertEquals("The string representation should be \"Jeremy --> Jane\"", "Jeremy --> Jane", r2.toString());
    }
   
}
