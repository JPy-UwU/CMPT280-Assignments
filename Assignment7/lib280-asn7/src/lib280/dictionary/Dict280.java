/* Dict280.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.dictionary;

import lib280.base.*;

/**	A Dictionary class with search capabilities to set the 
	current item.  It also includes a LinearIterator to move 
	through the dictionary, and deleteItem to delete the current item. 
	It has the basic dictionary routines: has, 
	delete, insert, obtain, and isEmpty.  */
public interface Dict280<I> extends BasicDict280<I>, Searchable280<I>, Dispenser280<I>,
        LinearIterator280<I>, CursorSaving280
{
}
