import com.opencsv.CSVReader;
import lib280.base.Pair280;
import lib280.hashtable.KeyedChainedHashTable280;
import lib280.list.ArrayedListIterator280;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;
import lib280.tree.OrderedSimpleTree280;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

// This project uses a JAR called opencsv which is a library for reading and
// writing CSV (comma-separated value) files.
//
// You don't need to do this for this project, because it's already done, but
// if you want to use opencsv in other projects on your own, here's the process:
//
// 1. Download opencsv-3.1.jar from http://sourceforge.net/projects/opencsv/
// 2. Drag opencsv-3.1.jar into your project.
// 3. Right-click on the project in the package explorer, select "Properties" (at bottom of popup menu)
// 4. Choose the "Libraries" tab
// 5. Click "Add JARs"
// 6. Select the opencsv-3.1.jar from within your project from the list.
// 7. At the top of your .java file add the following imports:
//        import java.io.FileReader;
//        import com.opencsv.CSVReader;
//
// Reference documentation for opencsv is here:
// http://opencsv.sourceforge.net/apidocs/overview-summary.html


public class QuestLog extends KeyedChainedHashTable280<String, QuestLogEntry> {

	public QuestLog() {
		super();
	}

	/**
	 * Obtain an array of the keys (quest names) from the quest log.  There is
	 * no expectation of any particular ordering of the keys.
	 *
	 * @return The array of keys (quest names) from the quest log.
	 */
	public String[] keys() {
		String[] keys = new String[this.count()];	// Create a new string array to store in all key values, with
													// size as number of items in hashTable
		this.goFirst();	// Go to the first item in hash
		int counter = 0;
		while (this.itemExists()) {
			keys[counter] = this.itemKey();	// Store the key obtained from item() to keys array
			this.goForth();	// Go forward
			counter++;	// Go to the next item
		}
		Arrays.sort(keys);	// Sort the keys array
		return keys;
	}

	/**
	 * Format the quest log as a string which displays the quests in the log in
	 * alphabetical order by name.
	 *
	 * @return A nicely formatted quest log.
	 */
	public String toString() {
		// TODO Implement this method.
		String[] holder = this.keys();	// Get all the keys of this hashQuestLog
		String s = "";	// holder for the obtained keys:value pairs to string
		for(String key : holder) {	// For each key in holder array
			QuestLogEntry log = this.obtain(key);	// get the log entry for key k
			s += "Quest Name: " + log.getQuestName() + " | Location: " +	// add the obtained information to string holder s
					log.getQuestArea() + " | Required Level: Level " + log.getRecommendedMinLevel() +
					"-" + log.getRecommendedMaxLevel() + "\n";
		}
		return s;	// return created string
	}

	/**
	 * Obtain the quest with name k, while simultaneously returning the number of
	 * items examined while searching for the quest.
	 * @param k Name of the quest to obtain.
	 * @return A pair in which the first item is the QuestLogEntry for the quest named k, and the
	 *         second item is the number of items examined during the search for the quest named k.
	 *         Note: if no quest named k is found, then the first item of the pair should be null.
	 */
	public Pair280<QuestLogEntry, Integer> obtainWithCount(String k) {
		// TODO Implement this method.

		// Write a method that returns a Pair280 which contains the quest log entry with name k,
		// and the number QuestLogEntry objects that were examined in the process.  You need to write
		// this method from scratch without using any of the superclass methods (mostly because
		// the superclass methods won't be terribly useful unless you can modify them, which you
		// aren't allowed to do!)
		int count = 0;	// Number of items checked before reaching our item or end if k doesn't exist
		int hashPosition = hashPos(k);	// convert k into its corresponding array index
		LinkedList280<QuestLogEntry> get_hash = this.hashArray[hashPosition];	// Get the Linked list from index hashPosition
		count++;	// Increment number of items checked by 1
		if (get_hash == null) {	// If this hash value is empty, since this is a chained hash table, key doesn't exist
			return new Pair280<>(null, count);	// return (null, count) pair
		}
		LinkedIterator280<QuestLogEntry> iter = get_hash.iterator();	// create a new QuestLogEntry iterator to find key we need to find
		iter.goFirst();	// Go to the first item
		if (iter.item().key().compareTo(k) != 0) {	// if the key of the item iter is pointing at is not the key we're looking for
			while (iter.itemExists() && iter.item().key().compareTo(k) !=0) {	// keep looping until we find the key we're looking for
				count++;	// Increment count by 1
				iter.goForth();	// go to the next item
			}
		}

		return new Pair280<>(iter.item(), count);  // Remove this line you're ready.  It's just to prevent compiler errors.
	}


	public static void main(String args[])  {
		// Make a new Quest Log
		QuestLog hashQuestLog = new QuestLog();

		// Make a new ordered binary lib280.tree.
		OrderedSimpleTree280<QuestLogEntry> treeQuestLog =
				new OrderedSimpleTree280<QuestLogEntry>();


		// Read the quest data from a CSV (comma-separated value) file.
		// To change the file read in, edit the argument to the FileReader constructor.
		CSVReader inFile;
		try {
			//input filename on the next line - path must be relative to the working directory reported above.
			inFile = new CSVReader(new FileReader("QuestLog-Template/quests100000.csv"));
		} catch (FileNotFoundException e) {
			System.out.println("Error: File not found.");
			return;
		}

		String[] nextQuest;
		try {
			// Read a row of data from the CSV file
			while ((nextQuest = inFile.readNext()) != null) {
				// If the read succeeded, nextQuest is an array of strings containing the data from
				// each field in a row of the CSV file.  The first field is the quest name,
				// the second field is the quest region, and the next two are the recommended
				// minimum and maximum level, which we convert to integers before passing them to the
				// constructor of a QuestLogEntry object.
				QuestLogEntry newEntry = new QuestLogEntry(nextQuest[0], nextQuest[1],
						Integer.parseInt(nextQuest[2]), Integer.parseInt(nextQuest[3]));
				// Insert the new quest log entry into the quest log.
				hashQuestLog.insert(newEntry);
				treeQuestLog.insert(newEntry);
			}
		} catch (IOException e) {
			System.out.println("Something bad happened while reading the quest information.");
			e.printStackTrace();
		}

		// Print out the hashed quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
//		System.out.println(hashQuestLog);

		// Print out the lib280.tree quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.  It takes way too long.
//	    System.out.println(treeQuestLog.toStringInorder());

		String[] arrays = hashQuestLog.keys();		// Get the set of keys contained in quest log
		int totalItems = arrays.length;
		Double count = 0.0;	// Count to keep track of the total number of iterations
		for (String key : arrays) {	// For each key in our key list
			count += hashQuestLog.obtainWithCount(key).secondItem();	// increment count by total count returned by
			// obtainWithCount()
		}
		Double hashAverage = count / totalItems;	// Find average by dividing count by total keys in arrays
		System.out.println("Average # of items examined per query in the hash quest log with 4 entries : " + hashAverage);


		Double total = 0.0;		// total counter for total count for treeQuestLog
		for (String key: arrays) {	// for each key obtained from hash.keys
			total += treeQuestLog.searchCount(hashQuestLog.obtain(key));	// Increment total by searchCount return value
		}
		Double average = total / totalItems;	// Find the total running average
		System.out.println("Average # of items examined per query in the tree quest log with 4 entries : " + average);
	}
}
