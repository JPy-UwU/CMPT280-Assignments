// Mark Cantuba
// MJC862
// 11214496

package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.exception.BeforeTheStart280Exception;
import lib280.exception.ContainerEmpty280Exception;

/**	A LinkedIterator which has functions to move forward and back, 
	and to the first and last items of the list.  It keeps track of 
	the current item, and also has functions to determine if it is 
	before the start or after the end of the list. */
public class BilinkedIterator280<I> extends LinkedIterator280<I> implements BilinearIterator280<I>
{

	/**	Constructor creates a new iterator for list 'list'. <br>
		Analysis : Time = O(1) 
		@param list list to be iterated */
	public BilinkedIterator280(BilinkedList280<I> list)
	{
		super(list);
	}

	/**	Create a new iterator at a specific position in the newList. <br>
		Analysis : Time = O(1)
		@param newList list to be iterated
		@param initialPrev the previous node for the initial position
		@param initialCur the current node for the initial position */
	public BilinkedIterator280(BilinkedList280<I> newList, 
			LinkedNode280<I> initialPrev, LinkedNode280<I> initialCur)
	{
		super(newList, initialPrev, initialCur);
	}
    
	/**
	 * Move the cursor to the last element in the list.
	 * @precond The list is not empty.
	 */
	public void  goLast() throws ContainerEmpty280Exception
	{
		// If the list is empty, throw a new Container Empty exception
		if (this.list.isEmpty()) {
			throw new ContainerEmpty280Exception("Cannot move cursor to last item of empty list!");
		}
		// Set the current position to refer to the tail node of the list.
		this.cur = this.list.tail;

		// Set the previous node to be the value before the current node. Has to be type casted
		// to BilinkedNode280 in order to gain access to previousNode Value.
		this.prev = ((BilinkedNode280<I>) this.list.tail).previousNode();

	}

	/**
	 * Move the cursor one element closer to the beginning of the list
	 * @precond !before() - the cursor cannot already be before the first element.
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		// If the cursor is pointed at the before position, throw a Before start exception.
		if (this.list.before()) {
			throw new BeforeTheStart280Exception("Cursor is on before position, and cannot move " +
					"back any further");
		}
		// If the cursor is pointed at the head of the list, set the current and previous value
		// to null since the before value's previous node is null.
		if (this.cur == this.list.head) {
			this.cur = null;
			this.prev = null;
		}
		else {
			// Set the current cursor position as previous' current previous node
			this.cur = ((BilinkedNode280<I>) this.cur).previousNode();

			// Set previous node to be the new current's prev. node.
			this.prev = ((BilinkedNode280<I>) this.cur).previousNode();
		}
	 }

	/**	A shallow clone of this object. <br> 
	Analysis: Time = O(1) */
	public BilinkedIterator280<I> clone()
	{
		return (BilinkedIterator280<I>) super.clone();
	}


} 
