package ModelTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fingies.Model.Field;
import org.fingies.Model.Position;
import org.fingies.Model.UMLClass;
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
            
            // shouldn't run
            assertTrue("Adding a field with the name String% should've thrown an exception because it contains invalid characters.", false);
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
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
            
            // shouldn't run
            assertTrue("Adding a field with the same name as its class should've thrown an exception.", false);
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
            
            // shouldn't run
            assertTrue("Adding a field to a class that already has a field with the same name should've thrown an exception.", false);
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
            
            // shouldn't run
            assertTrue("Adding a field with the name Engine% should've thrown an exception because it contains invalid characters.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
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
            
            // shouldn't run
            assertTrue("Adding a method with the name Engine% should've thrown an exception because it contains invalid characters.", false);
        }   
        catch (IllegalArgumentException e)
        {
            assertEquals("String contains invalid characters", e.getMessage());
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
    	List<String> parameters = new ArrayList<>();
        List<String> types = new ArrayList<>();
        c.addMethod("getEngine", "void", parameters, types);
        assertTrue(c.methodExists("getEngine", types));
        try
        {
            c.addMethod("getEngine", "void", parameters, types);
            
            // shouldn't run
            assertTrue("Adding a field with the same name as its class should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue(e.getMessage().contains("getEngine"));
            assertTrue(e.getMessage().contains(parameters.toString()));
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
            
            // shouldn't run
            assertTrue("Adding a field with the same name as its class should've thrown an exception.", false);
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
    	c.addField("Engine", "String");
        c.addField("Wheel", "String");
        try
        {
            c.renameField("Engine", "Wheel");
            
            // shouldn't run
            assertTrue("Renaming a field to the name of a field that already exists in the class should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Renaming the field Engine to Wheel should've thrown an error that contained the name Wheel, but was " + e.getMessage(), e.getMessage().contains("Wheel"));
        }
    }

    @Test
    public void renameFieldToSameName_ThenFieldShouldFailToBeRenamed()
    {
    	c.addField("Engine", "String");
        try
        {
            c.renameField("Engine", "Engine");
            
            // shouldn't run
            assertTrue("Renaming the field Engine to Engine should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Renaming the field Engine to Engine should've thrown an error that contained the name Engine, but was " + e.getMessage(), e.getMessage().contains("Engine"));
        }
    }
    
    @Test
    public void renameFieldThatDNE_ThenFieldShouldFailToBeRenamed()
    {
        try
        {
            c.renameField("Engine", "Wheel");
            
            // shouldn't run
            assertTrue("Renaming a field that doesn't exist should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Renaming a field that doesn't exist should've thrown an error that contained the name Engine, but was " + e.getMessage(), e.getMessage().contains("Engine"));
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
    	List<String> parameters = new ArrayList<>();
        List<String> types = new ArrayList<>();
        c.addMethod("getEngine", "void", parameters, types);
        c.addMethod("setEngine", "void", parameters, types);
        try
        {
            c.renameMethod("setEngine", types, "getEngine");
            
            // shouldn't run
            assertTrue("Renaming the method setEngine() to a method name that already exists should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Renaming the method setEngine() should've thrown an error that contained the name setEngine, but was " + e.getMessage(), e.getMessage().contains("getEngine"));
            assertTrue("Renaming the method setEngine() should've thrown an error that contained parameter types [], but was " + e.getMessage(), e.getMessage().contains(types.toString()));
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
    	List<String> parameters = new ArrayList<>();
        List<String> types = new ArrayList<>();
        c.addMethod("Engine", "void", parameters, types);
        try
        {
            c.renameMethod("Engine", types, "Engine");
            
            // shouldn't run
            assertTrue("Renaming the method Engine() so that it had the same name and parameter types as another method should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Renaming the method Engine() should've thrown an error that contained the name Engine, but was " + e.getMessage(), e.getMessage().contains("Engine"));
        }
    }
    
    @Test
    public void renameMethodThatDNE_ThenMethodShouldFailToBeRenamed()
    {
        try
        {
            List<String> types = new ArrayList<>();
            c.renameMethod("Engine", types, "Wheel");
            
            // shouldn't run
            assertTrue("Renaming a method that doesn't exist should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Renaming a method that doesn't exist should've thrown an error that contained the name Engine, but was " + e.getMessage(), e.getMessage().contains("Engine"));
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
            
            // shouldn't run
            assertTrue("Removing a method that does not exist should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Renaming the method setEngine() should've thrown an error that contained parameter types [], but was " + e.getMessage(), e.getMessage().contains("Engine"));
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
    	List<String> types = new ArrayList<>();
        try
        {
            c.removeMethod("Engine", types);
            
            // shouldn't run
            assertTrue("Removing a method that does not exist should've thrown an exception.", false);
        }
        catch (IllegalArgumentException e)
        {
            assertTrue("Removing the method Engine() should've thrown an error that contained the name Engine, but was " + e.getMessage(), e.getMessage().contains("Engine"));
            assertTrue("Removing the Engine() should've thrown an error that contained parameter types [], but was " + e.getMessage(), e.getMessage().contains(types.toString()));
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

    Position p = new Position(10, 10);
    
    @Test
    public void getPosition_ThenPositionIsReturned()
    {
        c.setPosition(10, 10);
        assertTrue(c.getPosition().getX() == 10 && c.getPosition().getY() == 10);
    }

    @Test
    public void testPositionEqualsSameObject() {
        assertTrue(p.equals(p));
    }

    @Test
    public void testPositionNotEqualsNull() {
        assertFalse(p.equals(null));
    }

    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testPositionNotEqualOtherObject() {
        assertFalse(p.equals(c));
    }

    @Test
    public void testPositionNotEqualOtherPosition() {
        Position z = new Position(69, 420);
        assertFalse(p.equals(z));
    }

    @Test
    public void testPositionToString() {
        assertEquals(p.toString(), "Position [x=10, y=10]");
    }

    // --------------------- FIELD ---------------------

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

    @Test
    public void testToString() {
        assertEquals(c.toString(), "UMLClass");
    }
}
