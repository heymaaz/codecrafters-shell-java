import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        List<String> builtIns = builtIns();
        String cwd = Paths.get("").toAbsolutePath().toString();

        while( true ) {
            System.out.print("$ ");

            String input = scanner.nextLine();
            String[] str = input.split(" ");
            String command = str[0];
            String parameter = "";
            boolean insideSingleQuote = false;
            boolean insideDoubleQuote = false;
            char prev = '\u0000';
            
            parameter = input.substring(command.length()).trim();
            switch (command) {
                case "exit":
                    if(parameter.equals("0")) {
                        System.exit(0);
                    }
                    else {
                        System.out.println(input+": command not found");
                    }
                    break;
                case "pwd":
                    if(parameter.equals("")) {
                        System.out.println(cwd);
                    }
                    else {
                        System.out.println("pwd: too many arguments");
                    }
                    break;
                case "cd":
                    if(parameter.equals("~")) {
                        cwd = System.getenv("HOME").toString();
                        break;
                    }
                    String tmpDir = cwd;
                    String[] parts = parameter.split("/");
                    for(String part:parts) {
                        if(part.equals("..")) {
                            tmpDir = tmpDir.substring(0,tmpDir.lastIndexOf('/'));
                        }
                        else if(part.equals(".")) {
                            //do nothing
                        }
                        else if(part.equals("")) {
                            tmpDir = "";
                        }
                        else {
                            tmpDir = tmpDir.concat("/").concat(part);
                        }
                    }
                    
                    Path newPath = Paths.get(tmpDir);
                    if(newPath.toFile().isDirectory()) {
                        cwd=tmpDir;
                    }
                    else {
                        System.out.println("cd: "+tmpDir+": No such file or directory");
                    }
                    break;
                case "echo":
                    for(int i = 0; i<parameter.length(); i++) {
                        char c = parameter.charAt(i);
                        if(c=='\"') {
                            insideDoubleQuote = !insideDoubleQuote;
                            prev = c;
                            continue;
                        }
                        if(!insideDoubleQuote) {
                            if(c=='\''){
                                insideSingleQuote = !insideSingleQuote;
                                continue;
                            }
                            if(c==' ' && !insideSingleQuote){
                                if(prev == ' ')
                                    continue;
                                else{
                                    prev = ' ';
                                }
                            }
                            prev = c;
                        }
                        System.out.print(c);
                    }
                    System.out.println();
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
                            String fullCommand = command + " " + parameter;
                            // System.out.println(fullCommand);
                            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", fullCommand});
                            process.getInputStream().transferTo(System.out);
                        } else {
                            System.out.println(command + ": command not found");
                        }
                    }
                    else {
                        System.out.println(input+": command not found");
                    }
            }
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
        builtIns.add("pwd");
        builtIns.add("cd");
        builtIns.add("type");
        builtIns.add("exit");
        return builtIns;
    }
}
