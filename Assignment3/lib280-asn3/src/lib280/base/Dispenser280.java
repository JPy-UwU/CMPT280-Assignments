/* DispenserUos.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.base;

import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.NoCurrentItem280Exception;


/**	A Container class which keeps track of the current item as 
	insertions are made.  Only the current item can be deleted.  
	The current item depends on the type of dispenser. */
public interface Dispenser280<I> extends Container280, Cursor280<I>
{
	/**	Insert x into the data structure.
	 * @precond !isFull() and !has(x)
	 * @param x item to be inserted into the data structure
	 * @throws ContainerFull280Exception if the dispenser is already full.
	 * @throws DuplicateItems280Exception if the dispenser already contains x.
	 */
	public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception;
 
	/**	Delete current item from the data structure. 
	 * @precond	itemExists()
	 * @throws NoCurrentItem280Exception if the cursor is not currently positioned at a valid item.
	 */
	public void deleteItem() throws NoCurrentItem280Exception;
}
