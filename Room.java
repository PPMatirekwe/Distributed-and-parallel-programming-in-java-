import org.jcsp.lang.*;

public class Room implements CSProcess {
    private final One2OneChannelInt roomChannel;
    private final One2OneChannelInt[] forkChannels;
    private final int numberOfPhilosophers;

    public Room(One2OneChannelInt roomChannel, One2OneChannelInt[] forkChannels, int numberOfPhilosophers) {
        this.roomChannel = roomChannel;
        this.forkChannels = forkChannels;
        this.numberOfPhilosophers = numberOfPhilosophers;
    }

    @Override
    public void run() {
        int philosophersInRoom = 0;

        // Create an array of guards for fork channels
        AltingChannelInputInt[] forkGuards = new AltingChannelInputInt[forkChannels.length];
        for (int i = 0; i < forkChannels.length; i++) {
            forkGuards[i] = forkChannels[i].in();
        }

        // Create a guard for the room channel
        AltingChannelInputInt roomGuard = roomChannel.in();

        // Combine guards for fork channels and room channel
        AltingChannelInputInt[] allGuards = new AltingChannelInputInt[forkGuards.length + 1];
        System.arraycopy(forkGuards, 0, allGuards, 0, forkGuards.length);
        allGuards[forkGuards.length] = roomGuard;

        Alternative alt = new Alternative(allGuards);

        while (true) {
            int selected = alt.priSelect();

            if (selected < forkChannels.length) {
                // Event: Fork picked up
                int forkId = forkChannels[selected].in().read();
                System.out.println("Philosopher picked up Fork " + forkId);
            } else {
                // Event: ENTER or LEAVE
                int eventValue = roomChannel.in().read();
                Event event = (eventValue == Event.ENTER.ordinal()) ? Event.ENTER : Event.LEAVE;

                if (event == Event.ENTER) {
                    if (philosophersInRoom < numberOfPhilosophers - 1) {
                        philosophersInRoom++;
                        System.out.println("Philosopher entered. Total in room: " + philosophersInRoom);
                    } else {
                        System.out.println("Room is full. Philosopher waiting.");
                    }
                } else if (event == Event.LEAVE) {
                    philosophersInRoom--;
                    System.out.println("Philosopher left. Total in room: " + philosophersInRoom);
                }
            }
        }
    }
}
