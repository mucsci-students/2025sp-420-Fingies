package org.fingies.View;

import java.util.List;

import javax.swing.JComponent;

import org.fingies.Controller.UMLController;

/**
 * An interface for the view component of the UML editor, which could be a command line interface,
 * a graphical user interface, or any other kind of front-end for the user.
 * 
 * The methods here are supposed to represent general actions that the program might want to
 * take to interact with the user (such as ask them for input, or return to them the result of
 * their action). They do NOT specify how the user will interact with the program -- that is
 * the job of any implementations of this interface.
 * 
 * @author Lincoln Craddock
 */
public interface UMLView {
	
	void run();
	
	/**
	 * Displays a message to the user asking for a filepath to save to.
	 * 
	 * @param message The message to display to the user, prompting them for a filepath to save to.
	 * @return The filepath provided by the user, or null if none was provided.
	 */
	String promptForSaveInput(String message);
	
	
	/**
	 * Displays a message to the user asking for a filepath to open.
	 * 
	 * @param message The message to display to the user, prompting them for a filepath to open.
	 * @return The filepath provided by the user, or null if none was provided.
	 */
	String promptForOpenInput(String message);
	
	/**
	 * Displays a message to the user and stalls the program until they respond.
	 * 
	 * In a CLI the user might recieve a message they can type an answer to.
	 * 
	 * In a GUI they might recieve a pop-up window with the prompt and a field for them to
	 * type in.
	 * 
	 * Returns their response as a string.
	 * 
	 * @param message The message to display to the user, prompting them for input.
	 * @return A string representation of the user's input.
	 */
	String promptForInput(String message);
	
	/**
	 * Displays multiple messages to the user and stalls the program until they respond to
	 * all of them.
	 * 
	 * In a CLI the user might recieve the prompts one at a time
	 * 
	 * In a GUI they might recieve a pop-up window with fields for them to respond to all of
	 * the prompts.
	 * 
	 * Returns a list of their responses.
	 * 
	 * @param messages The messages to display to the user, prompting them for input.
	 * @return The user's answer to each question.
	 */
	List<String> promptForInput(List<String> messages);
	
	/**
	 * Displays multiple messages to the user one at a time, and stalls the program
	 * until they respond to all of them. Only allows users to enter values that
	 * statisfy the InputCheck for that prompt.
	 * 
	 * In a CLI the view might keep prompting the user until they answer a prompt in
	 * a way that satisfies the InputCheck.
	 * 
	 * In a GUI they might not be able to hit the submit button until each of their
	 * responses are accepted.
	 * 
	 * Returns a list of their responses that were accepted.
	 * 
	 * @param messages The messages to display to the user, prompting them for input.
	 * @param checks A list of checks representing the what sort of responses should be accepted.
	 * @return The user's answer to each question.
	 */
	List<String> promptForInput(List<String> messages, List<InputCheck> checks); // this will probably remain unimplemented until we need it.
	
	/**
	 * Gives the user feedback to let them know that their most recent command was successful.
	 * 
	 * In a CLI their most recent command might get a checkmark next to it, or turn green.
	 * 
	 * In a GUI the visual UML diagram might get updated, or stay the same if their action involved
	 * manipulating the diagram (such as dragging an arrow from one class to another in order to
	 * create a relationship).
	 */
	void notifySuccess();
	
	/**
	 * Tells the user that their most recent command was successful.
	 * 
	 * In a CLI they might recieve a message giving more information about the result of their command,
	 * and their most recent command might get a checkmark next to it, or turn green.
	 * 
	 * In a GUI a notification might appear breifly giving more information about the result of their action.
	 * The visual UML diagram might also get updated, or stay the same if their action involved manipulating
	 * the diagram (such as dragging an arrow from one class to another in order to
	 * create a relationship).
	 */
	void notifySuccess(String message);
	
	/**
	 * Tells the user that their most recent command failed.
	 * 
	 * In a CLI the user should probably recieve a message explaining what went wrong. Their most
	 * recent command might also turn red, or get an 'X' next to it.
	 * 
	 * In a GUI the visual UML diagram might revert to the way it was before the user manipulated it
	 * (for example, if they tried dragging an arrow from one class to another when those two already
	 * had a relationship between them). It should probably also give the user a quick notification
	 * explaining why their action failed.
	 * 
	 * @param message A message explaining why the user's most recent command failed.
	 */
	void notifyFail(String message);
	
	/**
	 * Displays a message to the user. This can be used to show the user a list of classes (such as from
	 * the "list classes" command), print a list of commands and their uses, warn them that their project
	 * is still unsaved, or anything else.
	 * 
	 * In a CLI the message might just be a line of text that gets printed.
	 * 
	 * In a GUI a window might pop up with the message inside of it.
	 * 
	 * @param message The information to convey to the user.
	 */
	void display(String message);
	
	/**
	 * Tells the user how to use the program.
	 */
	void help();
	
	/**
	 * Tells the user how to use a specific command.
	 */
	void help(String command);
	
	/**
	 * Gives the View a Controller to send user input to.
	 * 
	 * @param c The Controller for the View to interact with.
	 */
	public void setController(UMLController c);
	
	
	/**
	 * Displays a message to the user and stalls the program until they respond.
	 * 
	 * In a CLI the user might receive a message they can only type y or n to
	 * 
	 * In a GUI they might receive a pop-up window with the prompt and yes or no buttons to choose between
	 * 
	 * Returns their response as a string.
	 * 
	 * @param message The message to display to the user, prompting them for input.
	 * @param title The title of the pop up window
	 * @return A int representing the choice of the user, 0 is yes, 1 is no, 2 is cancel
	 */
	int promptForYesNoInput(String message, String title);
	
	/**
	 * Creates a representation of the diagram as a JComponent and returns it, or returns
	 * an existing representation if it has one.
	 * 
	 * @return A JComponent that represents the entire diagram.
	 */
	public JComponent getJComponentRepresentation();
	
}
