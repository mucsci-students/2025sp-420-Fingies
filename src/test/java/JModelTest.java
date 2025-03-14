import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.HashSet;

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
        HashMap<String, String> carParams = new HashMap<>();
        carParams.put("ThrottleAmount", "Float");
        carParams.put("gasAmount", "Float");
        UMLClassHandler.getClass("Car").addMethod("Drive", "void", carParams);
        UMLClassHandler.getClass("Car").setPosition(1000, 500);
        UMLClassHandler.createClass("Animal");
        UMLClassHandler.getClass("Animal").addField("Eyeballs", "int");
        HashMap<String, String> animalParams = new HashMap<>();
        animalParams.put("Food", "String");
        animalParams.put("Calories", "int");
        UMLClassHandler.getClass("Animal").addMethod("Eat", "void", animalParams);
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
