import java.util.Scanner;

/*
 * The most important class, the class with main in it.
 */
public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");

        try (Scanner sc = new Scanner (System.in)) {
            System.out.println("You typed: \"" + sc.nextLine() +"\"");
        }
    }
}
