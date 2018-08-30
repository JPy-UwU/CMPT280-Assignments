package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
import lib280.base.Pair280;
import lib280.exception.*;

/**	This list class incorporates the functions of an iterated 
	dictionary such as has, obtain, search, goFirst, goForth, 
	deleteItem, etc.  It also has the capabilities to iterate backwards 
	in the list, goLast and goBack. */
public class BilinkedList280<I> extends LinkedList280<I> implements BilinearIterator280<I>
{
	/* 	Note that because firstRemainder() and remainder() should not cut links of the original list,
		the previous node reference of firstNode is not always correct.
		Also, the instance variable prev is generally kept up to date, but may not always be correct.  
		Use previousNode() instead! */

	/**	Construct an empty list.
		Analysis: Time = O(1) */
	public BilinkedList280()
	{
		super();
	}

	/**
	 * Create a BilinkedNode280 this Bilinked list.  This routine should be
	 * overridden for classes that extend this class that need a specialized node.
	 * @param item - element to store in the new node
	 * @return a new node containing item
	 */
	protected BilinkedNode280<I> createNewNode(I item)
	{
		return new BilinkedNode280<I>(item);
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insertFirst(I x) 
	{
		BilinkedNode280<I> newNode = createNewNode(x);
		newNode.setNextNode(this.head);
		newNode.setPreviousNode(null);
		
		// If the list was empty, new node becomes tail as well.
		if(this.isEmpty()) {
			this.tail = newNode;
		}
		else {
			// Otherwise, make the old head point back to the new head.
			((BilinkedNode280<I>)this.head).setPreviousNode(newNode);
		}
		// If the cursor was on the first item, then the cursor's predecessor has to be the new first item.
		if( this.position == this.head ) 
			this.prevPosition = newNode;
		
		// New node becomes the head.
		this.head = newNode;
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	public void insertBefore(I x) throws InvalidState280Exception {
		if( this.before() ) throw new InvalidState280Exception("Cannot insertBefore() when the cursor is already before the first element.");
		
		// If the item goes at the beginning or the end, handle those special cases.
		if( this.head == position ) {
			insertFirst(x);  // special case - inserting before first element
		}
		else if( this.after() ) {
			insertLast(x);   // special case - inserting at the end
		}
		else {
			// Otherwise, insert the node between the current position and the previous position.
			BilinkedNode280<I> newNode = createNewNode(x);
			newNode.setNextNode(position);
			newNode.setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			prevPosition.setNextNode(newNode);
			((BilinkedNode280<I>)this.position).setPreviousNode(newNode);
			
			// since position didn't change, but we changed it's predecessor, prevPosition needs to be updated to be the new previous node.
			prevPosition = newNode;			
		}
	}
	
	
	/**	Insert x before the current position and make it current item. <br>
		Analysis: Time = O(1)
		@param x item to be inserted before the current position */
	public void insertPriorGo(I x) 
	{
		this.insertBefore(x);
		this.goBack();
	}

	/**	Insert x after the current item. <br>
		Analysis: Time = O(1) 
		@param x item to be inserted after the current position */
	public void insertNext(I x) 
	{
		if (isEmpty() || before())
			insertFirst(x); 
		else if (this.position==lastNode())
			insertLast(x); 
		else if (after()) // if after then have to deal with previous node  
		{
			insertLast(x); 
			this.position = this.prevPosition.nextNode();
		}
		else // in the list, so create a node and set the pointers to the new node 
		{
			BilinkedNode280<I> temp = createNewNode(x);
			temp.setNextNode(this.position.nextNode());
			temp.setPreviousNode((BilinkedNode280<I>)this.position);
			((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode(temp);
			this.position.setNextNode(temp);
		}
	}

	/**
	 * Insert a new element at the end of the list
	 * @param x item to be inserted at the end of the list 
	 */
	public void insertLast(I x) 
	{
		if (this.isEmpty())
			this.insertFirst(x); 
		else	// make a new node and insert it at after the last node
		{
			BilinkedNode280<I> temp = this.createNewNode(x);
			this.tail.setNextNode(temp); 
			temp.setPreviousNode((BilinkedNode280<I>)this.tail);
			this.tail = temp;
			if (this.after()) 
				this.prevPosition = this.tail;
		}
	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	public void deleteItem() throws NoCurrentItem280Exception
	{
		if (!this.itemExists())
			throw new NoCurrentItem280Exception("Cannot delete an item that does not exist.");  
		
		if (this.position==this.head)
			this.deleteFirst(); 
		else // have to delete the node from the list and update the pointers of prev and cur 
		{ 
			this.prevPosition = ((BilinkedNode280<I>)this.position).previousNode();  // this line may not be necessary
			this.prevPosition.setNextNode(this.position.nextNode());
			if(this.position.nextNode() != null)
				((BilinkedNode280<I>) this.position.nextNode()).setPreviousNode((BilinkedNode280<I>)this.prevPosition);
			if (this.position==this.tail)
				this.tail = this.prevPosition;
			this.position = this.position.nextNode();
		}     
	}

	
	@Override
	public void delete(I x) throws ItemNotFound280Exception {
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");

		// Save cursor position
		LinkedIterator280<I> savePos = this.currentPosition();
		
		// Find the item to be deleted.
		search(x);
		if( !this.itemExists() ) throw new ItemNotFound280Exception("Item to be deleted wasn't in the list.");

		// If we are about to delete the item that the cursor was pointing at,
		// advance the cursor in the saved position, but leave the predecessor where
		// it is because it will remain the predecessor.
		if( this.position == savePos.cur ) savePos.cur = savePos.cur.nextNode();
		
		// If we are about to delete the predecessor to the cursor, the predecessor 
		// must be moved back one item.
		if( this.position == savePos.prev ) {
			
			// If savePos.prev is the first node, then the first node is being deleted
			// and savePos.prev has to be null.
			if( savePos.prev == this.head ) savePos.prev = null;
			else {
				// Otherwise, Find the node preceding savePos.prev
				LinkedNode280<I> tmp = this.head;
				while(tmp.nextNode() != savePos.prev) tmp = tmp.nextNode();
				
				// Update the cursor position to be restored.
				savePos.prev = tmp;
			}
		}
				
		// Unlink the node to be deleted.
		if( this.prevPosition != null)
			// Set previous node to point to next node.
			// Only do this if the node we are deleting is not the first one.
			this.prevPosition.setNextNode(this.position.nextNode());
		
		if( this.position.nextNode() != null )
			// Set next node to point to previous node 
			// But only do this if we are not deleting the last node.
			((BilinkedNode280<I>)this.position.nextNode()).setPreviousNode(((BilinkedNode280<I>)this.position).previousNode());
		
		// If we deleted the first or last node (or both, in the case
		// that the list only contained one element), update head/tail.
		if( this.position == this.head ) this.head = this.head.nextNode();
		if( this.position == this.tail ) this.tail = this.prevPosition;
		
		// Clean up references in the node being deleted.
		this.position.setNextNode(null);
		((BilinkedNode280<I>)this.position).setPreviousNode(null);
		
		// Restore the old, possibly modified cursor.
		this.goPosition(savePos);
		
	}
	/**
	 * Remove the first item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteFirst() throws ContainerEmpty280Exception
	{
		if (this.isEmpty())
			throw new ContainerEmpty280Exception("Cannot delete an item from an empty list.");
		
		super.deleteFirst(); 
		if (!this.isEmpty())
			((BilinkedNode280<I>)this.head).setPreviousNode(null);
	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	public void deleteLast() throws ContainerEmpty280Exception
	{
		if (this.isEmpty())
			throw new ContainerEmpty280Exception("Cannot delete last item of an empty list.");
		
		if (this.head==this.tail)
			deleteFirst(); 
		else // delete the last node and update prev and cur if necessary
		{ 
			// There are at least two nodes...
			
			// If the cursor is in the after position, the prevPosition must be updated.
			if (this.prevPosition==this.tail) {
				this.prevPosition = ((BilinkedNode280<I>)this.tail).previousNode();
				
			}
			// If the cursor is on the last element, move the cursor to the
			// previous element.
			else if (this.position==this.tail) {
				this.prevPosition = ((BilinkedNode280<I>)this.prevPosition).previousNode();
				this.position = ((BilinkedNode280<I>)this.position).previousNode();
			}
			this.tail = ((BilinkedNode280<I>)this.tail).previousNode();
			
			if (this.tail!=null) 
				this.tail.setNextNode(null);
		}
	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
		if(this.isEmpty()) throw new ContainerEmpty280Exception("Cannot move to the end of an empty list.");
		
		this.position = this.tail;
		this.prevPosition = ((BilinkedNode280<I>)this.position).previousNode();
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
		if (this.before()) 
			throw new BeforeTheStart280Exception("Cannot go back since already before list.");
		
		if (this.after()) // move to the last node 
			this.goLast(); 
		else
		{ 
			this.position = ((BilinkedNode280<I>)this.position).previousNode();     
			if (this.position != null)
			{
				if (this.position == this.head)
					this.prevPosition = null; 
				else
					this.prevPosition = ((BilinkedNode280<I>)this.position).previousNode();
			}
		}
	}

	/**	Iterator for list initialized to first item. 
		Analysis: Time = O(1) 
	*/
	public BilinkedIterator280<I> iterator()
	{
		return new BilinkedIterator280<I>(this);
	}

	/**	Go to the position in the list specified by c. <br>
		Analysis: Time = O(1) 
		@param c position to which to go */
	@SuppressWarnings("unchecked")
	public void goPosition(CursorPosition280 c)
	{
		if (!(c instanceof BilinkedIterator280))
			throw new InvalidArgument280Exception("The cursor position parameter" 
					    + " must be a BilinkedIterator280<I>");
		BilinkedIterator280<I> lc = (BilinkedIterator280<I>) c;
		this.position = lc.cur;
		this.prevPosition = lc.prev;
	}

	/**	The current position in this list. 
		Analysis: Time = O(1) */
	public BilinkedIterator280<I> currentPosition()
	{
		return  new BilinkedIterator280<I>(this, this.prevPosition, this.position);
	}

	
  
	/**	A shallow clone of this object. 
		Analysis: Time = O(1) */
	public BilinkedList280<I> clone() throws CloneNotSupportedException
	{
		return (BilinkedList280<I>) super.clone();
	}


	/* Regression test. */
	public static void main(String[] args) {
		//Probably doesn't achieve 100% coverage, but comes pretty close.
		
		BilinkedList280<Integer> L = new BilinkedList280<Integer>();
		
		// test isEmpty, isFull, insert, insertLast, insert,
		// toString() (which implicitly tests iteration to some extent)
		
		System.out.println(L);
		
		System.out.print("List should be empty...");
		if( L.isEmpty() ) System.out.println("and it is.");
		else System.out.println("ERROR: and it is *NOT*.");
		
		try {
			L.goFirst();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch(ContainerEmpty280Exception e) {
			System.out.println("Caught expected exception.  OK!");
		}
		
		
		try {
			L.goLast();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch(ContainerEmpty280Exception e) {
			System.out.println("Caught expected exception.  OK!");
		}


		
		L.insert(5);
		L.insert(4);
		L.insertLast(3);
		L.insertLast(10);
		//L.insertFirst(3);
		//L.insertFirst(4);
		//L.insertFirst(5);
		
		
		L.insertFirst(2);
		
		System.out.print("List should be 'not full'...");
		if( !L.isFull() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.println("List should be: 2, 4, 5, 3, 10, ");
		System.out.print(  "     and it is: ");
		System.out.println(L);

		// Test delete methods
		L.delete(5);
		System.out.println(L);

		L.deleteFirst();
		System.out.println(L);

		L.deleteLast();
		System.out.println(L);
		
		System.out.println("List should be: 4, 3,");
		System.out.print(  "     and it is: ");
		System.out.println(L);
		
		// Test firstItem/lastItem
		System.out.print("firstItem should be 4 ....");
		if( L.firstItem() == 4 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("lastItem should be 3 ....");
		if( L.lastItem() == 3 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		// test item(), goFirst(), goForth(), after(), goBefore(), before()
		L.insert(5);
		System.out.println("List should be: 5, 4, 3,");
		System.out.print(  "     and it is: ");
		System.out.println(L);

		L.goFirst();
		System.out.print("cursor should be at 5 ....");
		if( L.item() == 5 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
	
		L.goForth();
		System.out.print("cursor should be at 4 ....");
		if( L.item() == 4 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		L.goForth();
		System.out.print("cursor should be at 3 ....");
		if( L.item() == 3 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		L.goForth();
		System.out.print("cursor should be 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("itemExists() should be false ....");
		if( !L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		L.goBefore();
		System.out.print("cursor should be 'before' ....");
		if( L.before() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("itemExists() should be false ....");
		if( !L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		L.goAfter();
		System.out.print("cursor should be 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("itemExists() should be false ....");
		if( !L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("has(5) should be true ....");
		if( L.has(5) ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.print("has(4) should be true ....");
		if( L.has(4) ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.print("has(3) should be true ....");
		if( L.has(3) ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.print("has(2) should be false ....");
		if( !L.has(2) ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		L.insertLast(3);
		System.out.println("List should be: 5, 4, 3, 3");
		System.out.print(  "     and it is: ");
		System.out.println(L);
	
		L.search(3);
		System.out.print("itemExists() should be true ....");
		if( L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("cursor should be at 3 ....");
		if( L.item() == 3) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
	
		L.search(5);
		System.out.print("itemExists() should be true ....");
		if( L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("cursor should be at 5 ....");
		if( L.item() == 5 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		L.resumeSearches();
		
		L.search(3);
		System.out.print("itemExists() should be true ....");
		if( L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("cursor should be at 3 ....");
		if( L.item() == 3) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		L.search(3);
		System.out.print("itemExists() should be true ....");
		if( L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("cursor should be at 3 ....");
		if( L.item() == 3) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		L.search(3);
		System.out.print("itemExists() should be false ....");
		if( !L.itemExists() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		
		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		L.restartSearches();
	
		// Test obtain
		System.out.print("obtain(4) should result in 4 ....");
		if( L.obtain(4) == 4) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
	
		System.out.println("List should be: 5, 4, 3, 3");
		System.out.print(  "     and it is: ");
		System.out.println(L);

		
		// Test deleting from an empty list.
		L.delete(5);
		System.out.println("Deleted 5");
		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.println("List should be: 4, 3, 3");
		System.out.print(  "     and it is: ");
		System.out.println(L);

		L.delete(4);
		System.out.println("Deleted 4");
		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		L.delete(3);
		System.out.println("Deleted 3");
		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		L.delete(3);
		System.out.println("Deleted 3");	
		System.out.print("List should be empty...");
		if( L.isEmpty() ) System.out.println("and it is.");
		else System.out.println("ERROR: and it is *NOT*.");

		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		// Test preconditions
		System.out.println("Deleting first item from empty list.");
		try {
			L.deleteFirst();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception.  OK!");
		}
		
		System.out.println("Deleting last item from empty list.");
		try {
			L.deleteLast();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception. OK!");
		}

		System.out.println("Deleting 3 from empty list.");
		try {
			L.delete(3);
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception. OK!");
		}
		
		System.out.println("Getting first item from empty list.");
		try {
			L.firstItem();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception. OK!");
		}

		System.out.println("Trying to goFirst() with empty list.");
		try {
			L.goFirst();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception. OK!");
		}

		
		System.out.println("Getting last item from empty list.");
		try {
			L.lastItem();
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerEmpty280Exception e ) {
			System.out.println("Caught exception. OK!");
		}


		
		L.insert(5);
		System.out.println("Deleting 3 from list in which it does not exist.");
		try {
			L.delete(3);
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ItemNotFound280Exception e ) {
			System.out.println("Caught exception. OK!");
		}
		
		
		L.insert(4);
		L.insert(3);
		L.insert(2);
		L.insert(1);

		System.out.println("List should be: 1, 2, 3, 4, 5 ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
	
		// firstItem(), lastItem(), goForth()
		L.search(5);
		System.out.print("cursor should be at 5 ....");
		if( L.item() == 5 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");		
		
		L.goForth();
	
		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
	
		
		System.out.println("Trying to iterate past last item.");
		try {
			L.goForth();			
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( AfterTheEnd280Exception e ) {
			System.out.println("Caught exception. OK!");
		}
		
		L.clear();
		
		System.out.print("List should be empty...");
		if( L.isEmpty() ) System.out.println("and it is.");
		else System.out.println("ERROR: and it is *NOT*.");
		
		L.insert(5);
		L.delete(5);
		L.insert(5);
		L.deleteFirst();
		L.insert(5);
		L.deleteLast();

		System.out.print("List should be empty...");
		if( L.isEmpty() ) System.out.println("and it is.");
		else System.out.println("ERROR: and it is *NOT*.");
		
		
		L.insert(5);
		L.insert(4);
		L.insert(3);
		L.insert(2);
		L.insert(1);
		System.out.println("List should be: 1, 2, 3, 4, 5 ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
	
		// Test insertBefore when cursor is at first element.
		L.goFirst();
		L.insertBefore(10);
		System.out.println("List should be: 10, 1, 2, 3, 4, 5 ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
	
		// Test insertBefore when cursor is after last element.
		L.goAfter();
		L.insertBefore(20);
		System.out.println("List should be: 10, 1, 2, 3, 4, 5, 20 ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
	
		// Test insertBefore when cursor is at the last element.
		L.search(20);
		L.insertBefore(30);
		System.out.println("List should be: 10, 1, 2, 3, 4, 5, 30, 20 ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
		
		// Test insertBefore for an internal elemement.
		L.search(4);
		L.insertBefore(40);
		System.out.println("List should be: 10, 1, 2, 3, 40, 4, 5, 30, 20 ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
		
		// Test for exception when insertBefore is called when before() is true.
		L.goBefore();
		try {
			L.insertBefore(100);
			System.out.println("ERROR: insertBefore() with before() == true, exception should have been thrown, but wasn't.");			
		}
		catch( InvalidState280Exception e) {
			System.out.println("Caught expected exception. OK!");		
		}
		
	
		
		// Print list in reverse order to verify prev links.
		System.out.println("Reverse List should be: 20, 30, 5, 4, 40, 3, 2, 1, 10,");
		System.out.print("And it is:              ");
		L.goLast();
		while(L.itemExists()) {
			System.out.print(L.item() + ", ");
			L.goBack();
		}
		System.out.println();
		
		
		
		
		// Test search for simple integer list
		L.goAfter();
		L.search(40);
		if( !L.itemExists() || (L.itemExists() && L.item() != 40) )
			System.out.println("Error: 40 not found by search() when it should be.");
		
		// Test deleteLast() when cursor is in on the last element
		L.search(20);		
		if(!L.itemExists() || L.item() != 20) {
			System.out.println("Error: Cursor should be on 20 but it isn't.");
		}
		L.deleteLast();
		
		if( !L.itemExists() || L.item() != 30 ) {
			System.out.println("Error: Cursor should be on 30 but it isnt.");
		}
		if( L.prevPosition.item() != 5) {
			System.out.println("Error: prevPosition should be on 5 but it isnt.");
		}
		
		// Test deleteLast when cursor is in the "after" position and
		// make sure the cursor gets updated correctly.
		L.goAfter();
		L.deleteLast();  // This should delete 30 leaving 5 as the last element
		if( !L.after() ) System.out.println("Error: Cursor should be in the 'after' position but it isn't.");
		if( L.prevPosition.item() != 5)
			System.out.println("Error: prevPosition should be on 5 but it isn't.");
		
		
		
		
		
		
		// Test for searching for compound objects in a list.
		BilinkedList280<Pair280<Integer,Double>> T = new BilinkedList280<Pair280<Integer,Double>>();
		Pair280<Integer,Double> p = new Pair280<Integer,Double>(42, 10.0);
		Pair280<Integer,Double> q = new Pair280<Integer,Double>(42, 10.0);
		T.insert(p);
		T.goAfter();
		T.search(p);		
		if( !T.itemExists() )
			System.out.println("Error: search for same compound non-comparable object in T failed when it should not have.");
	
		
		
		
		
		
		// This test should fail, because p and q are not comparable.
		T.goAfter();
		T.search(q);
		if( T.itemExists() )
			System.out.println("Error: search for equal (but not actually the same) compound non-comparable object in T succeeded when it should not have.");
	
		class myPair extends Pair280<Integer,Double> implements Comparable<myPair> {

			public myPair(Integer v1, Double v2) {
				super(v1, v2);
			}
			
			public int compareTo(myPair other) {
				if( this.firstItem < other.firstItem )
					return -1;
				else if( this.firstItem > other.firstItem)
					return 1;
				else return 0;
			}
		}
		
		BilinkedList280<myPair> S = new BilinkedList280<myPair>();
		myPair x = new myPair(42, 10.0);
		myPair y = new myPair(42, 10.0);
		S.insert(x);
		
		S.goAfter();
		S.search(x);
		if( !S.itemExists() )
			System.out.println("Error: search for same compound comparable object in T failed when it should not have.");

		S.goAfter();
		S.search(y);
		if( !S.itemExists() )
			System.out.println("Error: search for equal (but not actually the same) compound comparable object in T failed when it should not have.");
	
		
	}
} 
