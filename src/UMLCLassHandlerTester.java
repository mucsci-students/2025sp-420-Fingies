import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class UMLCLassHandlerTester {
    @Before
    public void setUp()
    {
        UMLClassHandler.createClass("Car");
            UMLClassHandler.getClass("Car").addAttribute("Engine");
            UMLClassHandler.getClass("Car").addAttribute("Wheel");
            UMLClassHandler.getClass("Car").addAttribute("Pedal");
        UMLClassHandler.createClass("Animal");
            UMLClassHandler.getClass("Animal").addAttribute("Cat");
            UMLClassHandler.getClass("Animal").addAttribute("Dog");
        UMLClassHandler.createClass("Food");
            UMLClassHandler.getClass("Food").addAttribute("Breakfast");
            UMLClassHandler.getClass("Food").addAttribute("Lunch");
            UMLClassHandler.getClass("Food").addAttribute("Dinner");
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
        assertFalse(UMLClassHandler.renameClass("Car", "Animal"));
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
            assertEquals("String contains invalid characters", e.getMessage());
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
            assertEquals("String is longer than 50 characters", e.getMessage());
        }
    }


    // --------------------- ADD RELATIONSHIPS ---------------------

    @Test
    public void addRelationshipBetweenCarAndFoodClasses()
    {
        UMLClassHandler.addRelationship("Car", "Food");
        assertTrue(UMLClassHandler.getClass("Car").getOutgoing().contains("Food"));
        assertTrue(UMLClassHandler.getClass("Food").getIncoming().contains("Car"));
    }

    @Test
    public void addRelationshipBetweenTwoClassesThatDontExist()
    {
        try
        {
            UMLClassHandler.addRelationship("Class1", "Class2");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
        }
        
    }

    // --------------------- REMOVE RELATIONSHIPS ---------------------
    
    @Test
    public void removeRelationshpBetweenExistingCarAndFoodClasses()
    {
        UMLClassHandler.addRelationship("Car", "Food");
        assertTrue(UMLClassHandler.getClass("Car").getOutgoing().contains("Food"));
        assertTrue(UMLClassHandler.getClass("Food").getIncoming().contains("Car"));

        UMLClassHandler.removeRelationship("Car", "Food");
        assertTrue(UMLClassHandler.getClass("Car").getOutgoing().isEmpty());
        assertTrue(UMLClassHandler.getClass("Food").getIncoming().isEmpty());
    }

    @Test 
    public void removeRelationshipBetweenTwoClassesThatDontExist()
    {
        try
        {
            UMLClassHandler.removeRelationship("Class1", "Class2");
        }
        catch (IllegalArgumentException e)
        {
            System.out.println(e.getMessage());
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
    public void removeTheCarClassAfterAddingARelationshipBetweenTheCarAndAnimalClasses()
    {
        UMLClassHandler.addRelationship("Car", "Food");
        assertTrue(UMLClassHandler.getClass("Car").getOutgoing().contains("Food"));
        assertTrue(UMLClassHandler.getClass("Food").getIncoming().contains("Car"));

        UMLClassHandler.removeClass("Car");
        assertFalse(UMLClassHandler.exists("Car"));
        assertTrue(UMLClassHandler.getClass("Food").getIncoming().isEmpty());
    }

    @Test
    public void removeAClassThatDoesntExist()
    {
        assertFalse(UMLClassHandler.removeClass("Class1"));
    }

    
    // --------------------- DELETE ATTRIBUTE ---------------------
    @Test
    public void testResetFunction() {
        UMLClassHandler.createClass("Car");
            UMLClassHandler.getClass("Car").addAttribute("Engine");
            UMLClassHandler.getClass("Car").addAttribute("Wheel");
            UMLClassHandler.getClass("Car").addAttribute("Pedal");
        UMLClassHandler.createClass("Animal");
            UMLClassHandler.getClass("Animal").addAttribute("Cat");
            UMLClassHandler.getClass("Animal").addAttribute("Dog");
        UMLClassHandler.createClass("Food");
            UMLClassHandler.getClass("Food").addAttribute("Breakfast");
            UMLClassHandler.getClass("Food").addAttribute("Lunch");
            UMLClassHandler.getClass("Food").addAttribute("Dinner");
        UMLClassHandler.addRelationship("Car", "Food");

        UMLClassHandler.reset();
        HashSet<UMLClass> classes = UMLClassHandler.getAllClasses();
        assertTrue(classes.isEmpty());
    }
}
