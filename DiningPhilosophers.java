import java.nio.channels.Channel;

import org.jcsp.lang.*;

// Dining Philosophers simulation using JCSP
public class DiningPhilosophers {
    public static void main(String[] args) {
        final int numberOfPhilosophers = 5;

        // Channel for the room to control access
        One2OneChannelInt roomChannel = Channel.one2oneInt();
        // Channels for each fork
        One2OneChannelInt[] forkChannels = new One2OneChannelInt[numberOfPhilosophers];
        for (int i = 0; i < numberOfPhilosophers; i++) {
            forkChannels[i] = Channel.one2oneInt();
        }

         // Array to hold processes for Philosophers and the Room
        CSProcess[] processes = new CSProcess[numberOfPhilosophers + 1]; // Philosophers + Room
         // Room process to control access to forks
        processes[0] = new Room(roomChannel, forkChannels, numberOfPhilosophers);
        for (int i = 1; i <= numberOfPhilosophers; i++) {
            processes[i] = new Philosopher(i, roomChannel, forkChannels[i - 1], forkChannels[i % numberOfPhilosophers]);
        }

        // Parallel execution
        Parallel par = new Parallel(processes);
        par.run();
    }
}
