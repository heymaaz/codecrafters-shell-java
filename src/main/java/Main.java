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
            if(input.startsWith("type "))
            {
                input = input.substring(4).trim();
                if(input.equals("echo") || input.equals("exit") || input.equals("type")) {
                    System.out.println(input+" is a shell builtin");
                } 
                else {
                    System.out.println(input+": not found");
                }
                continue;
            }
            System.out.println(input+": command not found");
        } 
        
    }
}
