package lib280.dispenser;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.tree.ArrayedBinaryTreeIterator280;
import lib280.tree.IterableArrayedHeap280;

public class PriorityQueue280<I extends Comparable<? super I>> {
	
	protected IterableArrayedHeap280<I> items;
	
	
	public PriorityQueue280(int cap) {
		items = new IterableArrayedHeap280<I>(cap);
	}
	
	/**
	 * Enqueues an item into the queue in priority order.
	 * 
	 * @param item The item to be inserted.
	 * @precond The queue must not be full.
	 */
	public void insert(I item) {
		if( items.isFull() )
			throw new ContainerFull280Exception();
		items.insert(item);
	}
	
	
	/** 
	 * Determine if the queue is full.
	 * @return true if the queue is full, false otherwise.
	 * 
	 */
	public boolean isFull() {
		return items.isFull();
	}
	
	
	/** Determine if the queue is empty.
	 * 
	 * @return true if the queue is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return items.isEmpty();
	}
	
	/**
	 * Determine the number of items in the queue.
	 *
	 * @return the number of items in the priority queue.
	 */
	public int count() {
		return items.count();
	}
	
	/**
	 * Obtain the largest item in the queue.
	 * @return A reference to the largest item in the priority queue.
	 * @precond The queue must not be empty.
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 */
	public I maxItem() throws ContainerEmpty280Exception {
		if( items.isEmpty() )
			throw new ContainerEmpty280Exception("Cannot obtain highest priorty item from an empty queue.");
		return items.item();
	}
	
	/**
	 * Find the item in the queue with lowest priority.
	 * @return The item in the queue with lowest priority.
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 */
	public I minItem() throws ContainerEmpty280Exception {
		// Verify precondtion
		if( items.isEmpty() )
			throw new ContainerEmpty280Exception("Cannot obtain lowest priorty item from an empty queue.");
		
		// Get an iterator for the internal heap.
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();
		iter.goFirst();
		
		// Let the head of the queue be the smallest item
		// seen so  far and advance the iterator.
		I min = iter.item();
		iter.goForth();
		
		// As long as there are more items...
		while(iter.itemExists()) {
			// ... and the current item is lower priority than the smallest one so far...
			if( iter.item().compareTo(min) < 0 ) {
				// ... save the current item as the smallest priorty item seen so far.
				min = iter.item();
			}
			iter.goForth();
		}
		
		// Return the smallest item.
		return min;
	}
	
	/**
	 * Delete the item with lowest priority.
	 */
	public void deleteMin() {
		// Verify the precondtion
		if( items.isEmpty() )
			throw new ContainerEmpty280Exception("Cannot delete lowest priorty item from an empty queue.");
		
		// Get the smallest priority item.
		I min = minItem();
		
		// Get an iterator for the lib280.tree
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();
		
		// Use the iterator to search for the position of the
		// min.
		iter.goFirst();
		while(iter.itemExists()) {
			// When we find the item min...
			if( iter.item() == min ) {
				// Remove it.
				items.deleteAtPosition(iter);
				return;
			}
			iter.goForth();
		}
		return;
	}
	
	/**
	 * Delete all the items that have priority equal
	 * to the item with highest priority.
	 */
	public void deleteAllMax() {
		// Verify the precondition
		if( items.isEmpty() )
			throw new ContainerEmpty280Exception("Cannot delete highest priorty items from an empty queue.");
		
		// Save a copy of the highest priorty item.
		I max = items.item();
		
		// As long as the queue isn't empty, and the head of the
		// queue has priority equal to that of max, remove the head.
		while(items.itemExists() && items.item().compareTo(max) == 0) {
			items.deleteItem();
		}
	}
	
	/** 
	 * Delete the largest item in the queue (ie - dequeue).
	 * @precond The queue must not be empty.
	 * @throws ContainerEmpty280Exception if the queue is empty.
	 */
	public void deleteMax() throws ContainerEmpty280Exception {
		if( items.isEmpty() )
			throw new ContainerEmpty280Exception("Cannot delete largest priority item of an empty queue.");
		items.deleteItem();
	}

	
	
