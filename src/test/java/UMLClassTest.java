import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fingies.UMLClass;
import org.fingies.Field;
import org.fingies.Position;
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
            List<String> parameters = new ArrayList<>();
            List<String> types = new ArrayList<>();
            c.addMethod("Engine%", "void", parameters, types);
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
            List<String> parameters = new ArrayList<>();
            List<String> types = new ArrayList<>();
            c.addMethod("Engineeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee", "void", parameters, types);
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }

    @Test
    public void addOneMethod_ThenClassShouldContainMethod()
    {
        List<String> parameters = new ArrayList<>();
            List<String> types = new ArrayList<>();
        c.addMethod("getEngine", "void", parameters, types);
        assertTrue(c.methodExists("getEngine", types));
    }

    @Test
    public void addMethodWithSameNameAsClassWithSameTypes_ThenClassShouldFailToAddMethod()
    {
        try
        {
            List<String> parameters = new ArrayList<>();
            List<String> types = new ArrayList<>();
            c.addMethod("getEngine", "void", parameters, types);
            assertTrue(c.methodExists("getEngine", types));
            c.addMethod("getEngine", "void", parameters, types);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("A method with that name and types already exists", e.getMessage());
        }
    }

    @Test
    public void addMethodWithSameNameAsClass_ThenClassShouldFailToAddMethod()
    {
        try
        {
            List<String> parameters = new ArrayList<>();
            List<String> types = new ArrayList<>();
            c.addMethod("UMLClass", "void", parameters, types);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("A method must have a different name than its class", e.getMessage());
        }
    }

    @Test
    public void addMethodWithSameNameAsClassWithDifferenteTypes_ThenClassAddMethod()
    {
        List<String> parameters = new ArrayList<>();
        List<String> types = new ArrayList<>();
        c.addMethod("getEngine", "void", parameters, types);
        assertTrue(c.methodExists("getEngine", types));
        parameters.add("Param1");
        types.add("String");
        assertTrue(c.addMethod("getEngine", "void", parameters, types));
        assertTrue(c.methodExists("getEngine", types));
        assertTrue(c.getMethod("getEngine", types).getParameterNames().contains("Param1"));
    }

    // --------------------- RENAME FIELDS ---------------------

    @Test
    public void addFieldThenRenameIt_ThenFieldShouldBeRenamed()
    {
        c.addField("Engine", "String");
        assertTrue(c.fieldExists("Engine"));

        c.renameField("Engine", "Wheel");
        assertTrue(c.fieldExists("Wheel"));
    }

    @Test
    public void addTwoFieldsAndRenameOneFieldToOneThatExists_ThenFieldShouldFailToBeRenamed()
    {
        try
        {
            c.addField("Engine", "String");
            c.addField("Wheel", "String");
            c.renameField("Engine", "Wheel");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Invalid rename operation", e.getMessage());
        }
    }

    @Test
    public void renameFieldToSameName_ThenFieldShouldFailToBeRenamed()
    {
        try
        {
            c.addField("Engine", "String");
            c.renameField("Engine", "Engine");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Invalid rename operation", e.getMessage());
        }
    }

    // --------------------- RENAME METHODS ---------------------

    @Test
    public void addMethodThenRenameIt_ThenMethodShouldBeRenamed()
    {
        List<String> parameters = new ArrayList<>();
        List<String> types = new ArrayList<>();
        c.addMethod("getEngine", "void", parameters, types);
        assertTrue(c.methodExists("getEngine", types));

        c.renameMethod("getEngine", types, "setEngine");
        assertTrue(c.methodExists("setEngine", types));
    }

    @Test
    public void addTwoMethodsAndRenameOneMethodToOneThatExistsWithSameTypes_ThenMethodShouldFailToBeRenamed()
    {
        
        try
        {
            List<String> parameters = new ArrayList<>();
            List<String> types = new ArrayList<>();
            c.addMethod("getEngine", "void", parameters, types);
            c.addMethod("setEngine", "void", parameters, types);
            c.renameMethod("setEngine", types, "getEngine");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Invalid rename operation", e.getMessage());
        }
    }

    @Test
    public void addTwoMethodsAndRenameOneMethodToOneThatExistsWithDifferentTypes_ThenMethodShouldBeRenamed()
    {
        List<String> parameters = new ArrayList<>();
        List<String> types = new ArrayList<>();
        c.addMethod("getEngine", "void", parameters, types);
        parameters.add("param1");
        types.add("String");
        c.addMethod("setEngine", "void", parameters, types);
        assertTrue(c.renameMethod("setEngine", types, "getEngine"));
    }

    @Test
    public void renameMethodToSameNameWithSameTypes_ThenMethodShouldFailToBeRenamed()
    {
        try
        {
            List<String> parameters = new ArrayList<>();
            List<String> types = new ArrayList<>();
            c.addMethod("Engine", "void", parameters, types);
            c.renameMethod("Engine", types, "Engine");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Invalid rename operation", e.getMessage());
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
            assertEquals("Field not found", e.getMessage());
        }
    }

    // --------------------- DELETE METHODS ---------------------

    @Test
    public void addMethodAndRemoveIt_ThenMethodShouldBeRemoved()
    {
        List<String> parameters = new ArrayList<>();
        List<String> types = new ArrayList<>();
        c.addMethod("Engine", "void", parameters, types);
        assertTrue(c.methodExists("Engine", types));

        c.removeMethod("Engine", types);
        assertFalse(c.methodExists("Engine", types));
    }

    @Test
    public void removeMethodThatDNE_ThrowsIllegalArgumentException()
    {
        try
        {
            List<String> types = new ArrayList<>();
            c.removeMethod("Engine", types);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Method not found", e.getMessage());
        }
    }

    // --------------------- CHANGE TYPES ---------------------

    @Test
    public void changeFieldType_ThenFieldTypeIsChanged()
    {
        c.addField("Name", "String");
        assertEquals(c.getField("Name").getType(), "String");

        c.getField("Name").setType("char");
        assertEquals(c.getField("Name").getType(), "char");
    }

    @Test
    public void changeMethodReturnType_ThenMethodReturnTypeIsChanged()
    {
        List<String> parameters = new ArrayList<>(Arrays.asList("Name", "Age"));
        List<String> types = new ArrayList<>(Arrays.asList("String", "int"));
        c.addMethod("Person", "void", parameters, types);
        assertEquals(c.getMethod("Person", types).getReturnType(), "void");

        c.getMethod("Person", types).setReturnType("boolean");
        assertEquals(c.getMethod("Person", types).getReturnType(), "boolean");
    }

    // --------------------- POSITION ---------------------
    
    @Test
    public void getPositoin_ThenPositionIsReturned()
    {
        c.setPosition(10, 10);
        assertTrue(c.getPosition().getX() == 10 && c.getPosition().getY() == 10);
    }

    @Test
    public void testFieldHashCode() {
        Field field = new Field("Name", "Type");
        assertNotNull(field.hashCode());
    }

    @Test
    public void testFieldNotEquals() {
        Field f = new Field("Name", "Type");
        assertFalse(f.equals(null));
    }

    @Test
    public void testFieldEqualsOtherField() {
        Field f1 = new Field("Name", "Type");
        Field f2 = new Field("Name", "Type");
        assertTrue(f1.equals(f2));
    }
}
