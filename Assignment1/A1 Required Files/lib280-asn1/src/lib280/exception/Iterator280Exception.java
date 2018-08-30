/* Iterator280Exception.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This general exception is the parent of all exceptions that 
	can occur when using an iterator. */
public class Iterator280Exception extends Exception280
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public Iterator280Exception(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public Iterator280Exception()
	{
		super("Iterator280Exception thrown!");
	}

}
