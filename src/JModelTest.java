import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

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
        assertNotNull(model.loadData(path));
    }
}
