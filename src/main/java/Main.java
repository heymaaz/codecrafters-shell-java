import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        while( true ) {
            System.out.print("$ ");

            try (Scanner scanner = new Scanner(System.in)) {
                String input = scanner.nextLine();
                // shouldExitIfRequested(input);
                if (input.equals("exit 0")) {
                    break;
                }
                System.out.println(input+": command not found");
            } catch (Exception e) {
                // TODO: handle exception
            }
        } 
        
    }
    // static void shouldExitIfRequested(String input) {
    //     if(input.trim().toLowerCase().startsWith("exit")) {
    //         System.exit(0);
    //     }
    // }
}
