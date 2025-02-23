import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the UMLClass class
 * @author kdichter
 */
public class MethodTester {

    Method m;

    @Before
    public void setUp()
    {
        m = new Method ("Method1");
    }
    
    // --------------------- ADD PARAMETERS ---------------------

    @Test
    public void addOneParameterWithIllegalCharacters_ThrowsIllegalArgumentException()
    {
        try{
            m.addParameter("Param1%");
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
        }
    }

    @Test
    public void addOnearameterLongerThan50Characters_ThrowsIllegalArgumentException()
    {
        try{
            m.addParameter("Parameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeter1");
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }

    @Test
    public void addOneParameterToMethod_ThenMethodShouldContainParameter()
    {
        m.addParameter("Param1");
        assertTrue(m.parameterExists("Param1"));
    }

    @Test
    public void addTwoParametersToMethod_ThenMethodShouldContainBothParameters()
    {
        m.addParameter("Param1");
        m.addParameter("Param2");
        assertTrue(m.parameterExists("Param1") && m.parameterExists("Param2"));
    }

    @Test
    public void addDuplicateParameters_ThenMethodShouldNotAddDuplicateParameter()
    {
        m.addParameter("Param1");
        m.addParameter("Param1");
        assertEquals(m.getParameters().size(), 1);
    }

    // --------------------- RENAME PARAMETERS ---------------------

    @Test
    public void addParamterThenRenameIt_ThenParameterdShouldBeRenamed()
    {
        m.addParameter("Param1");
        assertTrue(m.parameterExists("Param1"));

        m.renameParameter("Param1", "Param2");
        assertTrue(m.parameterExists("Param2"));
    }

    @Test
    public void addTwoParametersndRenameOneParameteToOneThatExists_ThenParameteShouldFailToBeRenamed()
    {
        m.addParameter("Param1");
        m.addParameter("Param2");
        assertFalse(m.renameParameter("Param1", "Param2"));
    }

    @Test
    public void renameParameterToSameName_ThenParameterShouldFailToBeRenamed()
    {
        m.addParameter("Param1");
        assertFalse(m.renameParameter("Param1", "Param1"));
    }

    // --------------------- REMOVE PARAMETERS ---------------------

    @Test
    public void addOneParameterToMethodAndRemoveIt_ThenMethodShouldContainParameter()
    {
        m.addParameter("Param1");
        assertTrue(m.parameterExists("Param1"));
        m.removeParameter("Param1");
        assertFalse(m.parameterExists("Param1"));
    }

    @Test
    public void removeParameterThatDNE_ThrowsIllegalArgumentException()
    {
        assertFalse(m.removeParameter("Param1"));
    }
}
