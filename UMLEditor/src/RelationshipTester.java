import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.TreeSet;

import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

/**
 * A bunch of tests cases to make sure the relationhip class works.
 * 
 * @author Lincoln Craddock
 */
public class RelationshipTester {

    Relationship r;
    Relationship r2;

    @Before
    public void setUp()
    {
        r = new Relationship ("Class1", "Class2");
        r2 = new Relationship ("Jeremy", "Jane");
        
    }
    
    @Test
    public void RelationshipHasCorrectSrc ()
    {
    	assertEquals (r.getSrc(), "Class1", "The name of the src should be \"Class1\"");
    }
    
    @Test
    public void RelationshipHasCorrectSrc2 ()
    {
    	assertEquals (r2.getSrc(), "Jeremy", "The name of the src should be \"Jeremy\"");
    }
    
    @Test
    public void RelationshipHasCorrectDest ()
    {
    	assertEquals (r.getDest(), "Class1", "The name of the src should be \"Class1\"");
    }
    
    @Test
    public void RelationshipHasCorrectDest2 ()
    {
    	assertEquals (r2.getDest(), "Jeremy", "The name of the src should be \"Jeremy\"");
    }
    
    @Test
    public void RelationshipsAreEqual ()
    {
    	Relationship rCopy = new Relationship ("Class1", "Class2");
    	assertTrue ("Two relationships between Class1 and Class2 should be equal.", r.equals(rCopy));
    }
    
    @Test
    public void RelationshipsAreEqual2 ()
    {
    	Relationship rCopy = new Relationship ("Class1", "Class2");
    	assertTrue ("Two relationships between Class1 and Class2 should be equal.", rCopy.equals(r));
    }
    
    @Test
    public void RelationshipsAreNotEqual2 ()
    {
    	assertFalse (r2 + " should not equals " + r, r2.equals(r));
    }
    
    @Test
    public void RelationshipToStringWorks ()
    {
    	assertEquals (r.toString(), "Class1 --> Class2");
    }
    
    @Test
    public void RelationshipToStringWorks2 ()
    {
    	assertEquals (r.toString(), "Jeremy --> Jane");
    }


}
