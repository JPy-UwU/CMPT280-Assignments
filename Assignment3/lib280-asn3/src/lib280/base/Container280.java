/* Container280.java
 * ---------------------------------------------
 * Copyright (c) 2004, 2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 

package lib280.base;


/**	A container class with functions to test for empty or full, and 
	procedure clear to remove all items. */
public interface Container280 extends Cloneable
{
	/**	Is the data structure empty?. */
	public boolean isEmpty();
 
	/**	Is the data structure full?. */
	public boolean isFull();
 
	/**	Remove all items from the data structure. */
	public void clear();
 
} 
