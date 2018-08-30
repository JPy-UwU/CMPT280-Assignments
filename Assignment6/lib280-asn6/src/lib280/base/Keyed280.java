/* KeyedUos.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.base;

/**	An interface that includes the function key() of type K where
	objects of type K are comparable. */
public interface Keyed280<K extends Comparable<? super K>>
{
	/**	Key of the item */
	public K key();
}
