import java.rmi.Naming;
import java.util.List;
import java.util.Scanner;


// Client application to interact with the MessageService
public class MessageClient {
    public static void main(String[] args) {
        try {
            MessageService messageService = (MessageService) Naming.lookup("rmi://localhost/MessageService");
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            // Store a message
            System.out.print("Enter a message to store: ");
            String message = scanner.nextLine();
            messageService.storeMessage(username, message);

            // Retrieve messages
            List<String> messages = messageService.retrieveMessages(username);
            System.out.println("Messages for " + username + ":");
            for (String msg : messages) {
                System.out.println(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
