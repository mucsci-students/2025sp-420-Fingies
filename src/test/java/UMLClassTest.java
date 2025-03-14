import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashMap;

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
            c.addField("Engine%", "String");
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
            c.addField("Engineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", "String");
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }
    
    @Test
    public void addOneField_ThenClassShouldContainField()
    {
        c.addField("Engine", "String");
        assertTrue(c.fieldExists("Engine"));
    }

    @Test
    public void addFieldWithSameNameAsClass_ThenClassShouldFailToAddField()
    {
        try
        {
            c.addField("UMLClass", "String");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("A field must have a different name than its class", e.getMessage());
        }
    }

    @Test
    public void addFieldThatAlreadyExists_ThenClassShouldFailToAddField()
    {
        
        try
        {
            c.addField("Engine", "String");
            assertTrue(c.fieldExists("Engine"));
            c.addField("Engine", "String");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("UMLClass already has a field called Engine", e.getMessage());
        }
    }

    @Test
    public void addFieldWithNonAlphaNumericCharacters_ThrowsIllegalArgumentException()
    {
        try
        {
            c.addField("Engine%", "String");
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
            DNE.addField("Engine", "String");
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
        try {
            HashMap<String, String> parameters = new HashMap<>();
            c.addMethod("Engine%", "void", parameters);
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
        }
    }

    @Test
    public void addOneMethodLongerThan50Characters_ThrowsIllegalArgumentException()
    {
        try {
            HashMap<String, String> parameters = new HashMap<>();
            c.addMethod("Engineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", "void", parameters);
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }

    @Test
    public void addOneMethod_ThenClassShouldContainMethod()
    {
        HashMap<String, String> parameters = new HashMap<>();
        c.addMethod("getEngine", "void", parameters);
        assertTrue(c.methodExists("getEngine", "void", 0));
    }

    @Test
    public void addMethodWithSameNameAsClassWithSameArity_ThenClassShouldFailToAddMethod()
    {
        try
        {
            HashMap<String, String> parameters = new HashMap<>();
            c.addMethod("getEngine", "void", parameters);
            assertTrue(c.methodExists("getEngine", "void", 0));
            c.addMethod("getEngine", "void", parameters);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("A method with that name and arity already exists", e.getMessage());
        }
        
        
    }

    @Test
    public void addMethodWithSameNameAsClassWithDifferenteArity_ThenClassAddMethod()
    {
        HashMap<String, String> parameters = new HashMap<>();
        c.addMethod("getEngine", "void", parameters);
        assertTrue(c.methodExists("getEngine", "void", 0));
        parameters.put("Param1", "String");
        assertTrue(c.addMethod("getEngine", "void", parameters));
        assertTrue(c.methodExists("getEngine", "void", 1));
        assertTrue(c.getMethod("getEngine", "void", 1).getParameters().keySet().contains("Param1"));
    }

    // --------------------- RENAME FIELDS ---------------------

    @Test
    public void addFieldThenRenameIt_ThenFieldShouldBeRenamed()
    {
        c.addField("Engine", "String");
        assertTrue(c.fieldExists("Engine"));

        c.renameField("Engine", "String", "Wheel");
        assertTrue(c.fieldExists("Wheel"));
    }

    @Test
    public void addTwoFieldsAndRenameOneFieldToOneThatExists_ThenFieldShouldFailToBeRenamed()
    {
        try
        {
            c.addField("Engine", "String");
            c.addField("Wheel", "String");
            c.renameField("Engine", "String", "Wheel");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Class UMLClass already has a field named Wheel", e.getMessage());
        }
    }

    @Test
    public void renameFieldToSameName_ThenFieldShouldFailToBeRenamed()
    {
        try
        {
            c.addField("Engine", "String");
            c.renameField("Engine", "String", "Engine");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Bro seriously? Why would you rename a field to be the same name bro.", e.getMessage());
        }
    }

    // --------------------- RENAME METHODS ---------------------

    @Test
    public void addMethodThenRenameIt_ThenMethodShouldBeRenamed()
    {
        HashMap<String, String> parameters = new HashMap<>();
        c.addMethod("getEngine", "void", parameters);
        assertTrue(c.methodExists("getEngine", "void", 0));

        c.renameMethod("getEngine", "void", 0, "setEngine");
        assertTrue(c.methodExists("setEngine", "void", 0));
    }

    @Test
    public void addTwoMethodsAndRenameOneMethodToOneThatExistsWithSameArity_ThenMethodShouldFailToBeRenamed()
    {
        
        try
        {
            HashMap<String, String> parameters = new HashMap<>();
            c.addMethod("getEngine", "void", parameters);
            c.addMethod("setEngine", "void", parameters);
            c.renameMethod("setEngine", "void", 0, "getEngine");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Class UMLClass already has a method called getEngine with 0 parameters", e.getMessage());
        }
    }

    @Test
    public void addTwoMethodsAndRenameOneMethodToOneThatExistsWithDifferentArity_ThenMethodShouldBeRenamed()
    {
        HashMap<String, String> parameters = new HashMap<>();
        c.addMethod("getEngine", "void", parameters);
        parameters.put("param1", "String");
        c.addMethod("setEngine", "void", parameters);
        assertTrue(c.renameMethod("setEngine", "void", 1, "getEngine"));
    }

    @Test
    public void renameMethodToSameNameWithSameArity_ThenMethodShouldFailToBeRenamed()
    {
        try
        {
            HashMap<String, String> parameters = new HashMap<>();
            c.addMethod("Engine", "void", parameters);
            c.renameMethod("Engine", "void", 0, "Engine");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Class UMLClass already has a method called Engine with 0 parameters", e.getMessage());
        }
    }
    // --------------------- DELETE FIELDS ---------------------

    @Test
    public void addFieldAndRemoveIt_ThenFieldShouldBeRemoved()
    {
        c.addField("Engine", "String");
        assertTrue(c.fieldExists("Engine"));

        c.removeField("Engine");
        assertFalse(c.fieldExists("Engine"));
    }

    @Test
    public void removeFieldThatDNE_ThrowsIllegalArgumentException()
    {
        try
        {
            c.removeField("Engine");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Class UMLClass doesn't have a field named Engine", e.getMessage());
        }
    }

    // --------------------- DELETE METHODS ---------------------

    @Test
    public void addMethodAndRemoveIt_ThenMethodShouldBeRemoved()
    {
        HashMap<String, String> parameters = new HashMap<>();
        c.addMethod("Engine", "void", parameters);
        assertTrue(c.methodExists("Engine", "void", 0));

        c.removeMethod("Engine", "void", 0);
        assertFalse(c.methodExists("Engine", "void", 0));
    }

    @Test
    public void removeMethodThatDNE_ThrowsIllegalArgumentException()
    {
        try
        {
            c.removeMethod("Engine", "void", 0);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Class UMLClass doesn't have a method named Engine with the arity 0", e.getMessage());
        }
    }
}
