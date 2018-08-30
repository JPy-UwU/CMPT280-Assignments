/* DuplicateItems280Exception.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This exception is used whenever two identical items are attempted to 
	be inserted into a set, where duplicates are not allowed */
public class DuplicateItems280Exception extends Container280Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public DuplicateItems280Exception(String message)
	{
		super(message);
	}
  
	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public DuplicateItems280Exception()
	{
		super("DuplicateItems280Exception thrown!");
	}

} 
