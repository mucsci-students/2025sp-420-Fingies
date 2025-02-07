import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class CommandTester {
	
	// --------------------- ADD CLASS ---------------------

	@Test
	public void addClassAction() {
	    String cmd = Command.COMMANDS[0] + " class1"; // Dynamically reference "add class"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the ADD_CLASS enum.", c.action, Action.ADD_CLASS);
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
	    int cnt = 2000;

	    // Dynamically reference the "add class" command
	    String cmd = Command.COMMANDS[0] + " ";
	    for (int i = 0; i < cnt; ++i)
	        cmd += "class" + i + " ";

	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return " + cnt + " arguments.", cnt, c.arguments.length);
	}

	@Test
	public void manyArgs() {
	    int cnt = 2000;

	    // Dynamically reference the "add class" command
	    String cmd = Command.COMMANDS[0] + " ";
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

    
	// --------------------- REMOVE RELATIONSHIP W/ QUOTES ---------------------

	@Test
	public void removeRelationshipAction() {
	    String cmd = Command.COMMANDS[4] + " class1 class2"; // Dynamically reference "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the REMOVE_RELATIONSHIP enum.", c.action, Action.REMOVE_RELATIONSHIP);
	}

	@Test
	public void removeRelationshipArgCount() {
	    String cmd = Command.COMMANDS[4] + " class1 class2"; // Dynamically reference "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with 2 arguments.", 2, c.arguments.length);
	}

	@Test
	public void removeRelationshipArg() {
	    String cmd = Command.COMMANDS[4] + " class1 class2"; // Dynamically reference "remove relationship"
	    String[] expected = {"class1", "class2"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[0] + "\".", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[1] + "\".", expected[1], c.arguments[1]);
	}

	@Test
	public void removeRelationshipWithQuotesAction() {
	    String cmd = Command.COMMANDS[4] + " \"class1\" class2"; // Dynamically reference "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the REMOVE_RELATIONSHIP enum.", c.action, Action.REMOVE_RELATIONSHIP);

	    cmd = Command.COMMANDS[4] + " class1 \"class2\""; // Dynamically reference "remove relationship"
	    c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the REMOVE_RELATIONSHIP enum.", c.action, Action.REMOVE_RELATIONSHIP);
	}

	@Test
	public void removeRelationshipWithQuotesArgCount() {
	    String cmd = Command.COMMANDS[4] + " \"class1\" class2"; // Dynamically reference "remove relationship"
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with 2 arguments.", 2, c.arguments.length);

	    cmd = Command.COMMANDS[4] + " class1 \"class2\""; // Dynamically reference "remove relationship"
	    c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with 2 arguments.", 2, c.arguments.length);
	}

	@Test
	public void removeRelationshipWithQuotesArg() {
	    String cmd = Command.COMMANDS[4] + " \"class1\" class2"; // Dynamically reference "remove relationship"
	    String[] expected = {"class1", "class2"};
	    Command c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[0] + "\".", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[1] + "\".", expected[1], c.arguments[1]);

	    cmd = Command.COMMANDS[4] + " class1 \"class2\""; // Dynamically reference "remove relationship"
	    c = Command.parseCommand(cmd);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[0] + "\".", expected[0], c.arguments[0]);
	    assertEquals("Parsing the command \n" + cmd + "\n should return a command with the argument \"" + expected[1] + "\".", expected[1], c.arguments[1]);
	}

    
 // --------------------- WITH LONG ARGUMENTS ---------------------
    
    @Test
    public void longRandomArgsCount()
    {
    	int cnt = 3;
    	int len = 500;
    	String chars = "qwertyQWERTY1230   '[]{}|!@#$%^&*()_+/?,.<>-=";
    	
    	String cmd = "add class ";
    	String[] expected = new String[cnt];
    	for(int j = 0; j < cnt; ++j)
    	{
    		expected[j] = "";
    		for(int i = 0; i < len; ++i)
    			expected[j] += chars.charAt((int)(Math.random() * chars.length()));
    		cmd += "\"" + expected[j] + "\" ";
    	}
    	    	
    	Command c = Command.parseCommand(cmd);
       	assertEquals("Parsing a long command with random args should return " + cnt + " arguments.", cnt, c.arguments.length);
    }
    
    @Test
    public void longRandomArgs()
    {
    	int cnt = 3;
    	int len = 500;
    	String chars = "qwertyQWERTY1230   '[]{}|!@#$%^&*()_+/?,.<>-=";
    	
    	String cmd = "add class ";
    	String[] expected = new String[cnt];
    	for(int j = 0; j < cnt; ++j)
    	{
    		expected[j] = "";
    		for(int i = 0; i < len; ++i)
    			expected[j] += chars.charAt((int)(Math.random() * chars.length()));
    		cmd += "\"" + expected[j] + "\" ";
    	}
    	
    	Command c = Command.parseCommand(cmd);
    	for(int i = 0; i < cnt; ++i)
    		assertEquals("Parsing a long command with random args should return a command with the argument \"" + expected[i] + "\".", expected[i], c.arguments[i]);
    }
    
    // --------------------- FROM CHATGPT ---------------------
    
    @Test
    public void addRelationshipAction() {
        String cmd = "add relationship class1 class2";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the ADD_RELATIONSHIP enum.", c.action, Action.ADD_RELATIONSHIP);
    }

    @Test
    public void addRelationshipArgCount() {
        String cmd = "add relationship class1 class2";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
    }

    @Test
    public void addRelationshipArgs() {
        String cmd = "add relationship class1 class2";
        String[] expected = {"class1", "class2"};
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
    }

    @Test
    public void renameClassAction() {
        String cmd = "rename class class1 newClassName";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the RENAME_CLASS enum.", c.action, Action.RENAME_CLASS);
    }

    @Test
    public void renameClassArgCount() {
        String cmd = "rename class class1 newClassName";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
    }

    @Test
    public void renameClassArgs() {
        String cmd = "rename class class1 newClassName";
        String[] expected = {"class1", "newClassName"};
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
    }

    @Test
    public void saveAction() {
        String cmd = "save";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the SAVE enum.", c.action, Action.SAVE);
    }

    @Test
    public void saveArgCount() {
        String cmd = "save";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with no arguments.", 0, c.arguments.length);
    }

    @Test
    public void loadAction() {
        String cmd = "load filePath";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the LOAD enum.", c.action, Action.LOAD);
    }

    @Test
    public void loadArgCount() {
        String cmd = "load filePath";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with one argument.", 1, c.arguments.length);
    }

    @Test
    public void loadArg() {
        String cmd = "load filePath";
        String expected = "filePath";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct argument.", expected, c.arguments[0]);
    }
    
    // --------------------- SHORTHAND VERSIONS FROM CHATGPT ---------------------
    
    @Test
    public void shortAddRelationshipAction() {
        String cmd = "addr class1 class2";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the ADD_RELATIONSHIP enum.", c.action, Action.ADD_RELATIONSHIP);
    }

    @Test
    public void shortAddRelationshipArgCount() {
        String cmd = "addr class1 class2";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
    }

    @Test
    public void shortAddRelationshipArgs() {
        String cmd = "addr class1 class2";
        String[] expected = {"class1", "class2"};
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
    }

    @Test
    public void shortRenameClassAction() {
        String cmd = "rnc class1 newClassName";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the RENAME_CLASS enum.", c.action, Action.RENAME_CLASS);
    }

    @Test
    public void shortRenameClassArgCount() {
        String cmd = "rnc class1 newClassName";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with two arguments.", 2, c.arguments.length);
    }

    @Test
    public void shortRenameClassArgs() {
        String cmd = "rnc class1 newClassName";
        String[] expected = {"class1", "newClassName"};
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[0], c.arguments[0]);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct arguments.", expected[1], c.arguments[1]);
    }

    @Test
    public void shortSaveAction() {
        String cmd = "sv";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the SAVE enum.", c.action, Action.SAVE);
    }

    @Test
    public void shortSaveArgCount() {
        String cmd = "sv";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with no arguments.", 0, c.arguments.length);
    }

    @Test
    public void shortLoadAction() {
        String cmd = "ld filePath";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with the LOAD enum.", c.action, Action.LOAD);
    }

    @Test
    public void shortLoadArgCount() {
        String cmd = "ld filePath";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return a command with one argument.", 1, c.arguments.length);
    }

    @Test
    public void shortLoadArg() {
        String cmd = "ld filePath";
        String expected = "filePath";
        Command c = Command.parseCommand(cmd);
        assertEquals("Parsing the command \n" + cmd + "\n should return the correct argument.", expected, c.arguments[0]);
    }


	
}
