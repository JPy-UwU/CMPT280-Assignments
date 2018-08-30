import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import lib280.list.LinkedList280;


/******************************************************************************
 * 
 * This file contains three different solutions to the MSD Radix Sort:
 * 
 * 1. A version where the lists for each bucket are java.util.ArrayedList objects.
 * 2. A version where the lists for each bucket are lib280.LinkedList280 objects.
 * 3. A version where the lists for each bucket are implemented with a 2D array.
 *
 * The first two are acceptable for full marks.  The third version is should
 * probably receive a small deduction for design because it turns out to be
 * excruciatingly slow.
 *
 ******************************************************************************/

public class RadixSortMSD {
	
	/**
	 * This version of radix sort implements the lists using java.util.ArrayList.  
	 * @param items The items to be sorted.
	 * @param i The offset of the digit that we are considering (0 is left-most).
	 */
	protected static void sortByDigitArrayList(ArrayList<String> items, int i) {
		@SuppressWarnings("unchecked")
		// Create a "bucket" for each possible letter.
		ArrayList<String> lists[] = new ArrayList[27];
		
		// Initialize each bucket to be an empty list.
		for(int j=0; j < 27; j++) lists[j] = new ArrayList<String>();
		
		// The recurse flag is set to true if there are any words in items
		// that have an i-th digit.  If not, items is already sorted and we
		// don't have to recurse.
		boolean recurse = false;
		
		// For each item to be sorted....
		for(int j = 0; j < items.size(); j++) {
			// Obtain the j-th item
			String cur = items.get(j);
			
			// If the item has an i-th letter...
			if( cur.length() > i ) {
				// Put it on the appropriate list.  Subtracting 64 from the ASCII code
				// for the letter maps 'A' to list 1, 'B' to list 2, etc.  List 0
				// is reserved for items that don't have an i-th character.
				int listIdx = cur.charAt(i)-64;
				lists[listIdx].add(cur);
				
				// Since there was a word with an i-th digit, we have to recurse to process
				// at least one of lists 1 through 26.
				recurse = true;				
			}
			// otherwise, we've already used up all the digits, so put it on the 0-th
			// list because it is at least as short as any other element in items and must come before
			// any other item.
			else {
				lists[0].add(cur);
			}
		}
		
		// If there are longer items to consider...
		if( recurse ) {
			// Check each list...(list 0 is not included because it's already sorted)
			for(int j=1; j < 27; j++) {
				// If there is more than one item on list j that need to be sorted...
				if( lists[j].size() > 1 ) { 
					// Then recurse the radix sort to the next digit.
					sortByDigitArrayList(lists[j], i+1);
				}
			}
		}
		
		// Now we need to concatenate everything on lists 0 through 26 in order, and
		// put the result back in items... so we begin by clearing out 'items'.
		items.clear();
		
		// Iterate over each bucket list in order, adding each item we find to the end of 'items'.
		// At the conclusion of this loop, everything in 'items' is sorted.
		for(int j=0; j < 27; j++) {
			for(int k=0; k < lists[j].size(); k++) {
				items.add(lists[j].get(k));
			}
		}
		
	}
	
	
	
