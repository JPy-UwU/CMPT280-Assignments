import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;

import com.opencsv.CSVReader;

import lib280.base.Pair280;
import lib280.hashtable.KeyedChainedHashTable280;
import lib280.tree.OrderedSimpleTree280;

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
	 * Obtain an array of the keys (quest names) from the quest log.
	 * 
	 * @return The array of keys (quest names) from the quest log.
	 */
	public String[] keys() {
		String[] keyList = new String[this.count];
		
		this.goFirst();
		int i = 0;
		while(this.itemExists()) {
			keyList[i] = this.item().key();
			this.goForth();
			i++;
		}
		
		return keyList;
	}
	
	/**
	 * Format the quest log as a string which displays the quests in the log in 
	 * alphabetical order by name.
	 * 
	 * @return A nicely formatted quest log.
	 */
	public String toString() {
		String[] questNames = this.keys();	
		String result = "";
		Arrays.sort(questNames);
		for(int i=0; i < questNames.length; i++) {
			result += this.obtain(questNames[i]).toString() + "\n";
		}
		return result;
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
		// Write a method that returns a Pair280 which contains the quest log entry with name k, 
		// and the number QuestLogEntry objects that were examined in the process.  You need to write
		// this method from scratch without using any of the superclass methods (mostly because 
		// the superclass methods won't be terribly useful unless you can modify them, which you
		// aren't allowed to do!).
		
		int keyHash = this.hashPos(k);
		int itemsExamined = 0;
		
		// If the chain which the key hashes to is empty, then there is no quest named k.
		if( hashArray[keyHash].isEmpty() ) {
			return new Pair280<QuestLogEntry, Integer>(null, 0);
		}
		
		// Otherwise, search the chain for the quest k.
		hashArray[keyHash].goFirst();
		while(hashArray[keyHash].itemExists()) {
			itemsExamined++;
			if( hashArray[keyHash].item().key().compareTo(k) == 0 ) {
				// we found the quest.  Return it and the count.
				return new Pair280<QuestLogEntry, Integer>(hashArray[keyHash].item(), itemsExamined); 
			}
			hashArray[keyHash].goForth();
		}
		
		// If we get here, the chain did not contain the quest k.
		return new Pair280<QuestLogEntry, Integer>(null, itemsExamined);		
		
	}
	
	
	public static void main(String args[])  {
		// Make a new Quest Log
		QuestLog hashQuestLog = new QuestLog();
		
		// Make a new ordered binary lib280.tree.
		OrderedSimpleTree280<QuestLogEntry> treeQuestLog =
				new OrderedSimpleTree280<QuestLogEntry>();
		
		// Read the quest data from a CSV (comma-separated value) file.
		// To change the file read in, edit the argument to the FileReader constructor.

		System.out.println("Working Directory = " +
				System.getProperty("user.dir"));

		CSVReader inFile;
		try {
		    //NOTE: if you are using ECLIPSE, remove the 'QuestLog/' portion of the
            //input filename on the next line.
			inFile = new CSVReader(new FileReader("QuestLog/quests4.csv"));
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
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.
		System.out.println(hashQuestLog);
		
		// Print out the lib280.tree quest log's quests in alphabetical order.
		// COMMENT THIS OUT when you're testing the file with 100,000 quests.
		System.out.println(treeQuestLog.toStringInorder());
		
		// Get the names of all of the quests.
		String[] quests = hashQuestLog.keys();

		// Determine the average number of elements examined during access for hashed quest log.
	    // (call hashQuestLog.obtainWithCount() for each quest in the log and find average # of access)
			double hashAvg = 0;
		for(int i = 0; i < quests.length; i++) {
			Pair280<QuestLogEntry, Integer> p = hashQuestLog.obtainWithCount(quests[i]);
			hashAvg += p.secondItem();
		}
		hashAvg /= quests.length;
		System.out.println("Avg. # of items examined per query in the hashed quest log with " + hashQuestLog.count() + " entries: " + hashAvg);
		
		
		
		// Determine the average number of elements examined during access for lib280.tree quest log.
	    // (call treeQuestLog.searchCount() for each quest in the log and find average # of access)
		double treeAvg = 0;
		for(int i = 0; i < quests.length; i++) {
			treeAvg += treeQuestLog.searchCount(hashQuestLog.obtain(quests[i]));					
		}
		treeAvg /= quests.length;
		System.out.println("Avg. # of items examined per query in the lib280.tree quest log with " + hashQuestLog.count() + " entries: " + treeAvg);
	

		
	}
	
	
}
