/* Membership280.java
 * ---------------------------------------------
 * Copyright (c) 2004, 2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.base;

/**	A Container class which includes a membership test has and 
	equality test membershipEquals */
public interface Membership280<I> extends Container280
{
	/**	
	 * 	Does the container contain the element 'y'?. 
	 *	@param y item whose presence is to be determined 
	 *  @return True if y is in the container, false otherwise.
	 */
	public boolean has(I y);
 
	/**	
	 * A method that determines whether two elements of type I
	 * are equal.  Comparison must be based on the contents
	 * of x and y (do not use reference comparison).
	 * 
	 * @param x item to be compared to y
	 * @param y item to be compared to x 
	 * @return Returns true if x is the same element as y, false otherwise.
 	 */
	public boolean membershipEquals(I x, I y);

} 
