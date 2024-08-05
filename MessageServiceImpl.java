import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

// Implementation of the RMI interface
public class MessageServiceImpl extends UnicastRemoteObject implements MessageService {

    // Map to store messages for each username
    private final Map<String, List<String>> messageStore;

    // Constructor to initialize the message store
    protected MessageServiceImpl() throws RemoteException {
        super();
        this.messageStore = new HashMap<>();
    }

    // Method to store a message for a given username
    @Override
    public synchronized void storeMessage(String username, String message) throws RemoteException {
        // Ensure thread safety when modifying the messageStore
        messageStore.computeIfAbsent(username, k -> new LinkedList<>()).add(message);
        System.out.println("Message stored for " + username);
    }

    // Method to retrieve messages for a given username
    @Override
    public synchronized List<String> retrieveMessages(String username) throws RemoteException {
        // Return the messages for the given username, or an empty list if none exist
        return messageStore.getOrDefault(username, new LinkedList<>());
    }

    // Main method to start the RMI server
    public static void main(String[] args) {
        try {
            // Create an instance of the message service
            MessageService messageService = new MessageServiceImpl();
            
            // Create RMI registry and bind the service to a name
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("MessageService", messageService);
            
            System.out.println("MessageService is ready.");
        } catch (Exception e) {
            // Print any exceptions that occur during server initialization
            e.printStackTrace();
        }
    }
}
