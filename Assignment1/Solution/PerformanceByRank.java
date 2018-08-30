import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;
import lib280.list.LinkedList280;

public class PerformanceByRank {

    public static LinkedList280<Crew> readCrewData(String path) {
        Scanner infile = null;

        try {
            infile = new Scanner(new File(path));
        }
        catch (FileNotFoundException e) {
            System.out.println("Error: File not found!");
        }

        // Initialize output list.
        LinkedList280<Crew> pirateCrew = new LinkedList280<Crew>();

        // While there is more stuff to read...
        while(infile.hasNext()) {
            // Read the three values for a Crew record
            int rank = infile.nextInt();
            double pay = infile.nextDouble();
            int sacks = infile.nextInt();

            // Create a crew object from the data
            Crew c = new Crew(rank, pay, sacks);

            // Place the new Crew instnace in the linked list.
            pirateCrew.insertFirst(c);
        }

        // Close the input file like a good citizen. :)
        infile.close();

        // Return the list of Crew objects.
        return pirateCrew;
    }


    public static void main( String args[] ) {
        // Read the data for Jack's pirate crew.

        // If you are getting a "File Not Found" error here, you may adjust the
        // path to piratecrew.txt as needed.
        LinkedList280<Crew> pirateCrew = readCrewData("A1-PirateCrew/piratecrew.txt");

        // Create an array of linked lists.
        // Note the use of a "bare type" on the right where LinkedList280 has no generic type parameter.
        // This is another workaround to creating arrays with a generic type parameter which java does not allow.
        LinkedList280<Crew>[] piratesByRank = new LinkedList280[10];

        // *** Remember: you still have to initialize each array element ***


        // Initialize the array's linked lists.
        for(int i=0; i < 10; i++) {
            piratesByRank[i] = new LinkedList280<Crew>();
        }

        // Place each crew record into the appropriate list according to their rank.
        pirateCrew.goFirst();
        while(pirateCrew.itemExists()) {
            piratesByRank[pirateCrew.item().getRank()].insertFirst(pirateCrew.item());
            pirateCrew.goForth();
        }

        // Analyze the pirates of each rank.
        for(int i=0; i < 10; i++) {
            // If the list is non empty...
            if( !piratesByRank[i].isEmpty() ) {

                // Initialize variables to count total pay and total sacks of grain.
                double totalPay=0.0;
                int totalSacks = 0;

                // Iterate over the Crew objects in the list.
                piratesByRank[i].goFirst();
                while(piratesByRank[i].itemExists()) {
                    // Add this crew member's pay and sacks plundered to the running total.
                    totalPay += piratesByRank[i].item().getPay();
                    totalSacks += piratesByRank[i].item().getGrainSacks();

                    // Go to the next crew member.
                    piratesByRank[i].goForth();
                }

                // Print the result for this rank.
                System.out.println("Jack's rank " + i + " crew members were paid of " +
                        totalPay/totalSacks + " guineas per sack of grain plundered.");
            }
            // Otherwise the list was empty meaning there are no pirates at this rank.
            else {
                System.out.println("Jack employs no pirates of rank " + i + ".");
            }
        }


    }

}
