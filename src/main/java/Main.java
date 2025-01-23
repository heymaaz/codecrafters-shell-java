import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> builtIns = builtIns();

        while( true ) {
            System.out.print("$ ");

            String input = scanner.nextLine();

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
                if( builtIns.contains(input) ) {
                    System.out.println(input+" is a shell builtin");
                } 
                else {
                    String path = getPath(input);
                    if( path != null ) {
                        System.out.println(input+" is "+path);
                    }
                    else {
                        System.out.println(input+": not found");
                    }
                }
                continue;
            }
            System.out.println(input+": command not found");
        } 
        scanner.close();
        
    }

    static String getPath(String parameter) {
        for (String path : System.getenv("PATH").split(":")) {
            Path fullPath = Path.of(path, parameter);
            if (Files.isRegularFile(fullPath)) {
            return fullPath.toString();
            }
        }
        return null;
    }

    static List<String> builtIns() {
        List<String> builtIns = new ArrayList<>();
        builtIns.add("echo");
        builtIns.add("type");
        builtIns.add("exit");
        return builtIns;
    }
}
