import java.io.IOException;
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

        while( true ) {
            System.out.print("$ ");

            String input = scanner.nextLine().trim();
            String[] str = input.split(" ");
            String command = str[0];
            String parameter = "";
            
            if( input.charAt(0)=='\'' ) {
                int index = 1+input.substring(1).indexOf('\'');
                command = input.substring(0,index+2);
            }

            if( input.charAt(0)=='\"' ) {
                int index = 1+input.substring(1).indexOf('\"');
                command = input.substring(0,index+2);
            }

            parameter = input.substring(command.length()).trim();

            switchCommands(input, command, parameter, builtIns);
        }
    }
        
    static void switchCommands(String input, String command, String parameter, List<String> builtIns) {
        String currentDir = System.getProperty("user.dir");
        switch (command) {
            case "exit":
                handleExit(input, parameter);
                break;

            case "pwd":
                handlePWD(currentDir, parameter);
                break;

            case "cd":
                handleCD(currentDir, parameter);
                break;

            case "echo":
                handleEcho(parameter);
                break;

            case "type":
                handleType(builtIns, parameter);
                break;

            default:
                if( !parameter.equals("") ) {
                    handlePathCommands(command, parameter);
                }
                else {
                    System.out.println(input+": command not found");
                }
        }
    }
        
    

    static void handleExit(String input, String parameter) {
        if(parameter.equals("0")) {
            System.exit(0);
        }
        else {
            System.out.println(input+": command not found");
        }
    }
    
    static void handlePWD(String currentDir, String parameter) {
        if(parameter.equals("")) {
            System.out.println(currentDir);
        }
        else {
            System.out.println("pwd: too many arguments");
        }
    }
    
    static void handleCD(String currentDir, String parameter) {
        if(parameter.equals("~")) {
            System.setProperty("user.dir",System.getenv("HOME").toString());
            return;
        }
        String tmpDir = currentDir;
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
            System.setProperty("user.dir",tmpDir);
        }
        else {
            System.out.println("cd: "+tmpDir+": No such file or directory");
        }
    }
    
    static void handleEcho(String parameter) {
        boolean insideSingleQuote = false;
        boolean insideDoubleQuote = false;
        char prev = '\u0000';

        for(int i = 0; i<parameter.length(); i++) {
            char c = parameter.charAt(i);
            if(c=='\"') {
                if(insideSingleQuote) {
                    System.out.print("\"");
                    continue;
                }
                insideDoubleQuote = !insideDoubleQuote;
                prev = c;
                continue;
            }

            if(!insideDoubleQuote) {
                if(!insideSingleQuote && c=='\\' && i+1<parameter.length()) {
                    char next = parameter.charAt(i+1);
                    if(next==' ')
                        prev = ' ';
                    System.out.print(next);
                    i++;
                    continue;
                }

                if(c=='\'') {
                    insideSingleQuote = !insideSingleQuote;
                    continue;
                }

                if(c==' ' && !insideSingleQuote) {
                    if(prev == ' ')
                        continue;
                    else{
                        prev = ' ';
                    }
                }

                prev = c;
            }

            else {
                if(c=='\\' && i+1<parameter.length()) {
                    char next = parameter.charAt(i+1);
                    if(next==' ')
                        prev = ' ';
                    System.out.print(next);
                    i++;
                    continue;
                }
            }
            System.out.print(c);
        }
        System.out.println();
    }

    static void handleType(List<String> builtIns, String parameter) {
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
    }
    
    static void handlePathCommands(String command, String parameter) {
        String path = getPath(command);
        if(path != null) {
            String fullCommand = command + " " + parameter;
            Process process;
            try {
                process = Runtime.getRuntime().exec(new String[]{"sh", "-c", fullCommand});
                process.getInputStream().transferTo(System.out);
            } 
            catch (IOException e) {
                e.printStackTrace();
            }
        } 
        else {
            System.out.println(command + ": command not found");
        }
    }

    static String getPath(String command) {
        if(command.charAt(0)=='\'' || command.charAt(0)=='\"')
            command=command.substring(1, command.length()-2).trim();
        for (String path : System.getenv("PATH").split(":")) {
            Path fullPath = Path.of(path, command);
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
