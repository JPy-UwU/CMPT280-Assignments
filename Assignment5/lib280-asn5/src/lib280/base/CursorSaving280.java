/* CursorSaving280.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.base;

/**	Interface to obtain the current position of the cursor of a collection,
	and to go to a specified cursor position in the collection. */
public interface CursorSaving280
{
	/**	The current position. */
	public CursorPosition280 currentPosition();
 
	/**	Go to position c.
		@param c position to which to go */
	public void goPosition(CursorPosition280 c);
 
}
