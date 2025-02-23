import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the UMLClass class
 * @author kdichter
 */
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

    // --------------------- ADD FIELDS ---------------------

    @Test
    public void addOneField_ThenClassShouldContainField()
    {
        c.addField("Engine");
        assertTrue(c.fieldExists("Engine"));
    }
    @Test
    public void addFieldWithSameNameAsClass_ThenClassShouldFailToAddField()
    {
        assertFalse(c.addField("UMLClass"));
    }
    @Test
    public void addFieldThatAlreadyExists_ThenClassShouldFailToAddField()
    {
        c.addField("Engine");
        assertTrue(c.fieldExists("Engine"));
        assertFalse(c.addField("Engine"));
    }

    @Test
    public void addFieldWithNonAlphaNumericCharacters_ThrowsIllegalArgumentException()
    {
        try
        {
            c.addField("Engine%");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
        }
    }

    @SuppressWarnings("null")
    @Test
    public void addFieldToClassThatDNA_ThrowsNullPointerException()
    {
        // what is this testing??
        UMLClass DNE = null;
        try
        {
            DNE.addField("Engine");
        }
        catch (NullPointerException e)
        {
            System.out.println("Null Pointer");
        }
    }

    // --------------------- RENAME FIELDS ---------------------

    @Test
    public void addFieldThenRenameIt_ThenFieldShouldBeRenamed()
    {
        c.addField("Engine");
        assertTrue(c.fieldExists("Engine"));

        c.renameField("Engine", "Wheel");
        System.out.println("HELLO");
        assertTrue(c.fieldExists("Wheel"));
    }
    @Test
    public void addTwoFieldsAndRenameOneFieldToOneThatExists_ThenFieldShouldFailToBeRenamed()
    {
        c.addField("Engine");
        c.addField("Wheel");
        assertFalse(c.renameField("Engine", "Wheel"));
    }

    @Test
    public void renameFieldToSameName_ThenFieldShouldFailToBeRenamed()
    {
        c.addField("Engine");
        assertFalse(c.renameField("Engine", "Engine"));
    }

    // --------------------- DELETE FIELDS ---------------------

    @Test
    public void addFieldAndRemoveIt_ThenFieldShouldBeRemoved()
    {
        c.addField("Engine");
        assertTrue(c.fieldExists("Engine"));

        c.removeField("Engine");
        HashSet<Field> set = c.getFields();
        assertTrue(set.isEmpty());
    }

    @Test
    public void removeFieldThatDNE_ThrowsIllegalArgumentException()
    {
        try
        {
            c.removeField("Engine");
        }
        catch(IllegalArgumentException e)
        {
            assertEquals("Attribute provided does not exist", e.getMessage());
        }
        
    }
}
