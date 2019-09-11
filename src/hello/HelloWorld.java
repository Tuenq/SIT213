package hello;

/**
 * HelloWorld program
 */
public class HelloWorld {
    public static void main(String[] args) {
        if (args.length > 0) {
            String output = "Hello " + String.join(" ", args) + " !";
            System.out.println(output);
        } else {
            System.out.println("Hello World !");
        }
    }
}