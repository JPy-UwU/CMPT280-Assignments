/* ChainedHashTable280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.hashtable;

import lib280.base.CursorPosition280;
import lib280.dictionary.HashTable280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.ItemNotFound280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.list.LinkedIterator280;
import lib280.list.LinkedList280;

/**	A HashTable dictionary that uses chaining.  Items can be 
	inserted, deleted, obtained, and searched for by hashValue.  
	An iterator is included, with functions goFirst, goForth, 
	before, and after */
public class ChainedHashTable280<I> extends HashTable280<I>
{ 
	/**	Array to store linked lists for separate chaining. */
	protected LinkedList280<I>[] hashArray;
  
	/**	Position of the current item in its list. */
	protected LinkedIterator280<I> itemListLocation;
  
	/**	
	 * Create a new hash list for a new chain.
	 * @timing  O(1)  
	 */
	protected LinkedList280<I> newChain()
	{
		return new LinkedList280<I>();
	}

	/**	
	 * Construct hash table strucuture with at least newSize locations. <br>
	 * @timing O(newSize^2)
	 * @param newSize size of the hash table is at least this large
	 */
	@SuppressWarnings("unchecked")
	public ChainedHashTable280(int newSize)
	{
		hashArray = new LinkedList280[newSize];
		count = 0;
		itemListLocation = null;
	}

	@Override
	public int capacity()
	{
		return hashArray.length;
	}
  
	
	@Override
	public int frequency(I i)
	{
		int result = 0;
		boolean saveSearchMode = searchesContinue;
		CursorPosition280 savePos = currentPosition();
		restartSearches();
		search(i);
		resumeSearches();
		while (itemExists())
		{
			result++;
			search(i);
		}
		searchesContinue = saveSearchMode;
		goPosition(savePos);
		return result;
	}


	
	@Override
    @SuppressWarnings("unchecked")
	public boolean membershipEquals(I x, I y)
	{
		if ((x instanceof Comparable) && (y instanceof Comparable))
			return  0 == ((Comparable<I>) x).compareTo(y);
		else if (x.equals(y))
			return true;
		else 
			return false;
	}



	@Override
	public boolean isFull()
	{
		return false;
	}

	@Override
	public boolean itemExists()
	{
		return (itemListLocation!=null) && (itemListLocation.itemExists());
	}
    
	@Override
	public I item() throws NoCurrentItem280Exception
	{
		if (!itemExists())
			throw new NoCurrentItem280Exception("Cannot return an item that does not exist.");
			
		return itemListLocation.item();
	}
  
	@Override
	public boolean has(I y)
	{
		LinkedIterator280<I> saveListLocation;
		if(itemListLocation != null)
			saveListLocation = itemListLocation.clone();
		else
			saveListLocation = null;
		search(y);
		boolean result = itemExists();
		itemListLocation = saveListLocation;
		return result;
	}
  
	@Override
	public void insert(I y) 
	{
		int itemHashLocation = hashPos(y);
		if (hashArray[itemHashLocation]==null)
			hashArray[itemHashLocation] = newChain();
		hashArray[itemHashLocation].insert(y);
		count++;
	}

	@Override
	public void delete(I y) throws ItemNotFound280Exception
	{           
		if (!has(y)) 
			throw new ItemNotFound280Exception("Cannot delete item because it is not in the table.");
		
		if (itemExists() && membershipEquals(y, item()))
			deleteItem();
		else
		{
			hashArray[hashPos(y)].delete(y);
			if ( (itemExists()) && (hashPos(y) == hashPos(itemListLocation.item())))
				search(item()); // deletion might have messed up itemListLocation
			count--;
		}
	}
  
	@Override
	public void deleteItem() throws NoCurrentItem280Exception
	{
		I deleteItem = item();
		goForth(); // deletion should cause a move to the next item.
		hashArray[hashPos(deleteItem)].delete(deleteItem);
		if (itemExists())
			search(item()); // delete probably destroyed the prev reference.
		count--;     
	}

	@Override
	public void search(I y) 
	{
		int itemHashLocation = hashPos(y);
		if (searchesContinue && itemListLocation!=null)
			goForth();
		else
		{
			if (hashArray[itemHashLocation]==null)
				hashArray[itemHashLocation] = newChain();
			itemListLocation = hashArray[itemHashLocation].iterator();
		}
		while (!itemListLocation.after() && !membershipEquals(y, itemListLocation.item()))
			itemListLocation.goForth();
	}
  
	@Override
	public I obtain(I y) throws ItemNotFound280Exception
	{
		if (!has(y))
			throw new ItemNotFound280Exception("Cannot return an item that does not exist");
	
		return hashArray[hashPos(y)].obtain(y);
	}

	@Override
	public boolean before() 
	{
		return (itemListLocation==null) || (itemListLocation.before());
	}
  
