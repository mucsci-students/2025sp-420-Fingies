import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.fingies.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests controller functionality
 * @author kdichter
 */
public class ControllerTest {
    private View view;
    private JModel model;
    private Controller controller;

    @Before
    public void setUp() {
        UMLClassHandler.reset();
     // dummy view
 		view = new View() {
 		    @Override public void run() {}
 		    @Override public String promptForSaveInput(String message) { return null; }
 		    @Override public String promptForOpenInput(String message) { return null; }
 		    @Override public String promptForInput(String message) { return null; }
 		    @Override public List<String> promptForInput(List<String> messages) { return null; }
 		    @Override public List<String> promptForInput(List<String> messages, List<InputCheck> checks) { return null; }
 		    @Override public void notifySuccess() {}
 		    @Override public void notifySuccess(String message) {}
 		    @Override public void notifyFail(String message) {}
 		    @Override public void display(String message) {}
 		    @Override public void help() {}
 		    @Override public void help(String command) {}
 		    @Override public void setController(Controller c) {}
 		};
        model = new JModel();
        controller = new Controller(view, model);
    }

    @After
    public void resetTest() {
        UMLClassHandler.reset(); // both resets must always be called together
        RelationshipHandler.reset(); 
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
        boolean result = controller.doRenameField("TestClass", "oldField", "newField");
        assertTrue("Field should be renamed successfully.", result);
    }

    @Test
    public void testDoSave() {
        boolean result = controller.doSave("testfile.json");
        assertTrue("Data should be saved successfully.", result);
    }

    @Test
    public void changeFieldType()
    {
        controller.doAddClass("TestClass");
        controller.doAddField("TestClass", "String", "TestField");
        boolean result = controller.doChangeFieldDataType("TestClass", "TestField", "char");
        assertTrue("Field type should be changed successfully.", result);
    }

    @Test
    public void changeMethodReturnTypeZeroParameters()
    {
        List<String> empty = new ArrayList<>();

        controller.doAddClass("TestClass");
        controller.doAddMethod("TestClass", "TestMethod", "void", empty, empty);
        boolean result = controller.doChangeMethodReturnType("TestClass", "TestMethod", empty, "int");
        assertTrue("Method return type should be changed successfully.", result);
    }

    @Test
    public void changeParameterType()
    {
        List<String> parameters = new ArrayList<>(Arrays.asList("Name", "Age"));
        List<String> types = new ArrayList<>(Arrays.asList("String", "int"));

        controller.doAddClass("TestClass");
        controller.doAddMethod("TestClass", "TestMethod", "void", parameters, types);
        boolean result = controller.doChangeParameterDataType("TestClass", "TestMethod", types, "Name", "char");
        assertTrue("Parameter type should be changed successfully.", result);
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
        String [] args3 = {"JSON", "WILLSON", "KEVSON"};
        controller.runHelper(Action.ADD_CLASS, args1);
        controller.runHelper(Action.ADD_FIELD, args2);
        controller.runHelper(Action.RENAME_FIELD, args3);
        assertTrue("doRenameClass method was called through runHelper method", UMLClassHandler.getClass(args1[0]).fieldExists(args3[2]));
    }
}
