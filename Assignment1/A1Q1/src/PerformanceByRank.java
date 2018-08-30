// Mark Cantuba
// MJC862
// 11214496

import java.io.FileNotFoundException;
import java.io.File;
import java.util.Scanner;

import lib280.list.LinkedIterator280;
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

            // Place the new Crew instance in the linked list.
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
        LinkedList280<Crew> pirateCrew = readCrewData("C:\\Users\\Mark\\Documents\\Uni Stuff\\2nd Year Stuff" +
                "\\Term 2\\CMPT280\\Assignments\\Assignment1\\A1Q1\\src\\piratecrew.txt");

        // Creates an Array of 10 LinkedList280<Crew> Objects
        LinkedList280<Crew>[] piratesByRank = new LinkedList280[10];

        // Initializes each LinkedList280<Crew> Objects inside of piratesByRank Array.
        for (int i = 0; i < piratesByRank.length; i++) {
            piratesByRank[i] = new LinkedList280<>();
        }

        // Iterates through the pirateCrew LinkedList usingLinkedIterator280, starting from the first item of pirateCrew
        LinkedIterator280<Crew> iter = new LinkedIterator280<>(pirateCrew);
        while (iter.itemExists()) {
            /**
              * First, we get the item on the position, get the crew's rank, and get the r'th value from
              * our pirateByRank Array. Then, store the pirate information from pirateCrew to their corresponding rth
              * LinkedList280 from piratesByRank array.
              */
            piratesByRank[iter.item().getRank()].insert(iter.item());
            // Go to next item
            iter.goForth();
        }


         // For loop to access each of the LinkedList280 in piratesByRank array.
        for (int i = 0; i < piratesByRank.length ; i++) {
            // If the Linked list in index i is empty, print that there are no pirates in rank i
            if (piratesByRank[i].isEmpty()) {
                System.out.println("Jack has no pirates that are in rank " + i);
            }
            // If there are pirates in rank i
            else {
                // Variable containing the total sacks plundered by the entire crew in each rank.
                int totalSacksPlundered = 0;
                // total amount paid to all crew mates in each rank
                double totalPay = 0;
                // current rank being examined
                int rank;
                LinkedIterator280<Crew> iter2 = new LinkedIterator280(piratesByRank[i]);

                // Current rank/LinkedList being iterated
                rank = iter2.item().rank;


                // While the cursor is pointing at an item
                while (iter2.itemExists()) {
                    // Iterates through the list, then increment totalSacksPlundered by the number of grain sacks
                    // Each individual crew member obtained.
                    totalSacksPlundered += iter2.item().getGrainSacks();
                    totalPay += iter2.item().getPay();
                    iter2.goForth();
                }

                // The total guineas paid per sack of grains
                double payPerSack = totalPay/(double) totalSacksPlundered;

                // Prints out a message containing pirate's rank, and the total amount of guineas the crews of the rank
                // obtained all together.
                System.out.println("Jack's rank " + rank + " crew members were paid guineas of " +
                        payPerSack + " per sack of grain plundered!");
            }
        }
    }
}

