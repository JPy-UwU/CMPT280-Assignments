/* KeyedDictUos.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.dictionary;

import lib280.base.CursorSaving280;
import lib280.base.Keyed280;
import lib280.base.KeyedLinearIterator280;
import lib280.exception.InvalidArgument280Exception;
import lib280.exception.NoCurrentItem280Exception;

/**	An interface for KeyedUos dictionary class with the usual routines for inserting, 
	deleting, setting and obtaining items by key.  It also includes 
	a KeyedLinearIteratorUos to move through the dictionary, and deleteItem 
	to delete the current item. It also has a search procedure. */

public interface KeyedDict280<K extends Comparable<? super K>, I extends Keyed280<K>>
				extends KeyedBasicDict280<K, I>, KeyedLinearIterator280<K,I>, CursorSaving280
{
	
	/**	
	 * Move to the item with key k or else set to !itemExists.
	 * @param k key being sought 
	 */
	public void search(K k);

	/**	
	 * Move to the first item with key greater than or equal to k.
	 * @param k key being sought 
	 */
	public void searchCeilingOf(K k);

	/**	
	 *  Delete the current item from the data structure. 
	 *  @precond itemExists()
	 *  @throws NoCurrentItem280Exception if the cursor is not positioned at a valid item.
	 */
	public void deleteItem() throws NoCurrentItem280Exception;

	/**	
	 * Replace the current item with another item having the same key. 
	 * @precond itemExists() 
	 * @precond x.key().compareTo(itemKey()) == 0
	 * @param x item to replace the current item
	 * @throws NoCurrentItem280Exception if the cursor is not positioned at a valid item.
	 * @throws InvalidArgument280Exception if the key of x is not equal to the key of the item at the cursor.
	 */
	public void setItem(I x) throws NoCurrentItem280Exception, InvalidArgument280Exception;
} 
