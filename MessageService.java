
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

//interface for the simple message service methods
public interface MessageService extends Remote {

    // Method to store a message for a given username
    void storeMessage(String username, String message) throws RemoteException;

    // Method to retrieve messages for a given username
    List<String> retrieveMessages(String username) throws RemoteException;
}
