package ModelTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;

import org.fingies.Model.Method;
import org.fingies.Model.Parameter;
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

    // --------------------- Getters and Setters ---------------------

    @Test
    public void getMethodName()
    {
        assertEquals(m.getName(), "Method1");
    }

    @Test
    public void setMethodName()
    {
        m.renameAttribute("Method0");
        assertEquals(m.getName(), "Method0");
    }

    @Test
    public void getMethodReturnType()
    {
        assertEquals(m.getReturnType(), "void");
    }

    @Test
    public void setmethodReturnType()
    {
        m.setReturnType("int");
        assertEquals(m.getReturnType(), "int");
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
    public void addTwoParametersToMethod_ThenMethodTypeListShouldContainBothTypes()
    {
        m.addParameter("Param1",  "String");
        m.addParameter("Param2",  "int");
        List<String> paramTypes = m.getParameterTypes();
        assertTrue(paramTypes.get(0).equals("String") && paramTypes.get(1).equals("int"));
    }

    @Test
    public void addTwoParametersToMethod_ThenMethodParameterListShouldContainBothParameters()
    {
        Parameter param1 = new Parameter("Param1",  "String");
        Parameter param2 = new Parameter("Param2",  "int");
        m.addParameter(param1.getName(), param1.getType());
        m.addParameter(param2.getName(), param2.getType());
        List<Parameter> params = m.getParameters();
        assertTrue(params.get(0).equals(param1) && params.get(1).equals(param2));
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

    @Test
    public void addMultipleParametersWithLists_ThenParametersShouldBeAdded()
    {
        List<String> parameters = new ArrayList<>(Arrays.asList("Name", "Age", "Sext"));
        List<String> types = new ArrayList<>(Arrays.asList("String", "int", "String"));
        m.addParameters(parameters, types);

        assertTrue(m.getParameterNames().equals(parameters));
        assertTrue(m.getParameterNames().equals(parameters));
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
    public void addTwoParametersAndRenameOneParameterToOneThatDNE_ThenParameterShouldFailToBeRenamed()
    {
        try
        {
            m.addParameter("Param1", "String");
            m.renameParameter("Param2", "Param3");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Method Method1 doesn't have a parameter called Param2", e.getMessage());
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
    public void removeListOfParameters_ThenAllParametersShouldBeRemoved()
    {
        List<String> paramNames = new ArrayList<String>(Arrays.asList("Param1", "Param2", "Param3"));
        m.addParameter("Param1",  "String");
        m.addParameter("Param2",  "int");
        m.addParameter("Param3",  "boolean");

        assertTrue(m.parameterExists("Param1"));
        assertTrue(m.parameterExists("Param2"));
        assertTrue(m.parameterExists("Param3"));

        m.removeParameters(paramNames);
        
        assertTrue(m.getParameters().isEmpty());
    }

    @Test
    public void removeListOfParametersWithNonExistantParameters_ThenIllegalArgumentExceptionThrown()
    {
        List<String> paramNames = new ArrayList<String>(Arrays.asList("Param1", "Param2", "Param4"));
        m.addParameter("Param1",  "String");
        m.addParameter("Param2",  "int");
        m.addParameter("Param3",  "boolean");

        assertTrue(m.parameterExists("Param1"));
        assertTrue(m.parameterExists("Param2"));
        assertTrue(m.parameterExists("Param3"));
        try
        {
            m.removeParameters(paramNames);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(e.getMessage(), "Method Method1 doesn't have a parameter named Param4");
        }
    }

    @Test
    public void removeParameterThatDNE_ThrowsIllegalArgumentException()
    {
        assertFalse(m.removeParameter("Param1"));
    }
    
    // --------------------- CLEAR PARAMETERS ---------------------

    @Test
    public void addOneParameterToMethodAndClear_ThenMethodShouldContainParameter()
    {
        m.addParameter("Param1",  "String");
        assertTrue(m.parameterExists("Param1"));
        m.clearParameters();
        assertFalse(m.parameterExists("Param1"));
    }

    @Test
    public void clearListOfParameters_ThenAllParametersShouldBeRemoved()
    {
        m.addParameter("Param1",  "String");
        m.addParameter("Param2",  "int");
        m.addParameter("Param3",  "boolean");

        assertTrue(m.parameterExists("Param1"));
        assertTrue(m.parameterExists("Param2"));
        assertTrue(m.parameterExists("Param3"));

        m.clearParameters();
        
        assertTrue(m.getParameters().isEmpty());
    }

    @Test
    public void clearWithNonExistantParameters_ThenIllegalArgumentExceptionThrown()
    {
        try
        {
            m.clearParameters();
        }
        catch (IllegalArgumentException e)
        {
            assertEquals(e.getMessage(), "Method " + m.getName() + " doesn't have any parameters.");
        }
    }

    // --------------------- CHANGE PARAMETER TYPES ---------------------

    @Test
    public void changeOneParameterType_ThenParameterTypeIsChanged()
    {
        m.addParameter("Name", "String");
        assertEquals(m.getParameter("Name").getType(), "String");

        m.getParameter("Name").setType("char");
        assertEquals(m.getParameter("Name").getType(), "char");
    }

    @Test
    public void changeMultipleParameterTypes_ThenParameterTypesAreChanged()
    {
        List<String> parameters = new ArrayList<>(Arrays.asList("Name", "Age", "Sex"));
        List<String> types = new ArrayList<>(Arrays.asList("String", "int", "String"));
        m.addParameters(parameters, types);

        assertTrue(m.getParameterNames().equals(parameters));
        assertTrue(m.getParameterNames().equals(parameters));

        m.getParameter("Name").setType("char");
        m.getParameter("Age").setType("double");
        m.getParameter("Sex").setType("char");

        assertEquals(m.getParameter("Name").getType(), "char");
        assertEquals(m.getParameter("Age").getType(), "double");
        assertEquals(m.getParameter("Sex").getType(), "char");
    }

    // --------------------- ADDITIONAL TESTS ---------------------

    @Test
    public void checkMethodEqualsTrue()
    {
        Method m2 = new Method ("Method1", "void");
        Parameter param1 = new Parameter("Param1",  "String");
        assertTrue(m.equals(m2));

        m.addParameter(param1.getName(), param1.getType());
        m2.addParameter(param1.getName(), param1.getType());
        assertTrue(m.equals(m2));
    }

    @Test
    public void checkMethodEqualsFalse()
    {
        Method m2 = new Method ("Method2", "void");
        Parameter param1 = new Parameter("Param1",  "String");
        assertFalse(m.hashCode() == m2.hashCode());

        m.addParameter(param1.getName(), param1.getType());
        m2.addParameter(param1.getName(), param1.getType());
        assertFalse(m.equals(m2));
    }

    @Test
    public void checkHashCodeEqualsTrue()
    {
        Method m2 = new Method ("Method1", "void");
        Parameter param1 = new Parameter("Param1",  "String");
        assertTrue(m.hashCode() == m2.hashCode());

        m.addParameter(param1.getName(), param1.getType());
        m2.addParameter(param1.getName(), param1.getType());
        assertTrue(m.hashCode() == m2.hashCode());
    }

    @Test
    public void checkHashCodeEqualsFalse()
    {
        Method m2 = new Method ("Method2", "void");
        Parameter param1 = new Parameter("Param1",  "String");
        assertFalse(m.hashCode() == m2.hashCode());

        m.addParameter(param1.getName(), param1.getType());
        m2.addParameter(param1.getName(), param1.getType());
        assertFalse(m.hashCode() == m2.hashCode());
    }

    @Test
    public void checkToTypes()
    {
        assertEquals(m.toTypes(), "Method1 ()");
        m.addParameter("Param1", "String");
        assertEquals(m.toTypes(), "Method1 (String)");
        m.addParameter("Param2", "int");
        assertEquals(m.toTypes(), "Method1 (String, int)");
    }

    @Test
    public void checkToString()
    {
        assertEquals(m.toString(), "void Method1 ()");
        m.addParameter("Param1", "String");
        assertEquals(m.toString(), "void Method1 (String Param1)");
        m.addParameter("Param2", "int");
        assertEquals(m.toString(), "void Method1 (String Param1, int Param2)");
    }

    @Test
    public void testMethodNamesNotEqualTypes() {
        List<String> pNames = new ArrayList<>();
        List<String> pTypes = new ArrayList<>();
        pNames.add("test1");
        pNames.add("test2");
        pTypes.add("void");
        try {
            new Method("Test", "Void", pNames, pTypes);
            //Should have thrown an exception, so now we say it fails because it didn't.
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Every parameter must have one type");
        }
    }

    @Test
    public void testMethodAddNamesNotEqualTypes() {
        List<String> pNames = new ArrayList<>();
        List<String> pTypes = new ArrayList<>();
        pNames.add("test1");
        pNames.add("test2");
        pTypes.add("void");
        try {
            m.addParameters(pNames, pTypes);
            //Should have thrown an exception, so now we say it fails because it didn't.
            assertTrue(false);
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Every parameter must have one type");
        }
    }

    @Test
    public void testNotEqualsWrongObject() {
        assertFalse(m.equals(null));
    }

    @Test
    public void testNotEqualsDifferentObject() {
        Method z = new Method("Womp", "Different");
        assertFalse(m.equals(z));
    }
}
