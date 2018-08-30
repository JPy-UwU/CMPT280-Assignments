package lib280.list;
/* LinkedNode280.java
 * ---------------------------------------------
 * Copyright (c) 2004, 2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 


/**	A basic linked node containing a generic item and a reference to 
	the next node.  There are routines for setting the item and next 
	node, and a toString routine that provides a string representation of 
	the list of nodes starting with the current node. */
public class LinkedNode280<I> implements Cloneable
{
	/**	Contents of the node. */
	protected I item;
  
	/**	The next node. */
	protected LinkedNode280<I> nextNode;

	/**	Construct a new node with item x. <br>
		Analysis: Time = O(1) 
		@param x item placed in the new node */
	public LinkedNode280(I x)
	{
		setItem(x);
	}

	/**	Contents of node. <br>
		Analysis: Time = O(1) */
	public I item()
	{
		return item;
	}

	/**	The next node. <br>
		Analysis: Time = O(1) */
	public LinkedNode280<I> nextNode()
	{
		return nextNode;
	}
  
	/**	Set item equal to x. <br>
		Analysis: Time = O(1) 
		@param x item to replace the node's current item */
	public void setItem(I x)
	{
		item = x;
	}
  
	/**	Set nextNode equal to x. <br>
		Analysis: Time = O(1) 
		@param x node set as the next node */
	public void setNextNode(LinkedNode280<I> x)
	{
		nextNode = x;
	}
  
	/**	String representation suitable for output. <br>
		Analysis: Time = O(n), where n = number of nodes linked from this node */
	public String toString()
	{
		String result = item.toString();
		if (nextNode!=null)
			result += " " + nextNode;
		return result;
	}

	/**	A shallow clone of this object. <br> 
		Analysis: Time = O(1) */
	@SuppressWarnings("unchecked")
	public LinkedNode280<I> clone()
	{
		try
		{
			return (LinkedNode280<I>) super.clone();
		} catch (CloneNotSupportedException e)
		{
			// Should not occur: implements Cloneable 
			e.printStackTrace();
			return null;
		}
	}

} 
