/* NoCurrentItem280Exception.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This exception is used whenever an operation needs a 
	current item, but there is no current item. */
public class NoCurrentItem280Exception extends Container280Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public NoCurrentItem280Exception(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public NoCurrentItem280Exception()
	{
		super("NoCurrentItem280Exception thrown!");
	}

} 
