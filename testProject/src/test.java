
import java.util.Scanner;

/*
 * The most important class, the class with main in it.
 */
public class test {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        try (Scanner console = new Scanner (System.in)){
            String input = console.nextLine();
            System.out.println(input); //what is this?
        }
    }
}

