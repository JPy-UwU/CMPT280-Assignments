/* InvalidStateUosException.java
 * ---------------------------------------------
 * Copyright (c) 2009 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

public class InvalidState280Exception extends Exception280 
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public InvalidState280Exception(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public InvalidState280Exception()
	{
		super("InvalidStateUosException thrown!");
	}

}
