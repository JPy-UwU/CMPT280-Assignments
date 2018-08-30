package lib280.dispenser;

import lib280.exception.ContainerEmpty280Exception;
import lib280.exception.ContainerFull280Exception;
import lib280.tree.ArrayedBinaryTreeIterator280;
import lib280.tree.IterableArrayedHeap280;


public class PriorityQueue280<I extends Comparable<? super I>> {

	// This is the heap that we are restricting.
	// Items in the priority queue get stored in the heap.
	protected IterableArrayedHeap280<I> items;


	/**
	 * Create a new priority queue with a given capacity.
	 * @param cap The maximum number of items that can be in the queue.
	 */
	public PriorityQueue280(int cap) {
		items = new IterableArrayedHeap280<I>(cap);
	}

	public String toString() {
		return items.toString();
	}

	// TODO
	// Add Priority Queue ADT methods (from the specification) here.

	/**
	 * Insert an item to the list, based on Priority
	 * @param item value to be inserted
	 * @throws ContainerFull280Exception priorityQueue cannot be full
	 */
	public void insert(I item) throws ContainerFull280Exception {
		items.insert(item);	// Use normal insert method obtained from Arrayed Binary Heap with iterator
	}

	/**
	 * Check if the queue is empty
	 * @return true if the queue is empty
	 */
	public boolean isEmpty() {
		return items.count() == 0;
	}

	/**
	 * Get the number of items stored in our priority queue
	 * @return number of items in our priority queue
	 */
	public int count() {
		return items.count();
	}

	/**
	 * Get the item with max priority in our queue
	 * @return Value with the highest priority
	 * @throws ContainerEmpty280Exception queue cannot be empty
	 */
	public I maxItem() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {	// Check if the queue is empty. If it is, throw an exception
			throw new ContainerEmpty280Exception("Cannot Delete off an empty tree!");
		}
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();	// Create an iterator object
		iter.goFirst();	// We use an iterator because we cannot directly access items array using Arrayed Binary Heap with iterator
		return iter.item();	// Since the max item is always at root, return the 1st item of our array
	}

	/**
	 * Deletes the item with the highest priority
	 * @throws ContainerEmpty280Exception Queue cannot be empty!
	 */
	public void deleteMax() throws ContainerEmpty280Exception {
		if (items.isEmpty()) {	// Check if the tree is empty
			throw new ContainerEmpty280Exception("Cannot Delete off an empty tree!");
		}
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();
		iter.goFirst();
		items.deleteAtPosition(iter);	// Get the item with max priority, and delete it using deleteAtPosition method from tree
	}

	/**
	 * Gets the item with the lowest priority in our queue
	 * @return item with the lowest priority
	 * @throws ContainerEmpty280Exception queue cannot be empty
	 */
	public I minItem() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {	// Check if the tree is empty
			throw new ContainerEmpty280Exception("Cannot Delete off an empty tree!");
		}
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();	// Create an iterator
		iter.goFirst();	// Go to the first item in the queue

		if (this.count() == 1) {	// If there is only 1 item in the array, return that only value.
			return iter.item();
		}
		else {	// Otherwise
			I currItem = iter.item();	// Store the initial value to a variable
			while (iter.itemExists()) {	// While the item exist, continue iterating
				if (iter.item().compareTo(currItem) < 0) {	// If iter.item() is less than the initial holder value
					currItem = iter.item();		// Replace holder with the current iter item
				}
				iter.goForth();	// Advance forward
			}
			return currItem;	// If we reach this line, we got the minimum value.
		}
	}

	/**
	 * Deletes the item with the lowest priority in our queue
	 * @throws ContainerEmpty280Exception queue cannot be empty
	 */
	public void deleteMin() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {	// Check if the tree is empty
			throw new ContainerEmpty280Exception("Cannot Delete off an empty tree!");
		}
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();	// Create a new iterator object
		iter.goFirst();	// goto the first item in the array
		// While item exist, and the iterator is not pointing at the minimum item in the array
		while (iter.itemExists() && iter.item().compareTo(this.minItem()) != 0) {
			iter.goForth();	// Keep going forward
		}
		items.deleteAtPosition(iter);	// Get the minimum value of this tree, then delete it using items.deleteAtPosition()
	}

	/**
	 * Delete all items with the same priority, and are the values with the highest priority
	 * @throws ContainerEmpty280Exception Queue cannot be empty
	 */
	public void deleteAllMax() throws ContainerEmpty280Exception {
		if (this.isEmpty()) {	// Check if the tree is empty
			throw new ContainerEmpty280Exception("Cannot Delete off an empty tree!");
		}
		ArrayedBinaryTreeIterator280<I> iter = items.iterator();	// Create an iterator to get items from our queue
		iter.goFirst();	// Go to the first item
		I maxItem = this.maxItem();	// Get the max item in our priority queue
		// while the tree is not empty, and the current iter value == maxItem of this queue
		while (!this.isEmpty() && iter.item().compareTo(maxItem) == 0) {
			deleteMax();	// keep deleting max value until the maxItem is not equal to iterator item
		}
	}

	/**
	 * Check if our queue is full
	 * @return true if container is full
	 */
	public boolean isFull() {
		return items.count() == items.capacity();
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
