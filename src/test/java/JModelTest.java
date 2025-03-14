import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.fingies.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.After;

/**
 * Tests for the JModel.java class
 * @author trush
 */
public class JModelTest {
    JModel model = new JModel();
    String absolutePath = model.getProgramDirectory();
    String path = absolutePath + "/modelTest.json";

    @Before
    public void setUp() {
        UMLClassHandler.reset();
        UMLClassHandler.createClass("Car");
        UMLClassHandler.getClass("Car").addField("Wheels", "Wheels");
        UMLClassHandler.getClass("Car").addField("V16Engine", "Engine");
        List<String> carParams = new ArrayList<>();
        List<String> carTypes = new ArrayList<>();
        carParams.add("ThrottleAmount");
        carTypes.add("Float");
        carParams.add("gasAmount");
        carTypes.add("Float");
        UMLClassHandler.getClass("Car").addMethod("Drive", "void", carParams, carTypes);
        UMLClassHandler.getClass("Car").setPosition(1000, 500);
        UMLClassHandler.createClass("Animal");
        UMLClassHandler.getClass("Animal").addField("Eyeballs", "int");
        List<String> animalParams = new ArrayList<>();
        List<String> animalTypes = new ArrayList<>();
        animalParams.add("Food");
        animalTypes.add("String");
        animalParams.add("Calories");
        animalTypes.add("int");
        UMLClassHandler.getClass("Animal").addMethod("Eat", "void", animalParams, animalTypes);
        UMLClassHandler.createClass("Food");
        RelationshipHandler.addRelationship("Car", "Animal", RelationshipType.Aggregation);
        RelationshipHandler.addRelationship("Food", "Animal", RelationshipType.Inheritance);
    }

    @After
    public void resetTest() {
        RelationshipHandler.removeRelationship("Car", "Animal");
        RelationshipHandler.removeRelationship("Food", "Animal");
    }

    @Test
    public void retrieveProgramDirectory() {
        assertNotNull(model.getProgramDirectory());
    }

    @Test
    public void testErrorWritingToLog() {
        assertTrue(model.writeToLog("Test Write to Error Log, Ignore this message."));
    }

    @Test
    public void successfulSave() {
        assertTrue(model.saveData(path));
    }

    @Test
    public void testFileExist() {
        assertTrue(model.fileExist(path));
    }

    @Test
    public void loadSave() {
    	System.out.println("@Test loadSave():");
        assertNotNull(model.loadData(path));
        HashSet<UMLClass> umlClasses = UMLClassHandler.getAllClasses();
        for (UMLClass umlClass : umlClasses) {
            System.out.print(umlClass.getName() + " ");
        }
        System.out.println();
        System.out.println(RelationshipHandler.listRelationships());
    }
    
    @Test
    public void loadTwice()
    {
    	System.out.println("@Test loadTwice() (first time):");
        
        assertNotNull(model.loadData(path));
        HashSet<UMLClass> umlClasses = UMLClassHandler.getAllClasses();
        for (UMLClass umlClass : umlClasses) {
            System.out.print(umlClass.getName() + " ");
        }
        System.out.println();
        System.out.println(RelationshipHandler.listRelationships());
        
        System.out.println("@Test loadTwice() (second time):");
        assertNotNull(model.loadData(path));
        umlClasses = UMLClassHandler.getAllClasses();
        for (UMLClass umlClass : umlClasses) {
            System.out.print(umlClass.getName() + " ");
        }
        System.out.println();
        System.out.println(RelationshipHandler.listRelationships());
    }
}
