import org.jcsp.lang.*;

class Philosopher implements CSProcess {
      // Unique identifier for the philosopher
    private final int id;
    // Channels for communication with the room and neighboring forks
    private final One2OneChannelInt roomChannel;
    private final One2OneChannelInt leftForkChannel;
    private final One2OneChannelInt rightForkChannel;
    
      // Constructor to initialize the philosopher with channels
    public Philosopher(int id, One2OneChannelInt roomChannel,
                       One2OneChannelInt leftForkChannel, One2OneChannelInt rightForkChannel) {
        this.id = id;
        this.roomChannel = roomChannel;
        this.leftForkChannel = leftForkChannel;
        this.rightForkChannel = rightForkChannel;
    }


    // Run method to simulate the behavior of the philosopher 
    @Override
    public void run() {
        while (true) {
            System.out.println("Philosopher " + id + " thinking.");
            sleep(2000); // Simulate thinking time

            System.out.println("Philosopher " + id + " trying to enter the room.");
            roomChannel.out().write(Event.ENTER.ordinal()); // Write the ordinal value of Event.ENTER

            System.out.println("Philosopher " + id + " waiting for forks.");
            int leftForkId = leftForkChannel.in().read();
            int rightForkId = rightForkChannel.in().read();

            System.out.println("Philosopher " + id + " eating.");
            sleep(3000); // Simulate eating time

            System.out.println("Philosopher " + id + " releasing forks and leaving the room.");
            leftForkChannel.out().write(leftForkId);
            rightForkChannel.out().write(rightForkId);
            roomChannel.out().write(Event.LEAVE.ordinal()); // Write the ordinal value of Event.LEAVE
        }
    }

    private void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
