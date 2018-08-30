/* KeyedLinearIterator280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.base;

/**	A LinearIterator for keyed linear sequences.  It keeps track of 
	the current position and can be moved forward or to the front. */
public interface KeyedLinearIterator280<K extends Comparable<? super K>, I> 
				extends KeyedCursor280<K, I>, LinearIterator280<I>
{
} 
  


  
