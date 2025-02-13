import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

public class ControllerTester {
    private CLIView view;
    private JModel model;
    private Controller controller;

    @Before
    public void setUp() {
        view = new CLIView();  // Assuming CLIView has a default constructor
        model = new JModel();  // Assuming JModel has a default constructor
        controller = new Controller(view, model);
    }

    // --------------------- METHOD FUNCTIONALITY ---------------------

    @Test
    public void testDoAddClass() {
        boolean result = controller.doAddClass("TestClass");
        assertTrue("Class should be added successfully.", result);
    }

    @Test
    public void testDoRemoveClass() {
        controller.doAddClass("TestClass");
        boolean result = controller.doRemoveClass("TestClass");
        assertTrue("Class should be removed successfully.", result);
    }

    @Test
    public void testDoRenameClass() {
        controller.doAddClass("OldClass");
        boolean result = controller.doRenameClass("OldClass", "NewClass");
        assertTrue("Class should be renamed successfully.", result);
    }

    @Test
    public void testDoAddRelationship() {
        controller.doAddClass("A");
        controller.doAddClass("B");
        boolean result = controller.doAddRelationship("A", "B");
        assertTrue("Relationship should be added successfully.", result);
    }

    @Test
    public void testDoRemoveRelationship() {
        controller.doAddClass("A");
        controller.doAddClass("B");
        controller.doAddRelationship("A", "B");
        boolean result = controller.doRemoveRelationship("A", "B");
        assertTrue("Relationship should be removed successfully.", result);
    }

    @Test
    public void testDoAddAttribute() {
        controller.doAddClass("TestClass");
        boolean result = controller.doAddAttribute("TestClass", "attribute1");
        assertTrue("Attribute should be added successfully.", result);
    }

    @Test
    public void testDoRemoveAttribute() {
        controller.doAddClass("TestClass");
        controller.doAddAttribute("TestClass", "attribute1");
        boolean result = controller.doRemoveAttribute("TestClass", "attribute1");
        assertTrue("Attribute should be removed successfully.", result);
    }

    @Test
    public void testDoRenameAttribute() {
        controller.doAddClass("TestClass");
        controller.doAddAttribute("TestClass", "oldAttr");
        boolean result = controller.doRenameAttribute("TestClass", "oldAttr", "newAttr");
        assertTrue("Attribute should be renamed successfully.", result);
    }

    @Test
    public void testDoSave() {
        boolean result = controller.doSave("testfile.json");
        assertTrue("Data should be saved successfully.", result);
    }

    /* Not ready yet (Tristan, this one's all you)
    @Test
    public void testDoLoad() {
        UMLClassHandler data = controller.doLoad("testfile.json");
        assertNotNull("Loaded data should not be null.", data);
    }
    */

    // --------------------- RUN METHODS ---------------------

    @Test
    public void testAddClassAction() {
        String [] args = {"JSON"};
        controller.runHelper(Action.ADD_CLASS, args);
        assertTrue("doAddClass method was called through runHelper method", UMLClassHandler.exists(args[0]));
    }

    @Test
    public void testRenameClassAction() {
        String [] args1 = {"JSON"};
        String [] args2 = {"JSON", "WILLSON"};
        controller.runHelper(Action.ADD_CLASS, args1);
        controller.runHelper(Action.RENAME_CLASS, args2);
        assertTrue("doRenameClass method was called through runHelper method", UMLClassHandler.exists(args2[1]));
    }

    @Test
    public void testRenameAttributeAction() {
        String [] args1 = {"JSON"};
        String [] args2 = {"JSON", "WILLSON"};
        String [] args3 = {"JSON", "WILLSON", "KEVSON"};
        controller.runHelper(Action.ADD_CLASS, args1);
        controller.runHelper(Action.ADD_ATTRIBUTE, args2);
        controller.runHelper(Action.RENAME_ATTRIBUTE, args3);
        assertTrue("doRenameClass method was called through runHelper method", UMLClassHandler.getClass(args1[0]).exists(args3[2]));
    }
}
