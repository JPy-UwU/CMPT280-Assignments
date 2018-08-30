import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class RadixSortMSD {
	private static String a = "A";	// First Possible digit in our uppercase string
	private static char a_char = a.charAt(0);

	public RadixSortMSD() {}

	/**
	 * Main method that sorts the given array of string into lexicographic order
	 * @param keys: an array of keys to be sorted
	 * @param R: Radix value
	 */
	public static void RadixSortMSD(String[] keys, int R) {
		ArrayList<String> string = new ArrayList<>(Arrays.asList(keys));	// Convert the string List to an arrayList
		ArrayList<String> sorted = new ArrayList<>();	// Create an arrayList containing the sorted list
		sortByDigit(string, R, 0, sorted);	// Call in recursive helper method
		int offset = 0;	//	which position in our keys list are we?
		for (String item: sorted) {	// For each string in the sorted version of the list
			keys[offset] = item;	// Replace the current index in our original list with the appropriate item
			offset++;	// Move to the next offset in our list
		}
	}


	/**
	 * Helper method that performs the recursive MSDRadix sort to our Array list version of the string list
	 * @param keys: The converted String List --> ArrayList to be sorted
	 * @param R: The Radix --> Number of possible characters in our list to be sorted. in this case, 26 (all uppercase alphas)
	 * @param partition: Current digit that is to be compared
	 * @param aux: Auxiliary array list that holds our sorted list
	 */
	@SuppressWarnings("unchecked")
	private static void sortByDigit(ArrayList<String> keys,int R, int partition, ArrayList<String> aux) {
		ArrayList<String>[] buckets = new ArrayList[26];	// Create our list containing our buckets for each character
		// Initialize our arrayLists
		for (int i = 0; i < R; i++) {
			buckets[i] = new ArrayList<>();
		}

		// For each string in our keys array, insert each of the string into their corresponding locations
		for (String string : keys) {
			if (partition >= string.length()) {	// Of there are no more digits after the current character location,
				aux.add(string);	// add the current string to the auxiliary list of already sorted strings
			}	else {	// Otherwise, place the current string into its corresponding bucket. To get the index, we
				// subtract the ASCII code of the current string by the ASCII code of "A" (since it's all uppercase)
				buckets[string.charAt(partition) - a_char].add(string);
			}
		}
		// Now for each bucket in our list of buckets, we decide whether or not to recurse through the list
		for (int i = 0; i < R; i++) {
			if (buckets[i].isEmpty()) {	// if the bucket is empty, skip this current iteration
				continue;
			}
			if (buckets[i].size() <= 10) {	// If the list contains 10 or less item, perform an insertion sort, then
				insertionSort(buckets[i]);	// perform an insertion sort to short enough lists
				aux.addAll(buckets[i]);	// Then add it to our auxiliary list containing already sorted item
			}
			else {	// Otherwise, recurse through the current bucket, until it is empty!
				sortByDigit(buckets[i], R,partition+1, aux); // Also, move to the next character
			}
		}
	}

	/**
	 * My implementation of insertion sort that will be used for sorting small array
	 * List in our implementation of MSD sort. Based on the lecture notes!
	 * @param stringList an array list of strings to be sorted
	 */
	@SuppressWarnings("unchecked")
	private static void insertionSort(ArrayList stringList) {
		// For each item in our string list
		for (int i = 0; i < stringList.size(); i++) {
			// Store the current key to be compared in the next loop
			String key = (String) stringList.get(i);
			int j = i - 1;	// counter for the 2nd loop to determine appropriate position of the current item
			// While the item is not on the right spot
			while (j >= 0 && ((String) stringList.get(j)).compareTo(key) > 0) {
				stringList.set(j+1, stringList.get(j));
				j--;
			}
			stringList.set(j + 1, key);
		}
	}

	public static void main(String args[]) {


		// *************************************************************
		// Change the input file by changing the line below.
		// *************************************************************
		String inputFile = "RadixSortMSD-Template/words-235884.txt";

		// Initialize a scanner to read the input file.
		Scanner S = null;
		try {
			S = new Scanner(new File(inputFile));
		} catch (FileNotFoundException e) {
			System.out.println("Error: " + inputFile + "was not found.");
			return;
		}

		// Read the first line of the file and convert it to an integer to see how many
		// words are in the file.
		int numWords = Integer.valueOf(S.nextLine());

		// Initialize an array large enough to store numWords words.
		String items[] = new String[numWords];

		// Read each word from the input file and store it in the next free element of
		// the items array.
		int j = 0;
		while (S.hasNextLine()) {
			items[j++] = S.nextLine().toUpperCase();
		}
		S.close();
		System.out.println("Done reading " + numWords + " words.");


		// Test and time your radix sort.
		long startTime = System.nanoTime();

		RadixSortMSD(items, 26);        // Calls my RadixSortMSD method

		long stopTime = System.nanoTime();


		// Uncomment this section if you want the sorted list to be printed to the console.
		// (Good idea for testing with words-basictest.txt; leave it commented out though
		// for testing files with more than 50 words).

		for (int i = 0; i < items.length; i++) {
			System.out.println(items[i]);
		}

		// Print out how long the sort took in milliseconds.
		System.out.println("Sorted " + items.length + " strings in " + (stopTime - startTime) / 1000000.0 + "ms");
	}
}
