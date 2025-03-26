import static org.junit.Assert.assertTrue;

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
    public void testListClass()
    {
        assertEquals(UMLClassHandler.listClass(UMLClassHandler.getClass("Car")), "");
    }

    @Test
    public void testResetFunction() {
        try
        {
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
            // UMLClassHandler.addRelationship("Car", "Food");

            UMLClassHandler.reset();
            HashSet<UMLClass> classes = UMLClassHandler.getAllClasses();
            assertTrue(classes.isEmpty());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }  
    }

    // TODO: Write Tests for method return type char validation

    // TODO: Write Tests for param type char validation

    // TODO: Write Tests for field type char validation
}