	/**
	 * This version of radix sort implements the lists using lib280.LinkedList280.  
	 * @param items The items to be sorted.
	 * @param i The offset of the digit that we are considering (0 is left-most).
	 */
	protected static void sortByDigitLinkedList(LinkedList280<String> items, int i) {
		@SuppressWarnings("unchecked")
		// Create a "bucket" for each possible letter.
		LinkedList280<String> lists[] = new LinkedList280[27];
		
		// Initialize each bucket to be an empty list.
		for(int j=0; j < 27; j++) lists[j] = new LinkedList280<String>();
		
		// The recurse flag is set to true if there are any words in items
		// that have an i-th digit.  If not, items is already sorted and we
		// don't have to recurse.
		boolean recurse = false;
		while(!items.isEmpty()) {
			// Get the next item and remove it from 'items'.
			String cur = items.firstItem();
			items.deleteFirst();
			
			// If the current item has an i-th digit...
			if( cur.length() > i ) {
				// Put it on the appropriate list.   Subtracting 64 from the ASCII code
				// for the letter maps 'A' to list 1, 'B' to list 2, etc.  List 0
				// is reserved for items that don't have an i-th character.
				int listIdx = cur.charAt(i)-64;
				lists[listIdx].insertLast(cur);

				// Since there was a word with an i-th digit, we have to recurse to process
				// at least one of lists 1 through 26.
				recurse = true;				
			}
			// otherwise, we've already used up all the digits, so put it on the 0-th
			// list because it is at least as short as any other element in items and must come before
			// any other item.
			else {
				lists[0].insertLast(cur);
			}
		}
		
		// If there are longer items to consider...
		if( recurse ) {
			// Check each list...(list 0 is not included because it's already sorted)
			for(int j=1; j < 27; j++) {
				// If there is more than one item on it...
				// (This is a very hack-y way to test whether the list has 2 or more items
				// because, remarkably, LinkedList280 does not have a size() method!)
				if( !lists[j].isEmpty() ) lists[j].goFirst();
				if( !lists[j].after() ) lists[j].goForth();
				if( lists[j].itemExists() ) { 
					// Then recurse the radix sort to the next digit.
					sortByDigitLinkedList(lists[j], i+1);
				}
			}
		}
		
		// Now we need to concatenate everything on lists 0 through 26 in order, and
		// put the result back in 'items'... 
		
		// Iterate over each bucket list in order, adding each item we find to the end of 'items'.
		// At the conclusion of this loop, everything in 'items' is sorted.
		for(int j=0; j < 27; j++) {
			while(!lists[j].isEmpty()) {
				items.insertLast(lists[j].firstItem());
				lists[j].deleteFirst();
			}
		}
		
	}
	
	/**
	 * This version of radix sort implements the lists using a 2D array.
	 * This version becomes HORRIFICALLY slow for larger input arrays.  
	 * @param items The items to be sorted.
	 * @param i The offset of the digit that we are considering (0 is left-most).
	 */
	protected static void sortByDigitArrays(String items[], int i, int n) {
		// Create a 2D array to store the items.  lists[i] is the i-th list and
		// it needs to be large enough to hold up to items.length items in the worst case.
		// (This results in lots of wasted space)
		String lists[][] = new String[27][items.length];
		
		// For this approach to work, we need an array of counters to keep track of the end of 
		// each array-based list.  counters[i] is the offset of the first open position of 
		// lists[i].
		int counters[] = new int[27];
			
		// The recurse flag is set to true if there are any words in items
		// that have an i-th digit.  If not, items is already sorted and we
		// don't have to recurse.
		boolean recurse = false;
		
		// For each item to be sorted...
		for(int j=0; j < n; j++) {
		
			// If the current item has an i-th digit...		
			if( items[j].length() > i ) {
				// Put it on the appropriate list.   Subtracting 64 from the ASCII code
				// for the letter maps 'A' to list 1, 'B' to list 2, etc.  List 0
				// is reserved for items that don't have an i-th character.
				int listIdx = items[j].charAt(i)-64;
				lists[listIdx][counters[listIdx]++] = items[j];
				recurse = true;
			}
			// otherwise, we've already used up all the digits, so put it on the 0-th
			// list because it is at least as short as any other element in items and must come before
			// any other item.
			else {
				lists[0][counters[0]++] = items[j];
			}
		}
		
		// If there are longer items to consider...
		if( recurse ) {
			// Check each list...
			for(int j=1; j < 27; j++) {
				// If there is more than one item on it...
				if( counters[j] > 1 ) { 
					// Then recurse the radix sort to the next digit.
					sortByDigitArrays(lists[j], i+1, counters[j]);
				}
			}
		}
		
		
		// Now we need to concatenate everything on lists 0 through 26 in order, and
		// put the result back in 'items'... 
		
		// Iterate over each bucket list in order, adding each item we find to the end of 'items'.
		// At the conclusion of this loop, everything in 'items' is sorted.
		int c=0;
		for(int j=0; j < 27; j++) {
			for(int k=0; k < counters[j]; k++) {
				items[c++] = lists[j][k];
			}
		}
		
		
	}
	
