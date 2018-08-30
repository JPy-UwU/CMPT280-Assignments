/* Exception280.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This exception is parent of all Uos exceptions. */
public class Exception280 extends RuntimeException
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public Exception280(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public Exception280()
	{
		super("Exception280 thrown!");
	}

} 
