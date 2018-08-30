/* ArrayedList280.java
 * ---------------------------------------------
 * Copyright (c) 2010 University of Saskatchewan
 * All Rights Reserved
 * --------------------------------------------- */

package lib280.list;

import lib280.base.CursorPosition280;
import lib280.exception.*;

/**
 * This arrayed list is implemented as a circular list to allow for
 * constant-time insertions and deletions at the beginning and the end.
 * @author eramian
 *
 * @param <I>
 */
public class ArrayedList280<I> implements SimpleList280<I> {

	/**
	 * Array where the elements are stored.
	 */
	protected I[] listElements;
	
	/**
	 * Indices of the beginning and end of the list.
	 * List is empty when head = tail.  List is full when
	 * ((this.tail - 1) mod capacity) == this.head 
	 * 
	 */
	protected int head, tail;
	
	/**
	 * Size of the listElements array, maximum number of elements in the list.
	 */
	protected int capacity;
	
	/**
	 * Index of the position of the cursor.
	 */
	protected int position;
	
	/**
	 * Number of elements in the list
	 */
	protected int numEl;
	
	/**
	 * Do searches continue or start anew?
	 */
	protected boolean continueSearch;
	

	private int mod( int a, int b ) {
		return ((a % b) + b) % b;
	}

	/**
	 * Create a new, empty list.
	 * @param capacity The maximum number of elements in the list.
	 */
	@SuppressWarnings("unchecked")
	public ArrayedList280(int capacity) {
		this.head = 0;
		this.tail = 0;
		this.capacity = capacity;
		this.position = 0;
		this.listElements = (I[]) new Object[capacity];
	}
	
	@Override
	public void deleteFirst() throws ContainerEmpty280Exception {
		if(isEmpty()) throw new ContainerEmpty280Exception();
		
		// If the cursor is on the first item, we have to move it.
		if( this.position == this.head )
			this.position = this.mod(this.head - 1, this.capacity);
		
		this.head = this.mod(this.head - 1, this.capacity);
		this.numEl--;
		
		if( isEmpty() ) this.position = ArrayedListIterator280.beforePos;
	}



	@Override
	public void deleteLast() throws ContainerEmpty280Exception {
		if(isEmpty()) throw new ContainerEmpty280Exception();
		
		// If the cursor is on the last item, we have to move it.
		if( this.position == this.tail )
			this.position = this.mod(this.tail + 1,  this.capacity);
		
		this.tail = this.mod(this.tail + 1,  this.capacity);
		this.numEl--;
		
		if( isEmpty() ) this.position = ArrayedListIterator280.beforePos;
	}



	@Override
	public I firstItem() throws ContainerEmpty280Exception {
		if(this.isEmpty()) throw new ContainerEmpty280Exception();
		
		return this.listElements[this.mod(this.head-1, this.capacity)];
	}



	@Override
	public void insertFirst(I x) throws ContainerFull280Exception {
		if(this.isFull()) throw new ContainerFull280Exception();
		
		this.listElements[this.head] = x;
		this.head = this.mod( this.head + 1, this.capacity);
		this.numEl++;
	}

	

	@Override
	public void insertLast(I x) throws ContainerFull280Exception {
		if(this.isFull()) throw new ContainerFull280Exception();
		
		this.tail = this.mod(this.tail - 1, this.capacity);
		
		this.listElements[this.tail] = x;
		this.numEl++;
	}


	/**
	 * Returns the last element in the list.
	 * @precond The list is not empty. 
	 */
	public I lastItem() throws ContainerEmpty280Exception {
		if(this.isEmpty()) throw new ContainerEmpty280Exception();
		return this.listElements[tail];
	}
	

	@Override
	public I item() throws NoCurrentItem280Exception {
		if(!this.itemExists()) throw new NoCurrentItem280Exception();
		
		return this.listElements[this.position];
	}



	@Override
	public boolean itemExists() {
		return this.position != ArrayedListIterator280.beforePos && this.position != ArrayedListIterator280.afterPos;
	}



	@SuppressWarnings("unchecked")
	@Override
	public ArrayedList280<I> clone() throws CloneNotSupportedException {
		return (ArrayedList280<I>) super.clone();
	}



	@Override
	public boolean after() {
		return this.position == ArrayedListIterator280.afterPos;
	}



	@Override
	public boolean before() {
		return this.position == ArrayedListIterator280.beforePos;
	}



	@Override
	public void goAfter() {
		this.position = ArrayedListIterator280.afterPos;
	}



	@Override
	public void goBefore() {
		this.position = ArrayedListIterator280.beforePos;
	}



	@Override
	public void goFirst() throws ContainerEmpty280Exception {
		if( isEmpty() ) throw new ContainerEmpty280Exception();
		this.position = this.mod(this.head-1, capacity);
	}


