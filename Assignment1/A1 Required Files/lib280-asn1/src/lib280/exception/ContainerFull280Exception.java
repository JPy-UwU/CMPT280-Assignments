/* ContainerFull280Exception.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.exception;

/**	This exception is used to signal that an insertion
	is being attempted into an already full container. */
public class ContainerFull280Exception extends Container280Exception
{
	/**	Create an exception with the specified message. <br> 
		Analysis: Time = O(1) */
	public ContainerFull280Exception(String message)
	{
		super(message);
	}

	/**	Create an exception with the default message. <br> 
		Analysis: Time = O(1) */
	public ContainerFull280Exception()
	{
		super("ContainerFull280Exception thrown!");
	}

} 
