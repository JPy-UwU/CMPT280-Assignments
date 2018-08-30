/* Searchable280.java
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.base;

/**	A Container class with a search routine and a cursor to 
	reference the item found.  It can be set to continue searches 
	from the next item or restart from the beginning of the structure. */
public interface Searchable280<I> extends Membership280<I>, Cursor280<I>
{
	/**	Move the cursor to the next occurrence of the item x in the data structure's
		linear ordering (if it exists).  If the item is not found, the cursor must be
		left in a state such that Cursor280::itemExists() would return False.
		
		The behaviour of search() is further modified by the restartSearches() and
		resumeSearches() methods defined below.  Each of these two methods invokes
		a different behaviour for search().  The behaviour used by search() depends on
		which of restartSearches() and resumeSearches() has been called most recently.
		
		Classes implementing this interface must keep track of which of restartSearches()
		and resumeSearches() has been called most recently.  The behaviour of search()
		then depends on that state.
		
		@param x item being sought 
		*/
	public void search(I x);
  
	/**	Causes search(x) to always start the search for an item equal to x 
		from the beginning of the data structure's linear ordering.  This behaviour
		persists until resumeSearches() is called.
		
		Using restartSearches(), only the first occurrence of x will ever be found.
			  
	  */
	public void restartSearches();
 
	/**	Causes search(x) to search for an item equal to x starting from the successor
	    of the current cursor position.   This behaviour persists until restartSearches()
	    is called.
	    
	    Using resumeSearches() allows multiple occurrences of x to be found in the
	    data structure.
	    */
	public void resumeSearches();
}
