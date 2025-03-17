import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static org.junit.Assert.assertFalse;

import org.fingies.Method;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the UMLClass class
 * @author kdichter
 */
public class MethodTest {

    Method m;

    @Before
    public void setUp()
    {
        m = new Method ("Method1",  "void");
    }
    
    // --------------------- ADD PARAMETERS ---------------------

    @Test
    public void addOneParameterWithIllegalCharacters_ThrowsIllegalArgumentException()
    {
        try{
            m.addParameter("Param1%",  "String");
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
        }
    }

    @Test
    public void addOneParameterLongerThan50Characters_ThrowsIllegalArgumentException()
    {
        try{
            m.addParameter("Parameeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeter1",  "String");
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }

    @Test
    public void addOneParameterToMethod_ThenMethodShouldContainParameter()
    {
        m.addParameter("Param1",  "String");
        assertTrue(m.parameterExists("Param1"));
    }

    @Test
    public void addTwoParametersToMethod_ThenMethodShouldContainBothParameters()
    {
        m.addParameter("Param1",  "String");
        m.addParameter("Param2",  "String");
        assertTrue(m.parameterExists("Param1") && m.parameterExists("Param2"));
    }

    @Test
    public void addDuplicateParameters_ThenMethodShouldNotAddDuplicateParameter()
    {
        
        try
        {
            m.addParameter("Param1",  "String");
            m.addParameter("Param1",  "String");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Method Method1 already has a parameter named Param1", e.getMessage());
        }
    }

    // --------------------- RENAME PARAMETERS ---------------------

    @Test
    public void addParameterThenRenameIt_ThenParameterdShouldBeRenamed()
    {
        m.addParameter("Param1",  "String");
        assertTrue(m.parameterExists("Param1"));

        m.renameParameter("Param1", "Param2");
        assertTrue(m.parameterExists("Param2"));
    }

    @Test
    public void addTwoParametersAndRenameOneParameterToOneThatExists_ThenParameterShouldFailToBeRenamed()
    {
        try
        {
            m.addParameter("Param1",  "String");
            m.addParameter("Param2",  "String");
            m.renameParameter("Param1", "Param2");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Method Method1 already has a parameter called Param2", e.getMessage());
        }
    }

    @Test
    public void renameParameterToSameName_ThenParameterShouldFailToBeRenamed()
    {
        try
        {
            m.addParameter("Param1", "String");
            m.renameParameter("Param1", "Param1");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Method Method1 already has a parameter called Param1", e.getMessage());
        }
    }

    // --------------------- REMOVE PARAMETERS ---------------------

    @Test
    public void addOneParameterToMethodAndRemoveIt_ThenMethodShouldContainParameter()
    {
        m.addParameter("Param1",  "String");
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
