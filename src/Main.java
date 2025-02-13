
/**
 * The Main class for the entire UNL editor program.
 */
public class Main {

	public static void main(String[] args) {
		
		Controller controller = new Controller(new CLIView(), new JModel());
		controller.run();
	}

}