	@Override
	public void goForth() throws AfterTheEnd280Exception {
		if( this.position == ArrayedListIterator280.afterPos )
			throw new AfterTheEnd280Exception();
		else if( this.position == ArrayedListIterator280.beforePos ) this.position = head;
		else if( this.position == this.tail ) this.position = ArrayedListIterator280.afterPos;
		else this.position = this.mod(this.position - 1, capacity);
	}



	@Override
	public CursorPosition280 currentPosition() {
		ArrayedListIterator280<I> iter = new ArrayedListIterator280<I>(this.listElements, this.head, this.tail, this.numEl);
		iter.setPosition(this.position);
		return iter;
	}



	@SuppressWarnings("unchecked")
	@Override
	public void goPosition(CursorPosition280 c) throws IllegalArgumentException {
		if( !(c instanceof ArrayedListIterator280) ) 
			throw new IllegalArgumentException("Arguement is not an ArrayedListIterator280.");
		ArrayedListIterator280<I> iter = (ArrayedListIterator280<I>)c;
		if( iter.capacity != this.capacity ||
		    iter.head != this.head ||
			iter.tail != this.tail )
			throw new IllegalArgumentException("Iterator list does not match this list.");

		this.position = iter.position;
	}



	@Override
	public void delete(I x) throws ItemNotFound280Exception, ContainerEmpty280Exception {
		
		if( this.isEmpty() ) throw new ContainerEmpty280Exception("Cannot delete from an empty list.");
		
		// save cursor state		
		@SuppressWarnings("unchecked")
		ArrayedListIterator280<I> savePos = (ArrayedListIterator280<I>) this.currentPosition();
		
		// If there was an item at the cursor, save it.
		I saveItem;
		if( savePos.itemExists() )
			saveItem = savePos.item();
		else 
			saveItem = null;
		
		// Search for the item to be deleted.
		this.search(x);
		if(!this.itemExists()) throw new ItemNotFound280Exception();
		
		// If we are deleting the item at the saved cursor position, don't bother restoring it,
		// just leave it where it is.
		if( this.position == savePos.position)
			this.deleteItem();
		else {
			this.deleteItem();
			// If there was previously an item at the cursor, find it again.
			if( saveItem != null ) {
				// Restore the cursor to the item at which it was previously.
				boolean saveContinue = continueSearch;
				this.restartSearches();
				search(saveItem);
				continueSearch = saveContinue;			}
			else {
				// If not, it must have been 'before' or 'after'.  Just restore
				// it to that.
				this.position = savePos.position;
			}
		}	
	
	}



	@Override
	public void insert(I x) throws ContainerFull280Exception {		
		insertFirst(x);
	}



	@Override
	public I obtain(I y) throws ItemNotFound280Exception {
		// save cursor state
		CursorPosition280 savePos = this.currentPosition();
		
		// Search for y
		this.search(y);
		if(!this.itemExists()) throw new ItemNotFound280Exception();
		I result = this.item();
		
		// Restore cursor state
		this.goPosition(savePos);
		return result;
	}



	@Override
	public boolean has(I y) {
		// save cursor state
		CursorPosition280 savePos = this.currentPosition();
		
		// Search for y
		this.search(y);
		boolean result = itemExists();
		
		// Restore cursor state
		this.goPosition(savePos);
		
		return result;
	}



	@SuppressWarnings("unchecked")
	@Override
	public boolean membershipEquals(I x, I y) {
		if ((x instanceof Comparable) && (y instanceof Comparable))
			return  0==((Comparable<I>)x).compareTo(y);
		else return x.equals(y);
	}



	@Override
	public void deleteItem() throws NoCurrentItem280Exception {
		if( !itemExists() )
			throw new NoCurrentItem280Exception();
		
		for(int i = this.position; i != this.head; i = this.mod(i + 1, this.capacity) ) {
			listElements[i] = listElements[this.mod(i+1, this.capacity)];
		}
		this.head = this.mod(this.head - 1, this.capacity);
		this.numEl--;	
	}



	@Override
	public void restartSearches() {
		continueSearch = false;
	}



	@Override
	public void resumeSearches() {
		continueSearch = true;
	}



	@Override
	public void search(I x) {
		if (!continueSearch)
			goFirst();
		else if (!after())
			goForth();
	
		while (!after() && !membershipEquals(x, item()))
			goForth();
	}

	

	@Override
	public void clear() {
		this.head = 0;
		this.tail = 0;
		this.numEl = 0;
		this.position = ArrayedListIterator280.beforePos;
	}



	@Override
	public boolean isEmpty() {
		return this.head == this.tail && this.numEl == 0;
	}



	@Override
	public boolean isFull() {
		return this.head == this.tail && this.numEl == this.capacity;
	}


	/**
	 * Obtain the number of elements in the list.
	 * 
	 * @return The number of elements in the list.
	 */
	public int count() {
		return this.numEl;
	}
	
	/**
	 * Obtain the maximum number of elements that can be in the list.
	 * @return The capacity of the list.
	 */
	public int capacity() {
		return this.capacity;
	}
	
