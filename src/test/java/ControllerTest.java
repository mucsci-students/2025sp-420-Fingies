import static org.junit.Assert.assertTrue;

import org.fingies.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests controller functionality
 * @author kdichter
 */
public class ControllerTest {
    private CLIView view;
    private JModel model;
    private Controller controller;

    @Before
    public void setUp() {
        UMLClassHandler.reset();
        view = new CLIView();
        model = new JModel();
        controller = new Controller(view, model);
    }

    @After
    public void resetTest() {
        UMLClassHandler.reset();
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
        boolean result = controller.doAddRelationship("A", "B", "AGGREGATION");
        assertTrue("Relationship should be added successfully.", result);
    }

    @Test
    public void testDoRemoveRelationship() {
        controller.doAddClass("A");
        controller.doAddClass("B");
        controller.doAddRelationship("A", "B", "AGGREGATION");
        boolean result = controller.doRemoveRelationship("A", "B");
        assertTrue("Relationship should be removed successfully.", result);
    }

    @Test
    public void testDoAddField() {
        controller.doAddClass("TestClass");
        boolean result = controller.doAddField("TestClass", "field1", "String");
        assertTrue("Field should be added successfully.", result);
    }

    @Test
    public void testDoRemoveField() {
        controller.doAddClass("TestClass");
        controller.doAddField("TestClass", "String", "field1");
        boolean result = controller.doRemoveField("TestClass", "field1");
        assertTrue("Attribute should be removed successfully.", result);
    }

    @Test
    public void testDoRenameField() {
        controller.doAddClass("TestClass");
        controller.doAddField("TestClass", "String", "oldField");
        boolean result = controller.doRenameField("TestClass", "oldField","String", "newField");
        assertTrue("Field should be renamed successfully.", result);
    }

    @Test
    public void testDoSave() {
        boolean result = controller.doSave("testfile.json");
        assertTrue("Data should be saved successfully.", result);
    }

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
    public void testRenameFieldAction() {
        String [] args1 = {"JSON"};
        String [] args2 = {"JSON", "STRING", "WILLSON"};
        String [] args3 = {"JSON", "WILLSON", "STRING", "KEVSON"};
        controller.runHelper(Action.ADD_CLASS, args1);
        controller.runHelper(Action.ADD_FIELD, args2);
        controller.runHelper(Action.RENAME_FIELD, args3);
        assertTrue("doRenameClass method was called through runHelper method", UMLClassHandler.getClass(args1[0]).fieldExists(args3[3]));
    }
}
