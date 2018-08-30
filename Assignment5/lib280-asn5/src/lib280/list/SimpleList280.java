/* SimpleList280.java 
 * ---------------------------------------------
 * Copyright (c) 2004,2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */
 
package lib280.list;

import lib280.dictionary.Dict280;
import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;

/**	A simple list interface with methods to access, insert and delete 
	items at the front of the list.  It also has a test for empty 
	and a wipeOut method. */ 
public interface SimpleList280<I> extends Dict280<I>
{
	/**	Insert x as the first element in the list.
	 * @precond !isFull()
	 * @param x item to be inserted in the list 
	 * @throws ContainerFull280Exception if the list is full.
	 */ 
	public void insertFirst(I x) throws ContainerFull280Exception;

	/**	Return the first item. <br>
	 * @precond	!isEmpty() 
	 * @throws ContainerEmpty280Exception if the list is empty.
	 * @return the first item in the list.
	 */
	public I firstItem() throws ContainerEmpty280Exception;

	/**	Delete the first item from the list.
	 * @precond	!isEmpty() 
	 * @throws ContainerEmpty280Exception if the list is empty.
     */
	public void deleteFirst() throws ContainerEmpty280Exception;

	/**	Insert x as the last element in the list.
	 * @precond !isFull()
	 * @param x item to be inserted in the list 
	 * @throws ContainerFull280Exception if the list is full.
	 */ 
	public void insertLast(I x) throws ContainerFull280Exception;

	/**	Return the last item. <br>
	 * @precond	!isEmpty() 
	 * @throws ContainerEmpty280Exception if the list is empty.
	 */
	public I lastItem() throws ContainerEmpty280Exception;

	/**	Delete the last item from the list.
	 * @precond	!isEmpty()
	 * @throws ContainerEmpty280Exception if the list is empty.
     */
	public void deleteLast() throws ContainerEmpty280Exception;

	
	/**	A shallow clone of the current container. */
	public SimpleList280<I> clone() throws CloneNotSupportedException;

}
