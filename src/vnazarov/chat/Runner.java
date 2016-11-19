package vnazarov.chat;

import java.util.Scanner;

public class Runner {
    
    private static final String CLIENT = "c";
    private static final String SERVER = "s";
    
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.println("Are you (c)lient or (s)erver?:");
        
        while(true){
            String output = input.nextLine();
            if(output.equals(CLIENT)){
                new Client();
                input.close();
                break;
            } else if(output.equals(SERVER)) {
                new Server();
                input.close();
                break;
            } else {
                System.out.println("Command is incorrect, retry");
            }
        }
    }
}