	/**	
	 * Are we after the last item in the hash table?.
	 * @timing O(1) 
	 */  
	public boolean after()
	{
		return (itemListLocation!=null) && (itemListLocation.after());
	}

    @Override
	public void goForth() throws AfterTheEnd280Exception
	{
		if (this.after())
			throw new AfterTheEnd280Exception("Cannot goForth() when at the end of the table");

		if (this.itemListLocation==null || this.itemListLocation.before())
			this.goFirst();
		else
		{
			int itemHashLocation = this.hashPos(item());
			this.itemListLocation.goForth();
			if (this.itemListLocation.after())
				this.findNextItem(itemHashLocation + 1);
		}
	}

    @Override
	public void goFirst()
	{
		findNextItem(0);
	}

    @Override
	public void goBefore() 
	{
		itemListLocation.goBefore();
	}

    @Override
	public void goAfter() 
	{
		if (hashArray[hashArray.length-1] == null)
			hashArray[hashArray.length-1] = newChain();
		itemListLocation = hashArray[hashArray.length-1].iterator();
		if( !hashArray[hashArray.length-1].isEmpty() ) 
			itemListLocation.goAfter();
	}

	/**
	 * Go to the first item of the first non-empty list
	 * starting at index, or goAfter() if none found.
	 * @timing O(capacity()) - worst case.
	 * @param index first hash value to search to find the next item
	 */
	protected void findNextItem(int index)
	{
		int itemHashLocation = index;
		while ((itemHashLocation <= this.hashArray.length-1)
				&& ((this.hashArray[itemHashLocation] == null) || (this.hashArray[itemHashLocation].isEmpty())))
			itemHashLocation++;
		if (itemHashLocation < this.hashArray.length)
		{
			this.itemListLocation = this.hashArray[itemHashLocation].iterator();
			this.itemListLocation.goFirst();
		}
		else
			this.goAfter();
	}

    @Override
	public CursorPosition280 currentPosition()
	{
		/* Return type is CursorPosition280 rather than LinkedIterator280<I> as the iterator
		   returned only iterates through one list rather than the whole container. */
		if (itemListLocation != null) 
			return itemListLocation.clone();
		else
			return null;
	}

    @Override
    @SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 pos)
	{
		if (pos != null  && !(pos instanceof LinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a LinkedIterator280<I>");
		if(pos != null) 
			itemListLocation = ((LinkedIterator280<I>) pos).clone();
		else
			itemListLocation = null;
	}

    @Override
	public String toString()
	{
		String result = "";
		for (int i=0; i<capacity(); i++)
			if (hashArray[i] != null)
				result += "\n" + i + ": " + hashArray[i].toString();
		return result;
	}

    @Override
	public void clear()
	{
		for (int i=0; i<capacity(); i++)
			hashArray[i]=null;
		count = 0;
		itemListLocation = null;
	}

	public static void main(String args[]) {
		
		
		ChainedHashTable280<Double> H = new ChainedHashTable280<Double>(10);
		
		// Test isEmpty on empty table
		if(!H.isEmpty()) System.out.println("Error: hash table is empty but isEmpty() says otherwise.");
		
		// Testing insert() on empty table.
		H.insert(42.0);
		H.hashArray[H.hashPos(42.0)].goFirst();
		if( H.hashArray[H.hashPos(42.0)].item()  != 42.0) 
			System.out.println("Error: item 42 is not in the has table where it should be!");
		
		// Testing isEmpty() on non-empty table
		if(H.isEmpty()) System.out.println("Error: hash table is not empty bit isEmpty(0 says otherwise.");
		
		// Testing insert() on non-empty table
		H.insert(99.0);
		H.hashArray[H.hashPos(99.0)].goFirst();
		if( H.hashArray[H.hashPos(99.0)].item()  != 99.0) 
			System.out.println("Error: item 99 is not in the has table where it should be!");
		
		// Insert one more thing:
		H.insert(19.0);
		H.hashArray[H.hashPos(19.0)].goFirst();
		if( H.hashArray[H.hashPos(19.0)].item()  != 19.0) 
			System.out.println("Error: item 19 is not in the has table where it should be!");

		// Test search() on multi-items.
		H.search(42.0);
		if(!H.itemExists() || H.item() != 42.0)
			System.out.println("Error: cursor should be on 42.0 but it isn't!");
		
		H.goBefore();
		H.goForth();
		if(!H.itemExists() || H.item() != 19.0)
			System.out.println("Error: cursor should be on 19.0 but it isn't!");


		// Needs more regression testing!
		
		// Use iterators to print the items forward and backward.
		
		H.goFirst();
		while(H.itemExists()) {
			System.out.println(H.item());
			H.goForth();
		}
		
		
		System.out.println("Regression test complete.");
	}
} 
