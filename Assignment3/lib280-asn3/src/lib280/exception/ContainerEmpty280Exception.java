/* ContainerEmpty280Exception.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This exception is used whenever there is an attempt to do
	an operation which requires an item in the container, but
	the container is empty. */
public class ContainerEmpty280Exception extends Container280Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public ContainerEmpty280Exception(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public ContainerEmpty280Exception()
	{
		super("ContainerEmpty280Exception thrown!");
	}

} 
