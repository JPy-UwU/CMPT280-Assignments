/* KeyedBasicDict280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.dictionary;

import lib280.base.Keyed280;
import lib280.exception.ContainerFull280Exception;
import lib280.exception.DuplicateItems280Exception;
import lib280.exception.ItemNotFound280Exception;

/**	The interface for a basic container that stores keyed items.  
	All items must be implementations of Keyed280Items, i.e., must be
	keyed.  Items can be inserted, and be deleted and obtained by key 
	value.  All classes that implement this interface will be sets. */ 
public interface KeyedBasicDict280<K extends Comparable<? super K>, I extends Keyed280<K>>
{
	/**	
	 * Insert x into the dictionary. 
	 * @precond !isFull() 
	 * @precond	!has(x.key())
	 * @throws DuplicateItems280Exception if an item with key x.key() already exist in the dictionary.
	 * @throws ContainerFull280Exception if the dictionary is full.
	 * @param x item to be inserted into the dictionary 
	 */
	public void insert(I x) throws ContainerFull280Exception, DuplicateItems280Exception;

	/**	
	 * Set x to replace an item having the same key value as x. 
	 * @precond has(x.key())
	 * @throws ItemNotFound280Exception if there is no item with key k.
	 * @param x item to replace another item with the same key value 
	 */
	public void set(I x) throws ItemNotFound280Exception;
	
	/**	
	 * Does the structure contain key k?.
	 * @param k key whose presence is to be determined 
	 */
	public boolean has(K k);
 
	/**	
	 * The item with key k from the container.
	 *  @precond has(k)
	 *  @throws ItemNotFound280Exception if there is no item with key k.
	 *  @param k key of the item to be obtained from the dictionary 
	 */
	public I obtain(K k) throws ItemNotFound280Exception;
 
	/**	
	 * Delete the item with key k. 
	 *  @precond has(k)
	 *  @throws ItemNotFound280Exception if there is no item with key k.
	 *  @param k the key of the item to be deleted 
	 */
	public void delete(K k) throws ItemNotFound280Exception;
} 
