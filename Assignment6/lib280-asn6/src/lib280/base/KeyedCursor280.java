/* KeyedCursor280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.base;

import lib280.exception.NoCurrentItem280Exception;


/**	A Cursor with a key.  Item refers to the current item, itemKey 
	is the key of the current item, and itemExists determines if 
	the cursor is presently referring to an item in the structure. */
public interface KeyedCursor280<K extends Comparable<? super K>, I> extends Cursor280<I>
{
	/**	The key of the current item.
	 *  @precond itemExists()
	 *  @throws NoCurrentItem280Exception if the cursor is not positioned at a valid item.
	 */
	public K itemKey() throws NoCurrentItem280Exception;

	/**	The current key-item pair. 
	 * @precond itemExists()
	 * @throws NoCurrentItem280Exception if the cursor is not positioned at a valid item.
	 */
	public Pair280<K, I> keyItemPair() throws NoCurrentItem280Exception;
} 
