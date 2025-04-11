import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.fingies.UMLClass;
import org.fingies.UMLClassHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests the UMLClassHandler class
 * @author kdichter
 */
public class UMLClassHandlerTest {
    @Before
    public void setUp()
    {
        UMLClassHandler.reset();
        UMLClassHandler.createClass("Car");
            UMLClassHandler.getClass("Car").addField("Engine", "String");
            UMLClassHandler.getClass("Car").addField("Wheel", "String");
            UMLClassHandler.getClass("Car").addField("Pedal", "String");
        UMLClassHandler.createClass("Animal");
            UMLClassHandler.getClass("Animal").addField("Cat", "String");
            UMLClassHandler.getClass("Animal").addField("Dog", "String");
        UMLClassHandler.createClass("Food");
            UMLClassHandler.getClass("Food").addField("Breakfast", "String");
            UMLClassHandler.getClass("Food").addField("Lunch", "String");
            UMLClassHandler.getClass("Food").addField("Dinner", "String");
    }
    @After
    public void resetTest() {
        UMLClassHandler.reset();
    }

    // --------------------- CREATE CLASS ---------------------

    @Test
    public void createOneClassFromString_ThenClassShouldBeCreated()
    {
        UMLClassHandler.createClass("Person");
        assertTrue(UMLClassHandler.exists("Person"));
    }

    @Test
    public void createOneClassFromStringThatAlreadyExists_ThenIllegalArgumentExceptionThrown()
    {
        UMLClassHandler.createClass("Person");
        assertTrue(UMLClassHandler.exists("Person"));
        try
        {
            UMLClassHandler.createClass("Person");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Class Person already exists", e.getMessage());
        }
    }

    @Test
    public void createOneClassFromClassObject_ThenClassShouldBeCreated()
    {
        UMLClass c = new UMLClass ("Person");
        UMLClassHandler.addClassObject(c);
        assertTrue(UMLClassHandler.exists("Person"));
    }

    @Test
    public void createOneClassFromClassObjectThatAlreadyExists_ThenClassShouldNotBeCreated()
    {
        UMLClassHandler.createClass("Person");
        UMLClass c = new UMLClass ("Person");
        assertFalse(UMLClassHandler.addClassObject(c));
    }


    // --------------------- RENAME CLASS ---------------------
    @Test
    public void renameCarClassToTank()
    {
        UMLClassHandler.renameClass("Car", "Tank");
        assertFalse(UMLClassHandler.exists("Car"));
        assertTrue(UMLClassHandler.exists("Tank"));
    }

    @Test
    public void renameCarClassToAnimalClass()
    {
        try
        {
            UMLClassHandler.renameClass("Car", "Animal");
        }
        catch(IllegalArgumentException e)
        {
            assertEquals("Another class already has the name Animal", e.getMessage());
        }
    }

    @Test
    public void renameCarClassToClassWithoutAlphaNumericCharacters()
    {
        try
        {
            UMLClassHandler.renameClass("Car", "Tank%");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("The name Tank% contains invalid characters", e.getMessage());
        }
    }

    @Test
    public void renameClassToLongerThan50Characters()
    {
        try
        {
            UMLClassHandler.renameClass("Car", "jjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjjk");
        }
        catch (IllegalArgumentException e)
        {
            assertEquals("Class names must not be longer than 50 characters", e.getMessage());
        }
    }

    // --------------------- REMOVE CLASS ---------------------

    @Test
    public void removeTheCarClass()
    {
        UMLClassHandler.removeClass("Car");
        assertFalse(UMLClassHandler.exists("Car"));
    }

    @Test
    public void removeAClassThatDoesntExist()
    {
        try
        {
            UMLClassHandler.removeClass("Class1");
        }
        catch(IllegalArgumentException e)
        {
            assertEquals("Class Class1 does not exist", e.getMessage());
        }
        
    }

    
    // --------------------- EXTRA TESTS ---------------------

    @Test
    public void testListClassWithNoFieldsOrMethods()
    {
        UMLClassHandler.createClass("Person");
        assertEquals(UMLClassHandler.listClass(UMLClassHandler.getClass("Person")), "Person");
    }

    @Test
    public void testListClassWithOneClass()
    {
        assertEquals(UMLClassHandler.listClass(UMLClassHandler.getClass("Car")), "Car\n\tString Engine\n\tString Wheel\n\tString Pedal");
        ArrayList<String> params = new ArrayList<String>(Arrays.asList("color"));
        ArrayList<String> types = new ArrayList<String>(Arrays.asList("String"));
        UMLClassHandler.getClass("Car").addMethod("changeColor", "String", params, types);
        assertEquals(UMLClassHandler.listClass(UMLClassHandler.getClass("Car")), "Car\n\tString Engine\n\tString Wheel\n\tString Pedal\n\tString changeColor (String color)");
    }

    @Test
    public void testListClassesWithNoClasses()
    {
        UMLClassHandler.reset();
        assertEquals(UMLClassHandler.listClasses(), "No current classes exist");
    }

    @Test
    public void testListClassesWithOneClasses()
    {
        UMLClassHandler.reset();
        UMLClassHandler.createClass("Car");
            UMLClassHandler.getClass("Car").addField("Engine", "String");
            UMLClassHandler.getClass("Car").addField("Wheel", "String");
            UMLClassHandler.getClass("Car").addField("Pedal", "String");
        assertEquals(UMLClassHandler.listClasses(), "Car\n\tString Engine\n\tString Wheel\n\tString Pedal");
        ArrayList<String> params = new ArrayList<String>(Arrays.asList("color"));
        ArrayList<String> types = new ArrayList<String>(Arrays.asList("String"));
        UMLClassHandler.getClass("Car").addMethod("changeColor", "String", params, types);
        assertEquals(UMLClassHandler.listClasses(), "Car\n\tString Engine\n\tString Wheel\n\tString Pedal\n\tString changeColor (String color)");
    }

    @Test
    public void testListClassesWithManyClasses()
    {
        UMLClassHandler.reset();
        UMLClassHandler.createClass("Car");
            UMLClassHandler.getClass("Car").addField("Engine", "String");
            UMLClassHandler.getClass("Car").addField("Wheel", "String");
            UMLClassHandler.getClass("Car").addField("Pedal", "String");
        UMLClassHandler.createClass("Animal");
                UMLClassHandler.getClass("Animal").addField("Cat", "String");
                UMLClassHandler.getClass("Animal").addField("Dog", "String");
        assertEquals(UMLClassHandler.listClasses(), "Car\n\tString Engine\n\tString Wheel\n\tString Pedal\nAnimal\n\tString Cat\n\tString Dog");
    }

    @Test
    public void testResetFunction() {
        try
        {
            UMLClassHandler.reset();
            HashSet<UMLClass> classes = UMLClassHandler.getAllClasses();
            assertTrue(classes.isEmpty());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assertTrue(false);
        }  
    }

    @Test
    public void testRenameClassThatDoesNotExist() {
        try {
            UMLClassHandler.renameClass("ThisClassDoesNotExist", "ItReallyDoesNot");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Class ThisClassDoesNotExist does not exist");
        }
    }

    @Test
    public void testUMLClassHandlerConstruction() {
        UMLClassHandler classHandler = new UMLClassHandler();
        assertTrue(classHandler != null);
    }
}