	/**
	 * This version of radix sort implements the lists using a 2D array.
	 * This version becomes HORRIFICALLY slow for larger input arrays.  
	 * @param items The items to be sorted.
	 */
	public static void MSDRadixSortArrays(String items[]) {
		// Initiate the first call to the recursive radix sort.
		sortByDigitArrays(items, 0, items.length);
	}

	/**
	 * This version of radix sort implements the lists using lib280.LinkedList280.
	 * @param items The items to be sorted.
	 */
	public static void MSDRadixSortLinkedList(String items[]) {
		// Repackage the input array into a linked list.
		LinkedList280<String> L = new LinkedList280<String> ();
		for(int i=0; i < items.length; i++) 
			L.insertLast(items[i]);
		
		// Initiate the first call to the recursive radix sort.
		sortByDigitLinkedList(L, 0);
		
		// Copy the sorted items in the linked list back to the input array.
		int c = 0;
		while(!L.isEmpty()) {
			items[c++] = L.firstItem();
			L.deleteFirst();
		}
	}
	
	/**
	 * This version of radix sort implements the lists using java.util.ArrayList.
	 * @param items The items to be sorted.
	 */
	public static void MSDRadixSortArrayList(String items[]) {
		// Repackage the input array into a arrayed list.
		ArrayList<String> L = new ArrayList<String> ();
		for(int i=0; i < items.length; i++) 
			L.add(items[i]);
		
		// Initiate the first call to the recursive radix sort.
		sortByDigitArrayList(L, 0);
		
		// Copy the sorted items in the linked list back to the input array.
		for(int c=0; c < L.size(); c++) {
			items[c] = L.get(c);
		}
	}
	
	
	
	public static void main(String args[]) {
		
		// Change the input file here as desired.
		String inputFile = "words-basictest.txt";
		
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
		int j=0;
		while(S.hasNextLine()) {
			items[j++] = S.nextLine().toUpperCase();
		}
		S.close();
		System.out.println("Done reading " + numWords + " words.");
		

/*      REMOVE THIS LINE TO ENABLE THE ARRAY VERSION
		// This tests and times the Array version of the radix sort.  This runs *very*
		// slowly for larger arrays, so don't uncomment this unless you are using one
		// of the smaller files.
		long arrayStart = System.nanoTime();
		MSDRadixSortArrays(items);
		long arrayStop = System.nanoTime();
		
//		for(int i=0; i < items.length; i++) {
//			System.out.println(items[i]);
//		}
		
		System.out.println("Array version sorted " + items.length + " strings in " + (arrayStop-arrayStart)/1000000.0 + "ms");
		
*** REMOVE THIS LINE TO ENABLE THE ARRAY VERSION */
		
		// Test and time the lib280.LinkedList280 version of Radix Sort.
		long listStart = System.nanoTime();
		MSDRadixSortLinkedList(items);
		long listStop = System.nanoTime();
		
//      Uncomment this section if you want the sorted list to be printed to the console.		
//		for(int i=0; i < items.length; i++) {
//			System.out.println(items[i]);
//		}
		
		System.out.println("Linked list version sorted " + items.length + " strings in " + (listStop-listStart)/1000000.0 + "ms");

		
		// Test the java.util.ArrayList version of the radix sort.
		long alistStart = System.nanoTime();
		MSDRadixSortArrayList(items);
		long alistStop = System.nanoTime();
		
//      Uncomment this section if you want the sorted list to be printed to the console.		
//		for(int i=0; i < items.length; i++) {
//			System.out.println(items[i]);
//		}
		
		System.out.println("Arrayed list version sorted " + items.length + " strings in " + (alistStop-alistStart)/1000000.0 + "ms");
	
	}
	
}