	@Override
	public String toString() {
		// If the list is empty, we're done.
		if( this.isEmpty() ) return "<Empty>";
		
		// Save cursor position.
		CursorPosition280 p = this.currentPosition();
		
		String result = "";
		
		// Iterate over all elements...
		this.goFirst();
		while( !this.after() ) {
			result = result + this.item() + ", ";
			this.goForth();
		}
		
		// Restore cursor
		this.goPosition(p);
		
		return result;
	}
	
	public ArrayedListIterator280<I> iterator() {		
		return new ArrayedListIterator280<I>(this.listElements, this.head, this.tail, this.numEl);
	}
	
	
	/**
	 * Obtain the item at position idx in the list. The first element is at index 0.
	 * @param idx - index (position) of the desired list element
	 * @return the element at index idx in the arrayed list.
	 * @throws InvalidArgument280Exception if there is no item at the specified index.
	 */
	public I getItemAtIndex(int idx) {
		if( idx < 0 || idx >= this.numEl ) {
			throw new InvalidArgument280Exception("There is no element at position " + idx + "in the list.");
		}
		return this.listElements[this.mod(this.head-idx-1, this.capacity)];
	}
	
	
	/**
	 * Regression test.
	 */
	public static void main(String[] args) {

		// Probably doesn't achieve 100% coverage, but comes pretty close.
		
		ArrayedList280<Integer> L = new ArrayedList280<Integer>(5);
		
		// test isEmpty, isFull, insert, insertLast, insert,
		// toString() (which implicitly tests iteration to some extent)
		
		System.out.println(L);
		
		System.out.print("List should be empty...");
		if( L.isEmpty() ) System.out.println("and it is.");
		else System.out.println("ERROR: and it is *NOT*.");

		
		L.insert(5);
		L.insert(4);
		L.insertLast(3);
		L.insertLast(10);
		//L.insertFirst(3);
		//L.insertFirst(4);
		//L.insertFirst(5);
		
		System.out.print("Count should be 4 ....");
		if( L.count() == 4 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		L.insertFirst(2);
		System.out.print("Count should be 5 ....");
		if( L.count() == 5 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.print("List should be full...");
		if( L.isFull() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

		System.out.println("List should be: 2, 4, 5, 3, 10, ");
		System.out.print(  "     and it is: ");
		System.out.println(L);
		
		// Test getItemAtIndex
		if( L.getItemAtIndex(0) != 2 ) 
			System.out.println("Error: Item at index 0 should be 2 but it is: " + L.getItemAtIndex(0));
		if( L.getItemAtIndex(1) != 4 ) 
			System.out.println("Error: Item at index 1 should be 4 and it is: " + L.getItemAtIndex(1));
		if( L.getItemAtIndex(2) != 5 ) 
			System.out.println("Error: Item at index 2 should be 5 and it is: " + L.getItemAtIndex(2));
		if( L.getItemAtIndex(3) != 3 ) 
			System.out.println("Error: Item at index 3 should be 3 and it is: " + L.getItemAtIndex(3));
		if( L.getItemAtIndex(4) != 10 ) 
			System.out.println("Error: Item at index 4 should be 10 and it is: " + L.getItemAtIndex(4));

		try {
			System.out.println("Getting item at nonexistant index 5...");
			L.getItemAtIndex(5);
		}
		catch(InvalidArgument280Exception e) {
			System.out.println("Caught exception.  OK.  ");
		}
		try {
			System.out.println("Getting item at nonexistant index -1...");
			L.getItemAtIndex(-1);
		}
		catch(InvalidArgument280Exception e) {
			System.out.println("Caught exception.  OK.  ");
		}


		// Test delete methods
		L.delete(5);
		System.out.print("Count should be 4 ....");
		if( L.count() == 4 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println(L);

		L.deleteFirst();
		System.out.print("Count should be 3 ....");
		if( L.count() == 3 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
		System.out.println(L);

		L.deleteLast();
		System.out.print("Count should be 2 ....");
		if( L.count() == 2 ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");
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
	
		
		// Test deleting to an empty list.
		L.delete(5);
		System.out.println("Deleted 5");
		System.out.print("cursor should be at 'after' ....");
		if( L.after() ) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

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

		System.out.println("Adding to head of full list.");
		try {
			L.insertFirst(3);
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerFull280Exception e ) {
			System.out.println("Caught exception. OK!");
		}

		System.out.println("Adding to head of full list.");
		try {
			L.insert(3);
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerFull280Exception e ) {
			System.out.println("Caught exception. OK!");
		}
		
		System.out.println("Adding to tail of full list.");
		try {
			L.insertLast(3);
			System.out.println("ERROR: exception should have been thrown, but wasn't.");			
		}
		catch( ContainerFull280Exception e ) {
			System.out.println("Caught exception. OK!");
		}
	
	
		// firstItem(), lastItem(), goForth()
		L.search(5);
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
		System.out.print("count should be zero ....");
		if( L.count() == 0) System.out.println("and it is.  OK!");
		else System.out.println("and it is not.  ERROR!");

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

	}
}