	public String toString() {
		return items.toString();	
	}
	
	
	

	
	public static void main(String args[]) {
		class PriorityItem<I> implements Comparable<PriorityItem<I>> {
			I item;
			Double priority;
						
			public PriorityItem(I item, Double priority) {
				super();
				this.item = item;
				this.priority = priority;
			}

			public int compareTo(PriorityItem<I> o) {
				return this.priority.compareTo(o.priority);
			}
			
			public String toString() {
				return this.item + ":" + this.priority;
			}
		}
		
		PriorityQueue280<PriorityItem<String>> Q = new PriorityQueue280<PriorityItem<String>>(5);
		
		// Test isEmpty()
		if( !Q.isEmpty()) 
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");
		
		// Test insert() and maxItem()
		Q.insert(new PriorityItem<String>("Sing", 5.0));
		if( Q.maxItem().item.compareTo("Sing") != 0) {
			System.out.println("??Error: Front of queue should be 'Sing' but it's not. It is: " + Q.maxItem().item);
		}
		
		// Test isEmpty() when queue not empty
		if( Q.isEmpty()) 
			System.out.println("Error: Queue is not empty, but isEmpty() says it is.");
		
		// test count()
		if( Q.count() != 1 ) {
			System.out.println("Error: Count should be 1 but it's not.");			
		}

		// test minItem() with one element
		if( Q.minItem().item.compareTo("Sing")!=0) {
			System.out.println("Error: min priority item should be 'Sing' but it's not.");
		}	
		
		// insert more items
		Q.insert(new PriorityItem<String>("Fly", 5.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Dance", 3.0));
		if( Q.maxItem().item.compareTo("Sing")!=0) System.out.println("Front of queue should be 'Sing' but it's not.");
		Q.insert(new PriorityItem<String>("Jump", 7.0));
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");

		if(Q.minItem().item.compareTo("Dance") != 0) System.out.println("minItem() should be 'Dance' but it's not.");
		
		if( Q.count() != 4 ) {
			System.out.println("Error: Count should be 4 but it's not.");			
		}
		
		// Test isFull() when not full
		if( Q.isFull()) 
			System.out.println("Error: Queue is not full, but isFull() says it is.");
		
		Q.insert(new PriorityItem<String>("Eat", 10.0));
		if( Q.maxItem().item.compareTo("Eat")!=0) System.out.println("Front of queue should be 'Eat' but it's not.");

		if( !Q.isFull()) 
			System.out.println("Error: Queue is full, but isFull() says it isn't.");

		// Test insertion on full queue
		try {
			Q.insert(new PriorityItem<String>("Sleep", 15.0));
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got none.");
		}
		catch(ContainerFull280Exception e) {
			// Expected exception
		}
		catch(Exception e) {
			System.out.println("Expected ContainerFull280Exception inserting to full queue but got a different exception.");
			e.printStackTrace();
		}
		
		// test deleteMin
		Q.deleteMin();
		if(Q.minItem().item.compareTo("Sing") != 0) System.out.println("Min item should be 'Sing', but it isn't.");

		Q.insert(new PriorityItem<String>("Dig", 1.0));
		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		// Test deleteMax
		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Jump")!=0) System.out.println("Front of queue should be 'Jump' but it's not.");

		Q.deleteMax();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		if(Q.minItem().item.compareTo("Dig") != 0) System.out.println("minItem() should be 'Dig' but it's not.");

		Q.deleteMin();
		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");

		Q.insert(new PriorityItem<String>("Scream", 2.0));
		Q.insert(new PriorityItem<String>("Run", 2.0));

		if( Q.maxItem().item.compareTo("Fly")!=0) System.out.println("Front of queue should be 'Fly' but it's not.");
		
		// test deleteAllMax()
		Q.deleteAllMax();
		if( Q.maxItem().item.compareTo("Scream")!=0) System.out.println("Front of queue should be 'Scream' but it's not.");
		if( Q.minItem().item.compareTo("Scream") != 0) System.out.println("minItem() should be 'Scream' but it's not.");
		Q.deleteAllMax();

		// Queue should now be empty again.
		if( !Q.isEmpty()) 
			System.out.println("Error: Queue is empty, but isEmpty() says it isn't.");

		System.out.println("Regression test complete.");
	}
}
