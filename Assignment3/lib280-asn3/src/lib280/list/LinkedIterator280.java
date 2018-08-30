/* LinkedIterator280.java
 * ---------------------------------------------
 * Copyright (c) 2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.list;

import lib280.base.LinearIterator280;
import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ItemNotFound280Exception;

/**	A LinearIterator280 for linked lists.  It utilizes a cursor to 
	keep track of the current item, and has functions to move 
	forward and to the front of the list.  It also has functions to 
	determine if it is before the start or after the end of the 
	structure. */
public class LinkedIterator280<I> implements LinearIterator280<I>
{
	/**	List being iterated. */  
	protected LinkedList280<I> list;
  
	/**	The node with the current item. */
	protected LinkedNode280<I> cur;
  
	/**	The node previous to the current node. */
	protected LinkedNode280<I> prev;
  
	/**	Create a new iterator for the newList. <br>
		Analysis : Time = O(1) 
		@param newList list to be iterated */
	public LinkedIterator280(LinkedList280<I> newList)
	{
		list = newList;
		if(!list.isEmpty()) goFirst();
	}
  
	/**	Create a new iterator at a specific position in the newList. <br>
		Analysis : Time = O(1)
		@param newList list to be iterated
		@param initialPrev the previous node for the initial position
		@param initialCur the current node for the initial position */
	public LinkedIterator280(LinkedList280<I> newList,
                             LinkedNode280<I> initialPrev, LinkedNode280<I> initialCur)
	{
		list = newList;
		prev = initialPrev;
		cur = initialCur;
	}
    
	@Override
	public boolean before()
	{
		return (prev==null) && (cur==null);
	}
  
	@Override
	public boolean after()
	{
		return (cur==null) && (prev!=null || list.isEmpty());
	}

	@Override
	public boolean itemExists()
	{
		return !before() && !after();
	}

	@Override
	public I item() throws ItemNotFound280Exception
	{
		if (!itemExists())
			throw new ItemNotFound280Exception("A current item must exist");

		return cur.item();
	}

	@Override
	public void goFirst() throws ContainerEmpty280Exception
	{
		if(this.list.isEmpty()) throw new ContainerEmpty280Exception("Cannot move to first element of an empty list.");
		
		prev = null;
		cur = list.firstNode();
	}

	@Override
	public void goBefore()
	{
		cur = null;
		prev = null;
	}

	@Override
	public void goForth() throws AfterTheEnd280Exception
	{
		if (after())
			throw new AfterTheEnd280Exception("Cannot advance to next item when already after.");

		if (before())
			goFirst();
		else
		{
			prev = cur;
			cur = cur.nextNode();
		}
	}

	@Override
	public void goAfter()
	{
		cur = null;
		prev = list.lastNode();
	}

	@Override
	public String toString()
	{
		return list.toString();
	}

	@Override
	@SuppressWarnings("unchecked")
	public LinkedIterator280<I> clone()
	{
		try
		{
			return (LinkedIterator280<I>)super.clone();
		} catch (CloneNotSupportedException e)
		{
			// Should not occur: this is a Cursor280, which implements Cloneable 
			e.printStackTrace();
			return null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean equals(Object other)
	{
		if( other instanceof LinkedIterator280) {
			LinkedIterator280<I> otherIter = (LinkedIterator280<I>) other;
			return (otherIter.cur == cur) && (otherIter.prev == prev) && (otherIter.list == list);
		}
		else {
			return false;
		}
	}

}
