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
	/**	Move the current position to the first (the default) or the next x, if it exists.
		@param x item being sought */
	public void search(I x);
  
	/**	Set searches to always start over. */
	public void restartSearches();
 
	/**	Set searches to continue from the next item. */
	public void resumeSearches();
}
