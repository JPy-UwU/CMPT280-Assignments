/* BeforeTheStart280Exception.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This exception is used to signal that an attempt was made to go forth 
	when the current iteration is already after the end of the structure. */
public class BeforeTheStart280Exception extends Iterator280Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public BeforeTheStart280Exception(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public BeforeTheStart280Exception()
	{
		super("BeforeTheStart280Exception thrown!");
	}

}
