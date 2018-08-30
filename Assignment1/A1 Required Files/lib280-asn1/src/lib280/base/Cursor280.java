/* Cursor280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.base;

import lib280.exception.NoCurrentItem280Exception;

/**	A cursor to keep track of the current item in a data structure. 
	It includes an itemExists routine to determine if the 
	cursor is presently referring to an item in the structure. */
public interface Cursor280<I> extends CursorPosition280, Cloneable
{
	/**	The current item. 
	 * @precond The cursor is at a valid item.
	 * @return Returns the item at the cursor if there is one.
	 * @throws NoCurrentItem280Exception when the cursor is not at a valid item.
	 */
	public I item() throws NoCurrentItem280Exception;
 
	/**	Is there a current item?. */
	public boolean itemExists();
} 
