/* LinearIterator280.java
 * ---------------------------------------------
 * Copyright (c) 2004 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.base;

import lib280.exception.AfterTheEnd280Exception;
import lib280.exception.ContainerEmpty280Exception;

/**	A basic iterator for moving through a collection of items linearly. 
	It utilizes a cursor to keep track of the current item of the 
	sequence, and has functions to move forward and to the front of 
	the sequence. */
public interface LinearIterator280<I> extends Cursor280<I>
{

	/**	Is the current position before the start of the structure?. */
	public boolean before();
 
	/**	Is the current position after the end of the structure?. */
	public boolean after();
 
	/**	Advance one item in the data structure. 
	 * @precond  !after()
	 * @throws AfterTheEnd280Exception if the cursor is already in the after position and cannot be advanced.
     */
	public void goForth() throws AfterTheEnd280Exception; 

	/**	
	 * Go to the first item in the structure. 
	 * @precond !isEmpty()
	 * @throws ContainerEmpty280Exception if this container is empty.
	 */
	public void goFirst() throws ContainerEmpty280Exception;

	/**	Move to the position prior to the first element in the structure. */
	public void goBefore();

	/**	Move to the position after the last element in the structure. */
	public void goAfter();

} 
