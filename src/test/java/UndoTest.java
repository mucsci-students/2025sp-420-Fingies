import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.fingies.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UndoTest {
	
	@Before
	public void setUp()
	{
		// 
		View view = new View() {
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

        Controller controller = new Controller(view, new JModel());
	    view.setController(controller);
	    
	}
	
	@Test
	public void addClass()
	{
		
	}

}
