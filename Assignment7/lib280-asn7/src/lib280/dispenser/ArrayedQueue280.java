package lib280.dispenser;

import lib280.exception.ContainerFull280Exception;
import lib280.exception.NoCurrentItem280Exception;
import lib280.list.ArrayedList280;

public class ArrayedQueue280<I> extends Queue280<I> {

	/**
	 * Obtain the maximum number of items that can be in the queue.
	 * @return Maximum capacity of the queue.
	 */
	public int capacity() {
		return ((ArrayedList280<I>)this.queueItems).capacity();
	}
	
	/**
	 * Number of items in the queue.
	 * @return number of items in the queue.
	 */
	public int count() {
		return ((ArrayedList280<I>)this.queueItems).count();		
	}
	
	@Override
	public ArrayedQueue280<I> clone() throws CloneNotSupportedException {
		return (ArrayedQueue280<I>)super.clone();
	}


	public ArrayedQueue280(int capacity) {
		queueItems = new ArrayedList280<I>(capacity);
	}
	
	
	public static void main(String args[]) {
		ArrayedQueue280<Integer> Q = new ArrayedQueue280<Integer>(5);
		Integer x;
		
		// isEmpty() when queue is empty - should return true
		if( !Q.isEmpty() ) System.out.println("ERROR: Expected empty queue, but isEmpty() returned false.");
		
		// Test clear on empty queue -- queue should remain empty.
		Q.clear();
		if( !Q.isEmpty() ) System.out.println("ERROR: Expected empty queue, but isEmpty() returned false.");

		// item() on empty queue - should throw NoCurrentItem280Exception
		try {
			x = Q.item();
			System.out.println("ERROR: Expected NoCurrentItem280Exception to be thrown, but no exception was thrown.");
		}
		catch ( NoCurrentItem280Exception e ) {			
		}
		catch ( Exception e ) {
			System.out.println("ERROR: Expected NoCurrentItem280Exception to be thrown, but a different exception was thrown.");
		}
		
		// itemExists on empty queue - should return false
		if( Q.itemExists() ) System.out.println("ERROR: Expected no current item, but itemExists() returned true.");
		
		// deleteItem() when queue is empty
		try {
			Q.deleteItem();
			System.out.println("ERROR: Expected NoCurrentItem280Exception to be thrown, but no exception was thrown.");
		}
		catch ( NoCurrentItem280Exception e ) {			
		}
		catch ( Exception e ) {
			System.out.println("ERROR: Expected NoCurrentItem280Exception to be thrown, but a different exception was thrown.");
		}
			
		// toString() when queue is empty.
		System.out.println("Testing toString()...current queue:  " + Q);
		System.out.println("                   Expected output:  Queue starting with front item: <Empty>");
		System.out.println("  Cloned queue should be identical:  " + Q);
		
		// count() when queue is empty - should return 0
		if( Q.count() != 0 ) System.out.println("ERROR: expected count to be 0, got " + Q.count());
		
		// capacity() should return 5
		if( Q.capacity() != 5 ) System.out.println("ERROR: expected capacity to be 5, got "+ Q.capacity());
		
		// insert(x) on non-full queue
		Q.insert(11);
		x = Q.item();
		if( x != 11 ) System.out.println("ERROR: Queue head should be 11, got: " + x);
		Q.insert(12);
		x = Q.item();
		if( x != 11 ) System.out.println("ERROR: Queue head should be 11, got: " + x);
		Q.insert(13);
		x = Q.item();
		if( x != 11 ) System.out.println("ERROR: Queue head should be 11, got: " + x);

		
		// toString() when queue is non-empty.
		System.out.println("Testing toString()...current queue:  " + Q);
		System.out.println("                   Expected output:  Queue starting with front item: 11, 12, 13,");
		System.out.println("  Cloned queue should be identical:  " + Q);
			
		
		// isEmpty() when queue not empty - should return false
		if( Q.isEmpty() ) System.out.println("ERROR: isEmpty() returned true for a non-empty queue.");
		
		// test item() when queue non-empty -- should return 11, no exceptions thrown.
		try {
			x = Q.item();
		}
		catch(Exception e) {
			System.out.println("ERROR! Caught exception obtaining item from non-empty queue.");
		}
		finally {
			if(x != 11) System.out.println("ERROR: expected head of queue to be 11, got: " + x);
		}
		
		// test itemExists() when queue non-empty -- should return true.
		if( !Q.itemExists() ) System.out.println("ERROR: itemExists() returned false for a non-empty queue.");
		
		// test isFull() when queue is not full -- should return false
		if( Q.isFull() ) System.out.println("ERROR: isFull() returned true for a non-full queue.");
		
		// test count() when queue is not full -- should return 3
		if( Q.count() != 3) System.out.println("ERROR: count() should be 3, got: " + Q.count());
		
		Q.insert(14);
		x = Q.item();
		if( x != 11 ) System.out.println("ERROR: Queue head should be 11, got: " + x);
		Q.insert(15);
		x = Q.item();
		if( x != 11 ) System.out.println("ERROR: Queue head should be 11, got: " + x);
		
		// Queue should now be full.
		
		// Test isFull() on full queue - should return true
		if( !Q.isFull() ) System.out.println("ERROR: isFull() returned false on a full queue.");

		// insert(x) when queue is full
		try {
			Q.insert(16);
			System.out.println("ERROR: Expected ContainerFull280Exception to be thrown, but no exception was thrown.");
		}
		catch ( ContainerFull280Exception e ) {			
		}
		catch ( Exception e ) {
			System.out.println("ERROR: Expected ContainerFull280Exception to be thrown, but a different exception was thrown.");
		}
		
		// deleteItem() on non empty queue.
		try {
			Q.deleteItem();
		}
		catch (Exception e) {
			System.out.println("ERROR: Unexpected exception caught while deleting on non-empty queue.");
		}
		x = Q.item();
		if( x != 12 ) System.out.println("ERROR: Queue head should be 12, got: " + x);
		System.out.println("Current Queue: " + Q);
		System.out.println("     Expected: Queue starting with front item: 12, 13, 14, 15," );
		if( Q.count() != 4 ) System.out.println("Expected count() to be 4, got: " + Q.count());
		
		// clear() on non-empty queue
		Q.clear();
		System.out.println("Testing toString()...current queue:  " + Q);
		System.out.println("                   Expected output:  Queue starting with front item: <Empty>");
		System.out.println("  Cloned queue should be identical:  " + Q);
		if( Q.count() != 0 ) System.out.println("Expected count() to be 0, got: " + Q.count());

	}


}
