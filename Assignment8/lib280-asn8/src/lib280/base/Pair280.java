/* Pair280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.base;

/**	This class represents a pair of objects. */
public class Pair280<T, U> implements Cloneable
{
	/**	The first item of the pair. */
	protected T firstItem;
  
	/**	The second item of the pair. */
	protected U secondItem;
  
	/**	Initialize the values of the Pair. <br>
		Analysis: Time = O(1) <br>
		@param v1 the first value to be stored
		@param v2 the second value to be stored */
	public Pair280(T v1, U v2)
	{
		firstItem = v1;
		secondItem = v2;
	}

	/**	The first item of the pair. <br> 
		Analysis: Time = O(1) */
	public T firstItem()
	{
		return firstItem;
	}
  
	/**	The second item of the pair. <br> 
		Analysis: Time = O(1) */
	public U secondItem()
	{
		return secondItem;
	}
  
	/**	Set firstItem to newItem. <br> 
		Analysis: Time = O(1) 
		@param newItem item to be stored as the first item */
	public void setFirstItem(T newItem)
	{
		firstItem = newItem;
	}
  
	/**	Set secondItem to newItem. <br> 
	 	Analysis: Time = O(1) 
	 	@param newItem item to be stored as the second item */
	public void setSecondItem(U newItem)
	{
		secondItem = newItem;
	}
  
	/**	A shallow clone of this pair. <br> 
		Analysis: Time = O(1) */
	@SuppressWarnings("unchecked")
	public Pair280<T, U> clone()
	{
		try
		{
			return (Pair280<T, U>) super.clone();
		} catch (CloneNotSupportedException e)
		{
			// Should not occur: Pair280 implements Cloneable 
			e.printStackTrace();
			return null;
		}
	}

	/**	String representation of the pair. <br> 
		Analysis: Time = O(1) */
	public String toString()
	{
		return "(" + firstItem + "," + secondItem + ")";
	}

} 
