import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;

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
        UMLClassHandler.createClass("Car");
        UMLClassHandler.getClass("Car").addField("Wheels");
        UMLClassHandler.getClass("Car").addField("V16 Engine");
        UMLClassHandler.getClass("Car").addMethod("Drive", Arrays.asList("ThrottleAmount", "GasAmount"));
        UMLClassHandler.createClass("Animal");
        UMLClassHandler.getClass("Animal").addField("Eyeballs");
        UMLClassHandler.getClass("Animal").addMethod("Eat", Arrays.asList("Food", "Calories"));
        UMLClassHandler.createClass("Food");
        RelationshipHandler.addRelationship("Car", "Animal", RelationshipType.AGGREGATION);
        RelationshipHandler.addRelationship("Food", "Animal", RelationshipType.INHERITANCE);
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
    	//Reset Classes and Relations for testing
    	UMLClassHandler.reset();
        RelationshipHandler.reset();
        
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
