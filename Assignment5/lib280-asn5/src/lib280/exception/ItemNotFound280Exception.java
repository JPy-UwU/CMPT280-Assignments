/* ItemNotFound280Exception.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This exception is thrown when an item is needed for an operation
	but the item was not found. */
public class ItemNotFound280Exception extends Container280Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public ItemNotFound280Exception(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public ItemNotFound280Exception()
	{
		super("ItemNotFound280Exception thrown!");
	}

}
