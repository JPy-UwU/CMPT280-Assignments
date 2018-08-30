// Mark Cantuba
// MJC862
// 11214496

package lib280.list;


import lib280.base.BilinearIterator280;
import lib280.base.CursorPosition280;
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
	@Override
	protected BilinkedNode280<I> createNewNode(I item)
	{
		return new BilinkedNode280<I>(item);
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	@Override
	public void insertFirst(I x)
	{
		// Create a new Bi-linked node of item type I
		BilinkedNode280<I> newNode = createNewNode(x);

		// If the bi-linked list is empty, set head and tail to be the new node
		if (this.isEmpty()) {
			this.head = newNode;
			this.tail = newNode;
		}
		else {
			// Set the current head's previous node to be the new node.
			((BilinkedNode280<I>) this.head).previousNode = newNode;
			// In order to set the current head's previous node to the new node, we need to typecast this.head
            // to BilinkedNode in order to access setPreviousNode method, since this.head is of type LinkedNode.
            newNode.setNextNode(this.head);
			// If the position of the cursor is pointed at head, then set its previous position to be the new Node
			if (this.position == this.head) {
				this.prevPosition = newNode;
			}
			// Finally, set the new head to be the newNode.
            this.head = newNode;
		}
	}

	/**
	 * Insert element at the beginning of the list
	 * @param x item to be inserted at the beginning of the list 
	 */
	@Override
	public void insert(I x) 
	{
		this.insertFirst(x);
	}

	/**
	 * Insert an item before the current position.
	 * @param x - The item to be inserted.
	 */
	@Override
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
			newNode.setPreviousNode(((BilinkedNode280<I>)this.position).previousNode);
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
	@Override
	public void insertLast(I x) 
	{
	    // Create a new node
	    BilinkedNode280<I> newNode = new BilinkedNode280<>(x);
	    // If the cursor is in the after position, set the previous position to be the new node.


	    // if the list is empty, set head and tail to be the new node
		if (this.isEmpty()) {
		    this.head = newNode;
		    this.tail = newNode;
        }
        else {
            // set current tail's next node to be the new node
            this.tail.setNextNode(newNode);

            // set newNode's previous node to be the tail. has to be type casted
            newNode.setPreviousNode((BilinkedNode280<I>) this.tail);

            // set the new tail to be the new node.
            this.tail = newNode;
        }
		if (this.after()) {
			this.prevPosition = newNode;
		}
	}

	/**
	 * Delete the item at which the cursor is positioned
	 * @precond itemExists() must be true (the cursor must be positioned at some element)
	 */
	@Override
	public void deleteItem() throws NoCurrentItem280Exception
	{
        // If cursor is pointing to null, throw there is no current item, so throw and exception
		if (this.position == null) {
			throw new NoCurrentItem280Exception("Cursor is pointing to a null value.");
		}

		// if cursor is pointed to head, delete first item
		if (this.position == this.head || this.head == this.tail) {
			this.deleteFirst();
		}

		// if cursor is on the tail node, delete the last item
		else if (this.position == this.tail) {
			this.deleteLast();
		}
		// otherwise, get the item() of the current cursor position, then use the delete method to delete the item.
		else {
			// First, set the current cursor position to be the nextNode of the current node in position
			this.position = this.position.nextNode;
			// Next, set the previous' position  new next node to be the new position value
			((BilinkedNode280<I>) this.position).setPreviousNode((BilinkedNode280<I>) this.prevPosition);
			// Lastly, set the previous node's next value to be the new position value
			this.prevPosition.setNextNode(this.position);
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
	@Override
	public void deleteFirst() throws ContainerEmpty280Exception
	{
        // If the list is empty, we couldn't delete from an empty list. Throw and exception
		if (this.isEmpty()) {
			throw new ContainerEmpty280Exception("Bilinked List Cannot be empty!");
		}
		// If there is only 1 node in the list, set head to null
		if ( this.head == this.tail) {
		    this.head = null;
		    this.tail = null;
		    this.position = null;
		    this.prevPosition = null;
		}
		else {
			// set the head to be the next node after the old head
			this.head = this.head.nextNode;
			// set the head's previous node to be null
			((BilinkedNode280<I>) this.head).setPreviousNode(null);
			// set the new position value to be the new head
			this.position = this.head;
		}

	}

	/**
	 * Remove the last item from the list.
	 * @precond !isEmpty() - the list cannot be empty
	 */
	@Override
	public void deleteLast() throws ContainerEmpty280Exception
	{
        // If list is empty, throw a Container empty exception
        if (this.isEmpty()) {
            throw new ContainerEmpty280Exception("Deleting item from an empty node!");
        }
        // if the cursor is currently on the tail of the list
        if (this.position == this.tail) {
            // set the current cursor position to null, which is the after position
            this.position = ((BilinkedNode280<I>) this.position).previousNode;
            // Set the previous position to be the current tail's previous node
            this.prevPosition = ((BilinkedNode280<I>) this.position).previousNode;
        }
        // set the tail to be the old tail's previous node
        this.tail = ((BilinkedNode280<I>) this.tail).previousNode;
        // set the new tail's next value to null.
        this.tail.setNextNode(null);
	}

	
	/**
	 * Move the cursor to the last item in the list.
	 * @precond The list is not empty.
	 */
	public void goLast() throws ContainerEmpty280Exception
	{
        // If list is empty, we cannot go to last item since there is no last item
        if (this.isEmpty()) {
            throw new ContainerEmpty280Exception("The list is empty and there is nothing the cursor point to!");
        }
        // set current cursor position to be the current tail
        this.position = this.tail;
        // set the previous position to be the previous node's tail
        this.prevPosition = ((BilinkedNode280<I>) this.tail).previousNode;
	}
  
	/**	Move back one item in the list. 
		Analysis: Time = O(1)
		@precond !before() 
	 */
	public void goBack() throws BeforeTheStart280Exception
	{
        // if the cursor is on the before position, throw a before the start exception.
        if (this.before()) {
            throw new BeforeTheStart280Exception("The cursor is on the before position and cannot move back any further!");
        }
        // if the cursor is on the head, set previous to null and set current to null
        else if (this.head == this.position) {
            this.position = null;
            this.prevPosition = null;
        }
        else {
            this.position = ((BilinkedNode280<I>) this.position).previousNode;
            this.prevPosition = ((BilinkedNode280<I>) this.position).previousNode;
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

    /**
     *  main class contains regression test for the BilinkedList280 class.
     *  there will be no output, if no errors are found, except for displaying current
     *  state of the linked list, and when testing exceptions.
     */
	public static void main(String[] args) {
        // create a new node. Should be empty. In this test, we will use integers to preserve simplicity.
        BilinkedList280<Integer> bList = new BilinkedList280<>();
        // Number of errors found
		int listErrors = 0;
		int cursorErrors = 0;
        System.out.println("****Testing BilinkedList class (No Output if tests succeeds)****\n");

        // testing if the create list is empty, and if the cursor is on the right starting position.
		if (!bList.isEmpty()) {
            listErrors += 1;
            System.out.println("1. The created list should be empty! BilinkedList constructor method!");
        }
        // check if the current cursor position is initialized to null
        if (bList.position != null) {
		    cursorErrors += 1;
            System.out.println("2. The current position should be set to null. Check Constructor class!");
        }
        // check if the current prevPosition value is null
        if (bList.prevPosition != null) {
		    cursorErrors += 1;
            System.out.println("3. The current previous should be set to null. Check Constructor class!");
        }

        // Test Case 1: Insert values at the front of the list
        bList.insertFirst(10); // insert first item to the list

        // Check if the head and the tail of the list is 10, since this is the first item inserted.
        if (bList.head.item() != 10) {
            listErrors += 1;
            System.out.println("4. The 1st item of the list should now be 10. Head of the list should be also 10!");
        }
        if (bList.tail.item() != 10) {
            listErrors += 1;
            System.out.println("5. The tail should be set to 10 since it is the first item in the list!");
        }

        // inserting 2nd item to the list
		bList.insertFirst(9);

        // The new head value should now be 9
        if (bList.head.item() != 9) {
            listErrors += 1;
            System.out.println("6. Value 9 was inserted at front of the list, and is now the new head.");
        }
        // Testing if the tail remains the same
        if (bList.tail.item() != 10) {
            listErrors += 1;
            System.out.println("7. The tail value should not be changed since the new value was inserted to the first node.");
        }

        // inserting 3rd item into the list.
		bList.insertFirst(8);

        // testing if the head of the list has been updated to 8
        if (bList.head.item != 8) {
            System.out.println("8. The new head value should now be 8.");
        }

        // testing some cursor commands
        bList.goFirst();
        // cursor should now be pointed at the head of the list
        if (bList.position != bList.head) {
            cursorErrors += 1;
            System.out.println("9. The cursor should now be placed at the head of the node");
        }
        // test if previous position is null
        if (bList.prevPosition != null) {
            cursorErrors += 1;
            System.out.println("10. Previous position of position should be set to null since it is pointed at head");
        }

        // setting cursor to point at tail
        bList.goLast();
        if (bList.position != bList.tail) {
            cursorErrors += 1;
            System.out.println("11. The current cursor position should be set to tail");
        }
        // check previous position
        if (bList.prevPosition != ((BilinkedNode280) bList.tail).previousNode) {
            cursorErrors += 1;
            System.out.println("12. previous position should be set to the value before tail.");
        }

        // testing goBack
        bList.goBack();
        if (bList.position != ((BilinkedNode280) bList.tail).previousNode) {
            cursorErrors += 1;
            System.out.println("13. the new value should now be the node before the tail.");
        }
        // Check if the previous value is the item before current value
        if (bList.prevPosition != ((BilinkedNode280) bList.position).previousNode) {
            cursorErrors += 1;
            System.out.println("14. Previous node should be the value before the current cursor position");
        }
        // testing goBack even further
        bList.goBack();
        // cursor should now be positioned at head
        if (bList.position != bList.head) {
            cursorErrors += 1;
            System.out.println("15. The cursor should now be pointed at the head");
        }
        // test if previous position is null
        if (bList.prevPosition != null) {
            cursorErrors += 1;
            System.out.println("16. The previous position value should be null because the cursor is positioned at head");
        }
		bList.goForth();
		System.out.println("State Check A:\nCurrent bi-linked list state: \n" + bList);
		System.out.println("Current cursor position: " + bList.position.item);
		System.out.println("Current previous value position: " + ((BilinkedNode280) bList.position).previousNode.item()
				+ "\n");

        // testing insert end method
        bList.insertLast(42);
        // tail should now be 42
        if (bList.tail.item() != 42) {
            listErrors += 1;
            System.out.println("17. The new tail should now be 42 due to insertLast method");
        }
        bList.insertLast(66);
        if (bList.tail.item() != 66) {
        	listErrors += 1;
			System.out.println("18. New tail should now be 66 due to next insert last method");
		}
		// set cursor to last item of node.
		bList.goLast();
        if (bList.position != bList.tail) {
        	cursorErrors += 1;
        	System.out.println("19. The cursor should be on the tail position");
		}
		if (((BilinkedNode280) bList.tail).previousNode != ((BilinkedNode280) bList.position).previousNode) {
        	cursorErrors += 1;
			System.out.println("20. The before position of the current cursor position should be the item before tail!");
		}
		System.out.println("State Check B:\nCurrent bi-linked list state: \n" + bList);
		System.out.println("Current cursor position: " + bList.position.item);
		System.out.println("Current previous value position: " + ((BilinkedNode280) bList.position).previousNode.item()
		+ "\n");

		// testing delete Methods
		bList.deleteLast();
		// Last item should now be deleted
		if (bList.tail.item() != 42) {
			listErrors += 1;
			System.out.println("21. The last item should have been deleted!");
		}
		// check if the cursor current position value is null
		if (bList.position.item() != 42) {
			cursorErrors += 1;
			System.out.println("22. The current position value should now be null since tail is deleted");
		}
		// check value of cursor's previous node item() value
		if (bList.prevPosition.item() != 10) {
			cursorErrors += 1;
			System.out.println("23. The cursor previous position value should now be 42");
		}

		// testing delete methods starting with delete first
		bList.deleteFirst();
		if (bList.head.item() != 9) {
			listErrors += 1;
			System.out.println("24. The new head value should now be 9");
		}
		// testing delete first when cursor is pointing at head.
		bList.goFirst();
		if (bList.position.item() != 9) {
			listErrors += 1;
			System.out.println("25. The first item should now be 9 since the first item was previously deleted");
		}
		// testing effect of deleting first item when cursor is pointed at 1st item,
		bList.deleteFirst();
		if (bList.position.item() != 10) {
			cursorErrors += 1;
			System.out.println("26. The current position should now be 10 since the cursor was pointed at 1st item, which was deleted");
		}
		if (bList.prevPosition != null) {
			cursorErrors += 1;
			System.out.println("27. Previous position should still be null since the item deleted was where the cursor is.");
		}
		// Clearing the list for new set of items
		bList.clear();

		// adding some items to the list
		for (int i = 0; i < 10; i++) {
			bList.insertLast(i*2);
		}
		System.out.println("State Check C:\nCurrent bi-linked list state: \n" + bList);
		System.out.println("Current cursor position: " + bList.position);
		System.out.println("Current previous value position: " + bList.prevPosition
				+ "\n");

		// Testing delete Last function
		bList.deleteLast();
		if (bList.tail.item() != 16) {
			listErrors += 1;
			System.out.println("28. Last item should now be deleted and new tail should be 16");
		}

		// Testing delete last if the cursor is pointed at tail node
		bList.goLast();
		if (bList.item() != 16) {
			cursorErrors += 1;
			System.out.println("29. The current cursor position should be on 16.");
		}
		if (bList.prevPosition.item() != 14) {
			cursorErrors += 1;
			System.out.println("30. The previous position value should be 14");
		}
		// deleting last item
		bList.deleteLast();
		if (bList.item() != 14) {
			cursorErrors += 1;
			System.out.println("31. The new cursor item should now be 14 due to deletion of last item");
		}
		if (bList.prevPosition.item() != 12) {
			cursorErrors += 1;
			System.out.println("32. The new prev cursor item should now be 12 due to deletion of last item");
		}
		System.out.println("State Check D:\nCurrent bi-linked list state: \n" + bList);
		System.out.println("Current cursor position: " + bList.position.item());
		System.out.println("Current previous cursor position: " + bList.prevPosition.item() + "\n");

		// Testing deleteItem() method. Testing effects if cursor is at head.
		bList.goFirst();
		bList.deleteItem();
		if (bList.prevPosition != null) {
			cursorErrors += 1;
			System.out.println("33. The previous cursor node should still be null");
		}
		if (bList.position.item() != 2) {
			cursorErrors += 1;
			System.out.println("34. The current cursor position should now be pointed at 2");
		}
		bList.deleteItem();
		if (bList.prevPosition != null) {
			cursorErrors += 1;
			System.out.println("35. The previous cursor node should still be null");
		}
		if (bList.position.item() != 4) {
			cursorErrors += 1;
			System.out.println("36. The current cursor position should now be pointed at 4");
		}

		// Testing deleteItem if cursor is on the tail node.
		bList.goLast();
		bList.deleteItem();
		if (bList.prevPosition.item() != 10) {
			cursorErrors += 1;
			System.out.println("37. The previous cursor node item should now be 10");
		}
		if (bList.position.item() != 12) {
			cursorErrors += 1;
			System.out.println("38. The current cursor position should now be pointed at 12");
		}

		bList.deleteItem();
		if (bList.prevPosition.item() != 8) {
			cursorErrors += 1;
			System.out.println("39. The previous cursor node should now be 8");
		}
		if (bList.position.item() != 10) {
			cursorErrors += 1;
			System.out.println("40. The current cursor position should now be pointed at 10");
		}

		// Testing delete item if the cursor is pointed somewhere in the middle of the list
		System.out.println("State Check E:\nCurrent bi-linked list state: \n" + bList + "\n");
		bList.goFirst();
		bList.goForth();
		bList.deleteItem();
		if (bList.item() != 8) {
			cursorErrors += 1;
			System.out.println("41. The new position item should now be 8");
		}
		if (bList.prevPosition.item() != 4) {
			cursorErrors += 1;
			System.out.println("42. The previous position value should still be 4.");
		}
		if (bList.head.nextNode.item() != 8) {
			listErrors += 1;
			System.out.println("43. The next node after the head should now be 8");
		}

		bList.deleteItem();
		if (bList.item() != 10) {
			cursorErrors += 1;
			System.out.println("44. The new position item should now be 10");
		}
		if (bList.prevPosition.item() != 4) {
			cursorErrors += 1;
			System.out.println("45. The previous position value should still be 4.");
		}
		if (bList.head.nextNode.item() != 10) {
			listErrors += 1;
			System.out.println("46. The next node after the head should now be 10");
		}

		bList.deleteItem();
		if (bList.position != bList.head) {
			listErrors += 1;
			System.out.println("47. The current position should now be equals to the head value");
		}
		if (bList.prevPosition != null) {
			cursorErrors += 1;
			System.out.println("48. The prevPosition value should now be null");
		}

		bList.deleteItem();
		if (!bList.isEmpty()) {
			listErrors += 1;
			System.out.println("49. The list should now be empty!");
		}
		if (bList.prevPosition != null && bList.position != null && bList.head != null && bList.tail != null) {
			listErrors += 1;
			System.out.println("50. Previous position and position should both be null. So are head and tail values.");
		}

		System.out.println("State Check F:\nCurrent bi-linked list state: \n" + bList);
		System.out.println("Number of list errors found: " + listErrors);
        System.out.println("Number of cursor related errors found: " + cursorErrors);
        System.out.println("*****************************************************************************************");
        System.out.println("****Testing Cases Where Exceptions are Produced****");
        try {
			bList.deleteItem();
		}
		catch (NoCurrentItem280Exception e) {
			System.out.println("1. NoCurrentItem280Exception: Cursor is pointing on a null object. Produced by deleteItem()");
		}
		try {
			bList.goLast();
		}
		catch (ContainerEmpty280Exception e) {
			System.out.println("2. ContainerEmpty280Exception: Cursor is pointing on a null object. Produced by goLast()");
		}

		try {
			bList.deleteFirst();
		}
		catch (ContainerEmpty280Exception e) {
			System.out.println("3. ContainerEmpty280Exception: List is Empty! Cannot delete 1st item from an empty List." +
					"Produced by deleteFirst");
		}
		try {
			bList.deleteLast();
		}
		catch (ContainerEmpty280Exception e) {
			System.out.println("4. ContainerEmpty280Exception: List is Empty! Cannot delete last item from an empty List. " +
					"Produced by deleteLast()");
		}

		for (int i = 0; i < 10; i++) {
        	bList.insertLast(i);
        	bList.insertFirst(i*10);
		}
		try {
			bList.insertBefore(10);
		}
		catch (InvalidState280Exception e) {
			System.out.println("5. InvalidState280Exception: Cannot insert item when cursor pointed at before position. " +
					"Produced by insertBefore()");
		}
		bList.goBefore();
		try {
        	bList.goBack();
		}
		catch (BeforeTheStart280Exception e)  {
			System.out.println("6. BeforeTheStart280Exception: Cannot go back any further. Cursor on before position. " +
					"Produced by goBack()");
		}

		System.out.println("Testing Complete!");
	}
} 
