import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.fingies.UMLClass;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the UMLClass class
 * @author kdichter
 */
public class UMLClassTest {

    UMLClass c;

    @Before
    public void setUp()
    {
        c = new UMLClass ("UMLClass");
    }

    @Test
    public void renameUMLClassToCar_ThenClassShouldBeRenamed()
    {
        c.renameClass ("Car");
        assertEquals("Car", c.getName());
    }

    // --------------------- ADD FIELDS ---------------------

    @Test
    public void addOneFieldWithIllegalCharacters_ThrowsIllegalArgumentException()
    {
        try{
            c.addField("Engine%");
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
        }
    }

    @Test
    public void addOneFieldLongerThan50Characters_ThrowsIllegalArgumentException()
    {
        try{
            c.addField("Engineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }
    
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

    // --------------------- ADD METHODS ---------------------

    @Test
    public void addOneMethodWithIllegalCharacters_ThrowsIllegalArgumentException()
    {
        try{
            ArrayList<String> parameters = new ArrayList<String>();
            c.addMethod("Engine%", parameters);
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
        }
    }

    @Test
    public void addOneMethodLongerThan50Characters_ThrowsIllegalArgumentException()
    {
        try{
            ArrayList<String> parameters = new ArrayList<String>();
            c.addMethod("Engineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", parameters);
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }

    @Test
    public void addOneMethod_ThenClassShouldContainMethod()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("getEngine", parameters);
        assertTrue(c.methodExists("getEngine", 0));
    }

    @Test
    public void addMethodWithSameNameAsClassWithSameArity_ThenClassShouldFailToAddMethod()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("getEngine", parameters);
        assertTrue(c.methodExists("getEngine", 0));
        assertFalse(c.addMethod("getEngine", parameters));
    }

    @Test
    public void addMethodWithSameNameAsClassWithDifferenteArity_ThenClassAddMethod()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("getEngine", parameters);
        assertTrue(c.methodExists("getEngine", 0));
        parameters.add("Param1");
        assertTrue(c.addMethod("getEngine", parameters));
        assertTrue(c.methodExists("getEngine", 1));
        assertTrue(c.getMethod("getEngine", 1).getParameters().contains("Param1"));
    }

    // --------------------- RENAME FIELDS ---------------------

    @Test
    public void addFieldThenRenameIt_ThenFieldShouldBeRenamed()
    {
        c.addField("Engine");
        assertTrue(c.fieldExists("Engine"));

        c.renameField("Engine", "Wheel");
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

    // --------------------- RENAME METHODS ---------------------

    @Test
    public void addMethodThenRenameIt_ThenMethodShouldBeRenamed()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("getEngine", parameters);
        assertTrue(c.methodExists("getEngine", 0));

        c.renameMethod("getEngine", 0, "setEngine");
        assertTrue(c.methodExists("setEngine", 0));
    }

    @Test
    public void addTwoMethodsAndRenameOneMethodToOneThatExistsWithSameArity_ThenMethodShouldFailToBeRenamed()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("getEngine", parameters);
        c.addMethod("setEngine", parameters);
        assertFalse(c.renameMethod("setEngine", 0, "getEngine"));
    }

    @Test
    public void addTwoMethodsAndRenameOneMethodToOneThatExistsWithDifferentArity_ThenMethodShouldBeRenamed()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("getEngine", parameters);
        parameters.add("param1");
        c.addMethod("setEngine", parameters);
        assertTrue(c.renameMethod("setEngine", 1, "getEngine"));
    }

    @Test
    public void renameMethodToSameNameWithSameArity_ThenMethodShouldFailToBeRenamed()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("Engine", parameters);
        assertFalse(c.renameMethod("Engine", 0, "Engine"));
    }
    // --------------------- DELETE FIELDS ---------------------

    @Test
    public void addFieldAndRemoveIt_ThenFieldShouldBeRemoved()
    {
        c.addField("Engine");
        assertTrue(c.fieldExists("Engine"));

        c.removeField("Engine");
        assertFalse(c.fieldExists("Engine"));
    }

    @Test
    public void removeFieldThatDNE_ThrowsIllegalArgumentException()
    {
        assertFalse(c.removeField("Engine"));
    }

    // --------------------- DELETE METHODS ---------------------

    @Test
    public void addMethodAndRemoveIt_ThenMethodShouldBeRemoved()
    {
        ArrayList<String> parameters = new ArrayList<String>();
        c.addMethod("Engine", parameters);
        assertTrue(c.methodExists("Engine", 0));

        c.removeMethod("Engine", 0);
        assertFalse(c.methodExists("Engine", 0));
    }

    @Test
    public void removeMethodThatDNE_ThrowsIllegalArgumentException()
    {
        assertFalse(c.removeMethod("Engine", 0));
    }
}
