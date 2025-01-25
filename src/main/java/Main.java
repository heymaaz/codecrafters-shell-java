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
            String[] str = input.split(" ");
            String command = str[0];
            String parameter = "";

            for(int i = 1; i<str.length; i++) {
                parameter = parameter+" "+str[i];
            }
            parameter = parameter.trim();

            switch (command) {
                case "exit":
                    if(parameter.equals("0")) {
                        System.exit(0);
                    }
                    else {
                        System.out.println(input+": command not found");
                    }
                    break;
                case "echo":
                    System.out.println(parameter);
                    break;
                case "type":
                    if( builtIns.contains(parameter) ) {
                        System.out.println(parameter+" is a shell builtin");
                    } 
                    else {
                        String path = getPath(parameter);
                        if( path != null ) {
                            System.out.println(parameter+" is "+path);
                        }
                        else {
                            System.out.println(parameter+": not found");
                        }
                    }
                    break;
                default:
                    if( !parameter.equals("") ) {
                        String path = getPath(command);
                        if(path != null) {
                            String[] fullPath = new String[]{command, parameter};
                            Process process = Runtime.getRuntime().exec(fullPath);
                            process.getInputStream().transferTo(System.out);
                        } else {
                            System.out.println(command + ": command not found");
                        }
                    }
                    else {
                        System.out.println(input+": command not found");
                    }
            }


        //     if (input.equals("exit 0")) {
        //         break;
        //     }
        //     if(input.startsWith("echo "))
        //     {
        //         System.out.println(input.replace("echo ", ""));
        //         continue;
        //     }
        //     if(input.startsWith("type "))
        //     {
        //         input = input.substring(4).trim();
        //         if( builtIns.contains(input) ) {
        //             System.out.println(input+" is a shell builtin");
        //         } 
        //         else {
        //             String path = getPath(input);
        //             if( path != null ) {
        //                 System.out.println(input+" is "+path);
        //             }
        //             else {
        //                 System.out.println(input+": not found");
        //             }
        //         }
        //         continue;
        //     }
        //     System.out.println(input+": command not found");
        }
        
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
