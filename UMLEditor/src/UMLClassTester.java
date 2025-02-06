import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class UMLClassTester {

    UMLClass c;

    @Before
    public void setUp()
    {
        c = new UMLClass ("UMLClass");
    }

    @Test
    public void renameUMLClassToCar()
    {
        c.renameClass ("Car");
        assertEquals("Car", c.getName());
    }

    // --------------------- ADD ATTRIBUTE ---------------------

    @Test
    public void createOneAttributeCalledEngine()
    {
        c.addAttribute("Engine");
        HashSet<String> set = c.getAllAttributes();
        assertTrue(set.contains("Engine"));
    }
    @Test
    public void createAttributeWithSameNameAsClass()
    {
        assertFalse(c.addAttribute("UMLClass"));
    }
    @Test
    public void addEngineAttributeToUMLClassAlreadyContainingEngineAttribute()
    {
        c.addAttribute("Engine");
        HashSet<String> set1 = c.getAllAttributes();
        assertTrue(set1.contains("Engine"));

        assertFalse(c.addAttribute("Engine"));
    }

    @Test
    public void addAttributeWithNonAlphaNumericCharacters()
    {
        try
        {
            c.addAttribute("Engine%");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
        }
    }

    @Test
    public void addEngineAttributeToNonexistantClass()
    {
        UMLClass DNE = null;
        try
        {
            DNE.addAttribute("Engine");
        }
        catch (NullPointerException e)
        {
            System.out.println("Null Pointer");
        }
    }

    // --------------------- RENAME ATTRIBUTE ---------------------

    @Test
    public void createEngineAttributeAndRenameToWheel()
    {
        c.addAttribute("Engine");
        HashSet<String> set1 = c.getAllAttributes();
        assertTrue(set1.contains("Engine"));

        c.renameAttribute("Engine", "Wheel");
        HashSet<String> set2 = c.getAllAttributes();
        assertTrue(set2.contains("Wheel"));
    }
    @Test
    public void addEngineAttributeAndAddWheelAttributeThenRenameEngineToWheel()
    {
        c.addAttribute("Engine");
        c.addAttribute("Wheel");
        assertFalse(c.renameAttribute("Engine", "Wheel"));
    }

    @Test
    public void renameEngineAttributeToEngine()
    {
        c.addAttribute("Engine");
        assertFalse(c.renameAttribute("Engine", "Engine"));
    }

    // --------------------- DELETE ATTRIBUTE ---------------------

    @Test
    public void createEngineAttributeAndRemoveIt()
    {
        c.addAttribute("Engine");
        HashSet<String> set1 = c.getAllAttributes();
        assertTrue(set1.contains("Engine"));

        c.removeAttribute("Engine");
        HashSet<String> set2 = c.getAllAttributes();
        assertTrue(set2.isEmpty());
    }

    @Test
    public void removeEngineAttributeWhenItDoesntExist()
    {
        assertFalse(c.removeAttribute("Engine"));
    }
}
