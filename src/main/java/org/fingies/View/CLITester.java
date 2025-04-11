package org.fingies.View;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * A short test program for testing each of the methods in the CLIView class.
 * 
 * Enter the name of a method to test it.
 * @author Lincoln Craddock
 */
public class CLITester {
	
	public static final String[] methods = {"next command", "prompt for input", "prompt for input 2", "prompt for input 3", "notify success", "notify success 2", "notify fail", "display", "help", "help 2"};
	
	// Constants to be used in the program text to color it cyan
	public static final String COLOR = "\u001B[36m";
	public static final String RESET = "\u001B[0m";
	
	
	public static void main(String[] args)
	{
		Scanner sc = new Scanner (System.in);
		CLIView cli = new CLIView();
		Arrays.sort(methods); // sorted so that binary search can be used later
		
		System.out.println(COLOR + "CLIView Methods:\n"
				+ "next command\n"
				+ "prompt for input\n"
				+ "prompt for input 2\n"
				+ "prompt for input 3\n"
				+ "notify success\n"
				+ "notify success 2\n"
				+ "notify fail\n"
				+ "display" + RESET);
		while(true)
		{
			System.out.print(COLOR + "\nEnter the name of the method to test: \n> " + RESET);

			int idx = Arrays.binarySearch(methods, sc.nextLine());
			if (idx >= 0)
			{
				// call the method in CLIView
				if (methods[idx].equals("next command"))
				{
//					System.out.println(COLOR + "Result: " + RESET + cli.nextCommand());
				}
				else if (methods[idx].equals("prompt for input"))
				{
					System.out.println(COLOR + "Result: " + RESET + cli.promptForInput("*message*"));
				}
				else if (methods[idx].equals("prompt for input 2"))
				{
					System.out.println(COLOR + "Result: " + RESET + cli.promptForInput(List.of("*message1*", "*message2*")));
				}
				else if (methods[idx].equals("prompt for input 3"))
				{
					System.out.println(COLOR + "Result: " + RESET + cli.promptForInput(List.of("*yes/no question*", "*enter one letter please*"), List.of(
					
					new InputCheck () {

						@Override
						public String check(String t) {
							return t.equalsIgnoreCase("y") || t.equalsIgnoreCase("n") || t.equalsIgnoreCase("yes") || t.equalsIgnoreCase("no") ? "" : "Must be yes or no (y/n).";
						}},
							
					new InputCheck () {

						@Override
						public String check(String t) {
							return t.length() == 1 ? "" : "Please only enter one letter.";
						}}
					)));
				}
				else if (methods[idx].equals("notify success"))
				{
					cli.notifySuccess();
				}
				else if (methods[idx].equals("notify success 2"))
				{
					cli.notifySuccess("*message*");
				}
				else if (methods[idx].equals("notify fail"))
				{
					cli.notifyFail("*message*");
				}
				else if (methods[idx].equals("display"))
				{
					cli.display("*message*");
				}
				else if (methods[idx].equals("help"))
				{
					cli.help();
				}
				else if (methods[idx].equals("help 2"))
				{
					cli.help("addc");
				}
			}
			else
			{
				// the method entered does not exist
				System.out.println(COLOR + "Unknown method." + RESET);
			}
		}
	}
}
