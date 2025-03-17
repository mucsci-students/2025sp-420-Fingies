import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.fingies.Action;
import org.fingies.Command;
import org.junit.Assert;
import org.junit.Test;

/**
 * A bunch of test cases for the Command class.
 * 
 * I used ChatGPT to refactor the tests so that they reference Command.COMMANDS[*some integer*] rather than naming
 * the commands explicitly (such as "add class") so that they can be changed later. It also wrote the last few tests
 * for me.
 * 
 * @author Lincoln Craddock
 */
public class CommandTest {
	
	// --------------------- ADD CLASS ---------------------

	@Test
	public void addClassAction() {
	    String cmd = Command.COMMANDS[0] + " class1"; // Dynamically reference "add class"
	    Command c = Command.parseCommand(cmd);
	    Assert.assertEquals("Parsing the command \n" + cmd + "\n should return a command with the ADD_CLASS enum.", c.action, Action.ADD_CLASS);
	}

	@Test
	public void addClassArgCount() {
	    String cmd = Command.COMMANDS[0] + " class1"; // Dynamically reference "add class"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with one argument.", 1, c.arguments.length);
	}

	@Test
	public void addClassArg() {
	    String cmd = Command.COMMANDS[0] + " class1"; // Dynamically reference "add class"
	    String expected = "class1";
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected + "\".", expected, c.arguments[0]);
	}
	
	
	// --------------------- WITH MANY ARGUMENTS ---------------------

	@Test
	public void largeArgCount() {
	    int cnt = Command.maxNumArgs;

	    String cmd = Command.COMMANDS[0] + " "; // "add class"
	    for (int i = 0; i < cnt; ++i)
	        cmd += "class" + i + " ";

	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return " + cnt + " arguments.", cnt, c.arguments.length);
	}

	@Test
	public void manyArgs() {
	    int cnt = Command.maxNumArgs;

	    String cmd = Command.COMMANDS[0] + " "; // "add class"
	    for (int i = 0; i < cnt; ++i)
	        cmd += "class" + i + " ";

	    String[] expected = new String[cnt];
	    for (int i = 0; i < cnt; ++i)
	        expected[i] = "class" + i;

	    Command c = Command.parseCommand(cmd);
	    for (int i = 0; i < cnt; ++i) {
	        assertEquals(
	            "Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[i] + "\".",
	            expected[i],
	            c.arguments[i]
	        );
	    }
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void tooManyArgs() {
	    int cnt = Command.maxNumArgs + 1;

	    String cmd = Command.COMMANDS[0] + " "; // "add class"
	    for (int i = 0; i < cnt; ++i)
	        cmd += "class" + i + " ";
	    
	    Command.parseCommand(cmd);
	}

    
	// --------------------- REMOVE RELATIONSHIP W/ QUOTES ---------------------

	@Test
	public void removeRelationshipAction() {
	    String cmd = Command.COMMANDS[4] + " class1 class2"; // "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the REMOVE_RELATIONSHIP enum.", c.action, Action.REMOVE_RELATIONSHIP);
	}

	@Test
	public void removeRelationshipArgCount() {
	    String cmd = Command.COMMANDS[4] + " class1 class2"; // "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with 2 arguments.", 2, c.arguments.length);
	}

	@Test
	public void removeRelationshipArg() {
	    String cmd = Command.COMMANDS[4] + " class1 class2"; // "remove relationship"
	    String[] expected = {"class1", "class2"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[0] + "\".", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[1] + "\".", expected[1], c.arguments[1]);
	}

	@Test
	public void removeRelationshipWithQuotesAction() {
	    String cmd = Command.COMMANDS[4] + " \"class1\" class2"; // "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the REMOVE_RELATIONSHIP enum.", c.action, Action.REMOVE_RELATIONSHIP);

	    cmd = Command.COMMANDS[4] + " class1 \"class2\""; // "remove relationship"
	    c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the REMOVE_RELATIONSHIP enum.", c.action, Action.REMOVE_RELATIONSHIP);
	}

	@Test
	public void removeRelationshipWithQuotesArgCount() {
	    String cmd = Command.COMMANDS[4] + " \"class1\" class2"; // "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with 2 arguments.", 2, c.arguments.length);

	    cmd = Command.COMMANDS[4] + " class1 \"class2\""; // "remove relationship"
	    c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with 2 arguments.", 2, c.arguments.length);
	}

	@Test
	public void removeRelationshipWithQuotesArg() {
	    String cmd = Command.COMMANDS[4] + " \"class1\" class2"; // "remove relationship"
	    String[] expected = {"class1", "class2"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[0] + "\".", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[1] + "\".", expected[1], c.arguments[1]);

	    cmd = Command.COMMANDS[4] + " class1 \"class2\""; // "remove relationship"
	    c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[0] + "\".", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[1] + "\".", expected[1], c.arguments[1]);
	}
    
	
	// --------------------- WITH LONG ARGUMENTS ---------------------

	@Test
	public void longRandomArgsCount() {
	    int cnt = 3; // Number of arguments
	    int len = 500; // Length of each argument
	    String possibleChars = "qwertyQWERTY1230   '[]{}|!@#$%^&*()_+/?,.<>-=";

	    StringBuilder cmdBuilder = new StringBuilder(Command.COMMANDS[0] + " "); // Dynamically reference "add class"
	    String[] expected = new String[cnt];
	    for (int j = 0; j < cnt; ++j) {
	        StringBuilder argBuilder = new StringBuilder();
	        for (int i = 0; i < len; ++i) {
	            argBuilder.append(possibleChars.charAt((int) (Math.random() * possibleChars.length())));
	        }
	        expected[j] = argBuilder.toString();
	        cmdBuilder.append("\"").append(expected[j]).append("\" ");
	    }

	    String cmd = cmdBuilder.toString();
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing a long command with random args should return " + cnt + " arguments.", cnt, c.arguments.length);
	}

	@Test
	public void longRandomArgs() {
	    int cnt = 3; // Number of arguments
	    int len = 500; // Length of each argument
	    String possibleChars = "qwertyQWERTY1230   '[]{}|!@#$%^&*()_+/?,.<>-=";
	    
	    StringBuilder cmdBuilder = new StringBuilder(Command.COMMANDS[0] + " "); // "add class"
	    String[] expected = new String[cnt];
	    for (int j = 0; j < cnt; ++j) {
	        StringBuilder argBuilder = new StringBuilder();
	        for (int i = 0; i < len; ++i) {
	            argBuilder.append(possibleChars.charAt((int) (Math.random() * possibleChars.length())));
	        }
	        expected[j] = argBuilder.toString();
	        cmdBuilder.append("\"").append(expected[j]).append("\" ");
	    }

	    String cmd = cmdBuilder.toString();
	    Command c = Command.parseCommand(cmd);
	    for (int i = 0; i < cnt; ++i) {
	        assertEquals("Parsing a long command with random args should return a command with the argument \"" + expected[i] + "\".", expected[i], c.arguments[i]);
	    }
	}
	
	// --------------------- WITH LOTS OF SPACES ---------------------
	
	@Test
	public void lotsOfIntermediateSpaces() {
		String cmd = " ".repeat(100) + Command.COMMANDS[3] + " ".repeat(200) + "\"a class\"" + " ".repeat(300) + "someClass" + " ".repeat(400);
		Command c = Command.parseCommand(cmd);
		assertEquals("Parsing a command with many spaces in between the arguments should return the right number of arguments.\n", c.arguments.length, 2);
		assertEquals("Parsing a command with many spaces in between the arguments should return the right arguments.\n", "a class", c.arguments[0]);
		assertEquals("Parsing a command with many spaces in between the arguments should return the right arguments.\n", "someClass", c.arguments[1]);	
	}
	
	@Test
	public void argsWithLostOfSpaces() {
		String cmd =  Command.COMMANDS[3] + " \"argument" + " ".repeat(100) + "one\" \"argument" + " ".repeat(100) + "two\"";
		Command c = Command.parseCommand(cmd);
		assertEquals("Parsing a command with many spaces in the arguments should return the right number of arguments.\n", c.arguments.length, 2);
		assertEquals("Parsing a command with many spaces in the arguments should return the right arguments.\n", "argument" + " ".repeat(100) + "one", c.arguments[0]);
		assertEquals("Parsing a command with many spaces in the arguments should return the right arguments.\n", "argument" + " ".repeat(100) + "two", c.arguments[1]);	
	}
	
	
	// --------------------- EQUALS ---------------------
	
	@Test
	public void sameClassesWithoutArgsEqual()
	{
		Command c1 = Command.parseCommand("" + Command.COMMANDS[14]);
		Command c2 = Command.parseCommand("" + Command.COMMANDS[14]);
		assertEquals("Two identical commands (" + Command.COMMANDS[14] + ") should be equal.", c1, c2);
	}
	
	@Test
	public void differentClassesWithoutArgsUnequal()
	{
		Command c1 = Command.parseCommand("" + Command.COMMANDS[13]);
		Command c2 = Command.parseCommand("" + Command.COMMANDS[14]);
		assertNotEquals("The command \n" + Command.COMMANDS[13] + " should not equal the command \n" + Command.COMMANDS[14] + ".", c1, c2);
	}
	
	@Test
	public void sameClassesEqual()
	{
		String cmd = "" + Command.COMMANDS[3] + " class1 \"another class\"";
		Command c1 = Command.parseCommand(cmd);
		Command c2 = Command.parseCommand(cmd);
		assertEquals("Two identical commands (" + cmd + ") should be equal.", c1, c2);
	}
	
	@Test
	public void differenArgClassesUnequal()
	{
		String cmd1 = "" + Command.COMMANDS[3] + " class1 \"another class\"";
		String cmd2 = "" + Command.COMMANDS[3] + " class1 \"an entirely different class\"";
		Command c1 = Command.parseCommand(cmd1);
		Command c2 = Command.parseCommand(cmd2);
		assertNotEquals("The command \n" + cmd1 + "\n should not equal the command \n" + cmd2 + ".\n", c1, c2);
	}
	
	@Test
	public void differenArgClassesUnequal2()
	{
		String cmd1 = "" + Command.COMMANDS[3] + " class1 \"another class\"";
		String cmd2 = "" + Command.COMMANDS[3] + " class800 \"another class\"";
		Command c1 = Command.parseCommand(cmd1);
		Command c2 = Command.parseCommand(cmd2);
		assertNotEquals("The command \n" + cmd1 + "\n should not equal the command \n" + cmd2 + ".\n", c1, c2);
	}
	
	@Test
	public void differenArgClassesUnequal3()
	{
		String cmd1 = "" + Command.COMMANDS[3] + " class1 class2";
		String cmd2 = "" + Command.COMMANDS[3] + " class3";
		Command c1 = Command.parseCommand(cmd1);
		Command c2 = Command.parseCommand(cmd2);
		assertNotEquals("The command \n" + cmd1 + "\n should not equal the command \n" + cmd2 + ".\n", c1, c2);
	}
	
	@Test
	public void differenArgClassesUnequal4()
	{
		String cmd1 = "" + Command.COMMANDS[3] + " class1 class2";
		String cmd2 = "" + Command.COMMANDS[3];
		Command c1 = Command.parseCommand(cmd1);
		Command c2 = Command.parseCommand(cmd2);
		assertNotEquals("The command \n" + cmd1 + "\n should not equal the command \n" + cmd2 + ".\n", c1, c2);
	}
	
	
	// --------------------- TO STRING ---------------------
	
	@Test
	public void toStringWorks()
	{
		String cmd = Command.COMMANDS[1] + " \"  -_- __- my beautiful class name -__ -_-  \"";
		Command c = Command.parseCommand(cmd);
		assertEquals("The command " + cmd + " should produce the correct toString().\n", Action.REMOVE_CLASS + " [  -_- __- my beautiful class name -__ -_-  ]", c.toString());
	}

    
	// --------------------- FROM CHATGPT ---------------------

	@Test
	public void addRelationshipAction() {
	    String cmd = Command.COMMANDS[3] + " class1 class2"; // "add relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the ADD_RELATIONSHIP enum.", c.action, Action.ADD_RELATIONSHIP);
	}

	@Test
	public void addRelationshipArgCount() {
	    String cmd = Command.COMMANDS[3] + " class1 class2"; // "add relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
	}

	@Test
	public void addRelationshipArgs() {
	    String cmd = Command.COMMANDS[3] + " class1 class2"; // "add relationship"
	    String[] expected = {"class1", "class2"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
	}

	@Test
	public void renameClassAction() {
	    String cmd = Command.COMMANDS[2] + " class1 newClassName"; // "rename class"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the RENAME_CLASS enum.", c.action, Action.RENAME_CLASS);
	}

	@Test
	public void renameClassArgCount() {
	    String cmd = Command.COMMANDS[2] + " class1 newClassName"; // "rename class"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
	}

	@Test
	public void renameClassArgs() {
	    String cmd = Command.COMMANDS[2] + " class1 newClassName"; // "rename class"
	    String[] expected = {"class1", "newClassName"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
	}

	@Test
	public void saveAction() {
	    String cmd = Command.COMMANDS[5]; // "save"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the SAVE enum.", c.action, Action.SAVE);
	}

	@Test
	public void saveArgCount() {
	    String cmd = Command.COMMANDS[5]; // "save"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with no arguments.", 0, c.arguments.length);
	}

	@Test
	public void loadAction() {
	    String cmd = Command.COMMANDS[6] + " filePath"; // "load"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the LOAD enum.", c.action, Action.LOAD);
	}

	@Test
	public void loadArgCount() {
	    String cmd = Command.COMMANDS[6] + " filePath"; // "load"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with one argument.", 1, c.arguments.length);
	}

	@Test
	public void loadArg() {
	    String cmd = Command.COMMANDS[6] + " filePath"; // "load"
	    String expected = "filePath";
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct argument.", expected, c.arguments[0]);
	}
	
	
	// --------------------- USING SHORTHAND COMMANDS ---------------------
    
	@Test
	public void addRelationshipActionShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[3] + " class1 class2"; // "add relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the ADD_RELATIONSHIP enum.", c.action, Action.ADD_RELATIONSHIP);
	}

	@Test
	public void addRelationshipArgCountShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[3] + " class1 class2"; // "add relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
	}

	@Test
	public void addRelationshipArgsShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[3] + " class1 class2"; // "add relationship"
	    String[] expected = {"class1", "class2"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
	}

	@Test
	public void renameClassActionShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[2] + " class1 newClassName"; // "rename class"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the RENAME_CLASS enum.", c.action, Action.RENAME_CLASS);
	}

	@Test
	public void renameClassArgCountShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[2] + " class1 newClassName"; // "rename class"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
	}

	@Test
	public void renameClassArgsShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[2] + " class1 newClassName"; // "rename class"
	    String[] expected = {"class1", "newClassName"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
	}

	@Test
	public void saveActionShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[5]; // "save"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the SAVE enum.", c.action, Action.SAVE);
	}

	@Test
	public void saveArgCountShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[5]; // "save"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with no arguments.", 0, c.arguments.length);
	}

	@Test
	public void loadActionShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[6] + " filePath"; // "load"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the LOAD enum.", c.action, Action.LOAD);
	}

	@Test
	public void loadArgCountShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[6] + " filePath"; // "load"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with one argument.", 1, c.arguments.length);
	}

	@Test
	public void loadArgShorthand() {
	    String cmd = Command.COMMANDS_SHORTHAND[6] + " filePath"; // "load"
	    String expected = "filePath";
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return the correct argument.", expected, c.arguments[0]);
	}
	
}
