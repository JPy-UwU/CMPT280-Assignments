/* HashTableUos.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.dictionary;

/**	A hash table version of a Dictionary used to store items by their hash value. 
	It has functions to insert, delete, obtain, and  search for items, and
	has a frequency function and a current item that can be deleted.  A linear
	iterator is included to move the current item through the hash table. */
public abstract class HashTable280<I> implements Dict280<I>
{
	/**	Number of entries in the hash table. */
	protected int count;

	/**	Should search continue from current item or start at the beginning?. */
	protected boolean searchesContinue = false;


	/**	Size of the hash table. */
	public abstract int capacity();

	/** Frequency of items in hash table */
	public abstract int frequency(I i);
	
	/**	Number of entries in the hash table. <br>
		@timing O(1) */
	public int count()
	{
		return count;
	}

	/**	Ratio of the number of items to the table capacity. <br>
		@timing O(1) 
		*/
	public double loadFactor()
	{
		return ((double)count) / capacity();
	}

	/**	Is the hash table empty?.  
		Analysis: Time = O(1) */
	public boolean isEmpty()
	{
		return count == 0;
	}

	/**	
	 * Default hash position is obtained by the simple division method, which is
	 * calculated by using the object's default hash code. <br>
	 * @timing O(1)
	 * @param y item to calculate its hash position 
	 */
	public int hashPos(Object y)
	{
		return Math.abs(y.hashCode()) % capacity();
	}


	/**	
	 * Following searches start from the first position.
	 * @timing O(1) 
	 */
	
	public void restartSearches()
	{
		searchesContinue = false;
	}

	/**	
	 * Following searches continue from the current position. 
	 * @timing O(1)
	 */
	public void resumeSearches()
	{
		searchesContinue = true;
	}


	

	/**	
	 * A shallow clone of this object. 
	 * Analysis: Time = O(1) 
	 */
	@SuppressWarnings("unchecked")
	public HashTable280<I> clone()
	{
		try
		{
			return (HashTable280<I>) super.clone();
		} catch (CloneNotSupportedException e)
		{
			/*	Should not occur: this is a ContainerUos, which implements Cloneable. */ 
			e.printStackTrace();
			return null;
		}
	}

}
