/**
 * The Main class for the entire UML editor program.
 * @author Lincoln Craddock
 */
public class Main {

    public static void main(String[] args) {
        if (hasCLIFlag(args)){
            Controller controller = new Controller(new CLIView(), new JModel());
            controller.run();
        } else {
			// NEEDS TO BE UPDATED TO LAUNCH GUI AND NOT CLI
            Controller controller = new Controller(new CLIView(), new JModel());
            controller.run();
        }
    }

	public static boolean hasCLIFlag(String[] args) {
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--cli")) {
				return true;
			}
		}
		return false;
	}
}