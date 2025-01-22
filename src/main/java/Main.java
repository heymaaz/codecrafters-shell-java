import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        while( true ) {
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            // shouldExitIfRequested(input);
            if (input.equals("exit 0")) {
                break;
            }
            if(input.startsWith("echo "))
            {
                System.out.println(input.replace("echo ", ""));
                continue;
            }
            System.out.println(input+": command not found");
        } 
        
    }
}
